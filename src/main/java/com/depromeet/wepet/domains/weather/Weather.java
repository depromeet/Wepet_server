package com.depromeet.wepet.domains.weather;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Weather {

    private WeatherCode weatherCode;

    private String temperature;

    public static Weather of(WeatherApiDto weatherApiDto) {
        weatherApiDto.getMain().convert();
        return Weather
                .builder()
                .temperature(String.valueOf(weatherApiDto.getMain().temp))
                .weatherCode(WeatherCode.getWeatherCode(weatherApiDto.getWeather().get(0).getId()))
                .build();
    }
}
