package com.depromeet.wepet.domains.weather;

import com.depromeet.wepet.domains.common.constans.ErrorCode;
import com.depromeet.wepet.domains.common.exceptions.WepetException;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;

public enum WeatherCode {

    // docs https://openweathermap.org/weather-conditions
    THUNDERSTORM(Arrays.asList(200, 201, 202, 210, 211, 212, 221, 230, 232)),
    Drizzle(Arrays.asList(300, 301, 302, 310, 311, 312, 313, 314, 32)),
    RAIN(Arrays.asList(500, 501, 502, 503, 504, 511, 520, 521, 522, 531)),
    SNOW(Arrays.asList(600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622)),
    ATMOSPHERE(Arrays.asList(701, 711, 721, 731, 741, 751, 761, 762, 771, 781)),
    CLEAR(Arrays.asList(800)),
    CLOUDS(Arrays.asList(801, 802, 803, 804)),
    MIST(Arrays.asList(701)),
    SMOKE(Arrays.asList(711)),
    HAZE(Arrays.asList(721)),
    DUST(Arrays.asList(731, 761)),
    FOG(Arrays.asList(741)),
    SAND(Arrays.asList(751)),
    ASH(Arrays.asList(762)),
    SQUALL(Arrays.asList(771)),
    TORNADO(Arrays.asList(781));

    private List<Integer> codes;

    WeatherCode(List<Integer> codes) {
        this.codes = codes;
    }

    public static WeatherCode getWeatherCode(Integer code) {
        return Arrays.stream(WeatherCode.values())
                .filter(weatherCode -> weatherCode.codes.contains(code)).findFirst().orElseThrow(() -> new WepetException(ErrorCode.NOT_EXISTS, HttpStatus.INTERNAL_SERVER_ERROR, "not found weather code"));
    }
}
