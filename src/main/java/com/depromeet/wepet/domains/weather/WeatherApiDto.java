package com.depromeet.wepet.domains.weather;

import lombok.Data;

import java.util.List;

@Data
public class WeatherApiDto {

    private Coord coord;
    private List<Weather> weather;
    private String base;
    private Clouds clouds;
    private Main main;
    private long visibility;
    private Wind wind;
    private long dt;
    private Sys sys;
    private long timezone;
    private long id;
    private String name;
    private Integer cod;

    @Data
    static class Coord {
        long lon;
        long lat;
    }

    // List
    @Data
    static class Weather {
        Integer id;
        String main;
        String description;
        String icon;
    }

    @Data
    static class Main {
        long temp;
        long pressure;
        long humidity;
        long temp_min;
        long temp_max;

        void convert() {
           temp = temp - Double.valueOf(273.15).longValue();
        }
    }

    @Data
    static class Wind {
        long speed;
        long deg;
        long gust;
    }

    @Data
    static class Clouds {
        long all;
    }

    @Data
    static class Sys {
        long type;
        long id;
        String country;
        long sunrise;
        long sunset;
    }
}
