package com.depromeet.wepet.domains.place;

import com.depromeet.wepet.domains.location.Location;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlaceService {

    private PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    @Async
    public void savePlace(List<Location> locations) {
        List<Place> places = locations.stream().map(Place::of).collect(Collectors.toList());
        placeRepository.saveAll(places);
    }

    public List<Place> getPlaces(List<String> placeIds) {
        return placeRepository.findByPlaceIdIn(placeIds);
    }
}
