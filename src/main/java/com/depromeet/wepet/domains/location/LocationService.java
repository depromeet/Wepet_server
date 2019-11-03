package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.domains.category.Category;
import com.depromeet.wepet.domains.category.CategoryService;
import com.depromeet.wepet.domains.common.respose.DefaultPage;
import com.depromeet.wepet.domains.location.google.PlaceApiResponse;
import com.depromeet.wepet.domains.place.Place;
import com.depromeet.wepet.domains.place.PlaceService;
import com.depromeet.wepet.domains.wishList.WishList;
import com.depromeet.wepet.domains.wishList.WishListService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private String googleApiKey;
    private CategoryService categoryService;
    private LocationRepository locationRepository;
    private LocationComparator locationComparator;
    private PlaceService placeService;
    private WishListService wishListService;

    public LocationService(@Value("${google.api.url}") String url,
                            @Value("${google.api.key}") String googleApiKey,
                           CategoryService categoryService,
                           LocationRepository locationRepository,
                           PlaceService placeService,
                           WishListService wishListService) {
        this.googleApiKey = googleApiKey;
        this.categoryService = categoryService;
        this.locationRepository = locationRepository;
        this.locationComparator = new LocationComparator();
        this.placeService = placeService;
        this.wishListService = wishListService;
    }

    public DefaultPage<?> getLocations(double latitude, double longitude, long distance, Long categoryId, Pageable pageable, String deviceId) {
        List<WishList> wishLists = wishListService.selectByDeviceId(deviceId);
        if (categoryId.compareTo(new Long(-1)) == 0) {
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
                .map(res -> Location.of(res, latitude, longitude, googleApiKey, wishLists.contains(res.getPlaceId())))
                .collect(Collectors.toList());
        locationList.sort(locationComparator);
        return DefaultPage.of(locationList, pageable);
    }

    public Location getLocation(double latitude, double longitude, String placeId, String deviceId) {
        List<WishList> wishLists = wishListService.selectByDeviceId(deviceId);
        Optional<PlaceApiResponse.Result> results = locationRepository.getLocation(placeId);
        if (!results.isPresent()) {
            return null;
        }
        Location location = Location.of(results.get(), latitude, longitude, googleApiKey, wishLists.contains(results.get().getPlaceId()));
        return location;
    }

}
