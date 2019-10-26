package com.depromeet.wepet.domains.weather;

import com.depromeet.wepet.domains.common.respose.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/{latitude}/{longitude}")
    public ResponseEntity<?> getWeather(@PathVariable("latitude") double latitude,
                                        @PathVariable("longitude") double longitude) {
        Weather weather = weatherService.getWeather(latitude, longitude);
        return ResponseEntity.ok(Response.of(weather));
    }
}
