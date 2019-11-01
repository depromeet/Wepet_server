package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.domains.category.Category;
import com.depromeet.wepet.domains.category.CategoryService;
import com.depromeet.wepet.domains.location.google.PlaceApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private WebClient webClient;
    private String googleApiKey;
    private List<String> fileds;
    private CategoryService categoryService;

    public LocationService(@Value("${google.api.url}") String url,
                            @Value("${google.api.key}") String googleApiKey,
                            @Value("${google.api.fields}") List<String> fileds,
                           CategoryService categoryService) {
        this.webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
        this.googleApiKey = googleApiKey;
        this.categoryService = categoryService;
        this.fileds = fileds;
    }

    public Collection<Location> getLocations(double latitude, double longitude, long distance, long categoryId) {
        Collection<Category> categories = new ArrayList<>();
        if (categoryId == 0) {
            categories = categoryService.getCategories();
        } else {
            categories.add(categoryService.getCategory(categoryId));
        }

        Collection<Location> locations = new ArrayList<>();
        for (Category category : categories) {
            String searchKeyword = category.getSearchKeyword();
            PlaceApiResponse apiResponse = webClient
                    .get()
                    .uri("/nearbysearch/json?location={latitude},{longitude}&radius={distance}&type={searchKeyword}&key={googleApiKey}&language=ko", latitude, longitude, distance, searchKeyword, googleApiKey)
                    .retrieve()
                    .bodyToMono(PlaceApiResponse.class)
                    .block();

            locations.addAll(
                    apiResponse.getResults()
                            .stream()
                            .map(Location::of)
                            .collect(Collectors.toList())
            );
        }
        return locations.stream().distinct().collect(Collectors.toList());
    }

    public Location getLocation(String placeId) {
        PlaceApiResponse apiResponse = webClient
                .get()
                .uri("/details/json?key={key}&language=ko&place_id={placeId}", googleApiKey, placeId)
                .retrieve()
                .bodyToMono(PlaceApiResponse.class)
                .block();
        return Location.of(apiResponse.getResult());
    }

}
