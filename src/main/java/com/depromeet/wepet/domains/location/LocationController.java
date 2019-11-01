package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.domains.common.respose.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    private LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/{latitude}/{longitude}")
    public ResponseEntity<?> getLocations(@PathVariable("latitude") double latitude,
                                          @PathVariable("longitude") double longitude,
                                          @RequestParam(value = "categoryId", defaultValue = "0") String categoryId,
                                          @RequestParam(value = "distance", defaultValue = "3000") long distance) {
        Collection<Location> locations = locationService.getLocations(latitude, longitude, distance, Long.parseLong(categoryId));
        return ResponseEntity.ok(Response.of(locations));
    }

    @GetMapping("{placeId}")
    public ResponseEntity<?> getLocation(@PathVariable("placeId") String placeId) {
        Location location = locationService.getLocation(placeId);
        return ResponseEntity.ok(Response.of(location));
    }
}
