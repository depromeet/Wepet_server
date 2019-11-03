package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.config.GoogleConfig;
import com.depromeet.wepet.domains.location.google.PlaceApiResponse;
import com.depromeet.wepet.domains.place.Place;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Location {

    private String name;
    private String photoUrl;
    private String placeId;
    private double latitude;
    private double longitude;
    private String homePage;
    private String address;
    private String phoneNumber;

    private long distance;

    private static String url = "https://maps.googleapis.com/maps/api/place";

    public static Location of(PlaceApiResponse.Result placeApiResponse, double latitude, double longitude, String apiKey) {
        String photoUrl = !CollectionUtils.isEmpty(placeApiResponse.getPhotos()) ? url + "/photo?maxwidth=" + placeApiResponse.getPhotos().get(0).getWidth() + "&photoreference=" + placeApiResponse.getPhotos().get(0).getPhotoReference() : null;
        photoUrl += "&key=" + apiKey;
        return Location
                .builder()
                .name(placeApiResponse.getName())
                .photoUrl(photoUrl)
                .latitude(placeApiResponse.getGeometry().getLocation().getLat())
                .longitude(placeApiResponse.getGeometry().getLocation().getLng())
                .placeId(placeApiResponse.getPlaceId())
                .homePage(placeApiResponse.getWebsite())
                .address(placeApiResponse.getFormattedAddress() == null ? placeApiResponse.getVicinity() : placeApiResponse.getFormattedAddress())
                .phoneNumber(placeApiResponse.getFormatterdPhoneNumber())
                .distance(getDistance(latitude, longitude, placeApiResponse.getGeometry().getLocation().getLat(), placeApiResponse.getGeometry().getLocation().getLng(), DistanceType.METER))
                .build();
    }

    public static long getDistance(double lat1, double lon1, double lat2, double lon2, DistanceType distanceType) {

        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환
        return new Double(dist).longValue();
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    public static Location of(Place place, double latitude, double longitude) {
        return Location
                .builder()
                .name(place.getName())
                .photoUrl(place.getPhotoUrl())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .placeId(place.getPlaceId())
                .homePage(place.getHomePage())
                .address(place.getAddress())
                .phoneNumber(place.getPhoneNumber())
                .distance(getDistance(latitude, longitude, place.getLatitude(), place.getLongitude(), DistanceType.METER))
                .build();
    }
}
