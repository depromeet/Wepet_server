package com.depromeet.wepet.domains.weather;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
public class WeatherService {

    private WebClient webClient;
    private String apikey;

    public WeatherService(@Value("${weather.api.key}") String apiKey,
                          @Value("${weather.api.url}") String url) {
        this.webClient = WebClient
                .builder()
                .baseUrl(url)
                .build();
        this.apikey = apiKey;
    }

    public Weather getWeather(double latitude, double longitude) {
        WeatherApiDto weatherApiDto = webClient
                .get()
                .uri("?lat={latitude}&lon={longitude}&apiKey={apiKey}", latitude, longitude, apikey)
                .retrieve()
                .bodyToMono(WeatherApiDto.class).block();
        return Weather.of(weatherApiDto);
    }
}
