package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.config.GoogleConfig;
import com.depromeet.wepet.domains.location.google.PlaceApiResponse;
import com.depromeet.wepet.domains.place.PlaceRepository;
import com.depromeet.wepet.domains.place.PlaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class LocationRepository {

    private WebClient webClient;
    private GoogleConfig googleConfig;
    private LocationComparator locationComparator;

    public LocationRepository(GoogleConfig googleConfig,
                              PlaceService placeService) {
        this.googleConfig = googleConfig;
        this.webClient = WebClient
                .builder()
                .baseUrl(googleConfig.getUrl())
                .build();
        this.locationComparator = new LocationComparator();
    }

    public Optional<List<PlaceApiResponse.Result>> getLocations(double latitude, double longitude, long distance, String searchKeyword) {
        try {
            PlaceApiResponse apiResponse = webClient
                    .get()
                    .uri("/nearbysearch/json?location={latitude},{longitude}&radius={distance}&type={searchKeyword}&key={googleApiKey}&language=ko", latitude, longitude, distance, searchKeyword, googleConfig.getKey())
                    .retrieve()
                    .bodyToMono(PlaceApiResponse.class)
                    .block();
            return Optional.of(apiResponse.getResults());
        } catch (Exception e) {
            log.warn(" {} ", e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<PlaceApiResponse.Result> getLocation(String placeId) {
        try {
            PlaceApiResponse apiResponse = webClient
                    .get()
                    .uri("/details/json?key={key}&language=ko&place_id={placeId}", googleConfig.getKey(), placeId)
                    .retrieve()
                    .bodyToMono(PlaceApiResponse.class)
                    .block();
            return Optional.of(apiResponse.getResult());
        } catch (Exception e) {
            log.warn(" {} ", e.getMessage());
        }

        return Optional.empty();
    }
}
