package com.depromeet.wepet.domains.location;

import com.depromeet.wepet.domains.location.google.PlaceApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
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

    private static String url = "https://maps.googleapis.com/maps/api/place";

    public static Location of(PlaceApiResponse.Result placeApiResponse) {
        String photoUrl = !CollectionUtils.isEmpty(placeApiResponse.getPhotos()) ? url + "/photo?maxwidth=?&photoreference=" + placeApiResponse.getPhotos().get(0).getPhotoReference() : null;
        return Location
                .builder()
                .name(placeApiResponse.getName())
                .photoUrl(photoUrl)
                .latitude(placeApiResponse.getGeometry().getLocation().getLat())
                .longitude(placeApiResponse.getGeometry().getLocation().getLng())
                .placeId(placeApiResponse.getPlaceId())
                .homePage(placeApiResponse.getWebsite())
                .address(placeApiResponse.getFormattedAddress())
                .phoneNumber(placeApiResponse.getFormatterdPhoneNumber())
                .build();
    }
}
