package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.domains.category.Category;
import com.depromeet.wepet.domains.category.CategoryService;
import com.depromeet.wepet.domains.common.respose.DefaultPage;
import com.depromeet.wepet.domains.location.google.PlaceApiResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private WebClient webClient;
    private String googleApiKey;
    private List<String> fileds;
    private CategoryService categoryService;
    private LocationRepository locationRepository;
    private LocationComparator locationComparator;
    public LocationService(@Value("${google.api.url}") String url,
                            @Value("${google.api.key}") String googleApiKey,
                            @Value("${google.api.fields}") List<String> fileds,
                           CategoryService categoryService,
                           LocationRepository locationRepository) {
        this.webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
        this.googleApiKey = googleApiKey;
        this.categoryService = categoryService;
        this.fileds = fileds;
        this.locationRepository = locationRepository;
        this.locationComparator = new LocationComparator();
    }

    public DefaultPage<List<Location>> getLocations(double latitude, double longitude, long distance, Long categoryId, Pageable pageable) {
        Category category = categoryService.getCategory(categoryId);

        Optional<List<PlaceApiResponse.Result>> results = locationRepository.getLocations(latitude, longitude, distance, category.getSearchKeyword());
        if (!results.isPresent()) {
            return DefaultPage.empty();
        }

        List<PlaceApiResponse.Result> list = results.get();
        List<Location> locationList = list
                .stream()
                .map(res -> Location.of(res, latitude, longitude, googleApiKey))
                .collect(Collectors.toList());
        locationList.sort(locationComparator);
        List<Location> subLocationList = locationList.subList((pageable.getPageNumber() - 1) * pageable.getPageSize(), pageable.getPageNumber() * pageable.getPageSize());

        return new DefaultPage(subLocationList, pageable, locationList.size());
    }

    public Location getLocation(double latitude, double longitude, String placeId) {
        Optional<PlaceApiResponse.Result> results = locationRepository.getLocation(placeId);
        if (!results.isPresent()) {
            return null;
        }
        return Location.of(results.get(), latitude, longitude, googleApiKey);
    }

}
