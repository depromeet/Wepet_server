package com.depromeet.wepet.domains.place;

import com.depromeet.wepet.config.GoogleConfig;
import com.depromeet.wepet.domains.common.constans.ErrorCode;
import com.depromeet.wepet.domains.common.exceptions.WepetException;
import com.depromeet.wepet.domains.location.Location;
import com.depromeet.wepet.domains.location.LocationRepository;
import com.depromeet.wepet.domains.location.google.PlaceApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaceService {

    private PlaceRepository placeRepository;
    private LocationRepository locationRepository;
    private GoogleConfig googleConfig;

    public PlaceService(PlaceRepository placeRepository,
                        LocationRepository locationRepository,
                        GoogleConfig googleConfig) {
        this.placeRepository = placeRepository;
        this.locationRepository = locationRepository;
        this.googleConfig = googleConfig;
    }

    @Async
    public void savePlace(String placeId) {
        Optional<PlaceApiResponse.Result> apiResult = locationRepository.getLocation(placeId);
        if (!apiResult.isPresent()) {
            throw new WepetException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, "Place is not found");
        }
        Location location = Location.of(apiResult.get(), googleConfig.getKey());
        Place places = Place.of(location);
        placeRepository.save(places);
    }

    public List<Place> getPlaces(List<String> placeIds) {
        return placeRepository.findByPlaceIdIn(placeIds);
    }
}
