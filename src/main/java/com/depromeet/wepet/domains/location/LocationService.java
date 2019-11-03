package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.domains.category.Category;
import com.depromeet.wepet.domains.category.CategoryService;
import com.depromeet.wepet.domains.common.respose.DefaultPage;
import com.depromeet.wepet.domains.location.google.PlaceApiResponse;
import com.depromeet.wepet.domains.place.Place;
import com.depromeet.wepet.domains.place.PlaceRepository;
import com.depromeet.wepet.domains.place.PlaceService;
import com.depromeet.wepet.domains.wishList.WishList;
import com.depromeet.wepet.domains.wishList.WishListService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
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
    private PlaceService placeService;
    private WishListService wishListService;

    public LocationService(@Value("${google.api.url}") String url,
                            @Value("${google.api.key}") String googleApiKey,
                            @Value("${google.api.fields}") List<String> fileds,
                           CategoryService categoryService,
                           LocationRepository locationRepository,
                           PlaceService placeService,
                           WishListService wishListService) {
        this.webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
        this.googleApiKey = googleApiKey;
        this.categoryService = categoryService;
        this.fileds = fileds;
        this.locationRepository = locationRepository;
        this.locationComparator = new LocationComparator();
        this.placeService = placeService;
        this.wishListService = wishListService;
    }

    public DefaultPage<?> getLocations(double latitude, double longitude, long distance, Long categoryId, Pageable pageable, String deviceId) {
        if (categoryId.compareTo(new Long(-1)) == 0) {
            List<WishList> wishLists = wishListService.selectByDeviceId(deviceId);
            List<String> placeIds = wishLists.stream().map(wishList -> wishList.getPlaceId()).collect(Collectors.toList());
            List<Place> places = placeService.getPlaces(placeIds);
            if (CollectionUtils.isEmpty(places)) {
                return DefaultPage.empty();
            }

            List<Location> locationList = places.stream().map(place -> Location.of(place, latitude, longitude)).collect(Collectors.toList());
            return DefaultPage.of(locationList, pageable);
        }

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
        placeService.savePlace(locationList);
        return DefaultPage.of(locationList, pageable);
    }

    public Location getLocation(double latitude, double longitude, String placeId) {
        Optional<PlaceApiResponse.Result> results = locationRepository.getLocation(placeId);
        if (!results.isPresent()) {
            return null;
        }
        Location location = Location.of(results.get(), latitude, longitude, googleApiKey);
        placeService.savePlace(Arrays.asList(location));
        return location;
    }

}
