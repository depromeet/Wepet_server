package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.domains.common.respose.DefaultPage;
import com.depromeet.wepet.domains.common.respose.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

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
                                          @RequestParam(value = "distance", defaultValue = "3000") long distance,
                                          @RequestParam(value = "deviceId", required = false) String deviceId,
                                          Pageable pageable) {
        DefaultPage<?> locations = locationService.getLocations(latitude, longitude, distance, Long.parseLong(categoryId), pageable, deviceId);
        return ResponseEntity.ok(Response.of(locations));
    }

    @GetMapping("/{latitude}/{longitude}/{placeId}")
    public ResponseEntity<?> getLocation(
            @PathVariable("latitude") double latitude,
            @PathVariable("longitude") double longitude,
            @PathVariable("placeId") String placeId) {
        Location location = locationService.getLocation(latitude, longitude, placeId);
        return ResponseEntity.ok(Response.of(location));
    }
}
