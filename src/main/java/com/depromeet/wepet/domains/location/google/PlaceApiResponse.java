package com.depromeet.wepet.domains.location.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
public class PlaceApiResponse implements Serializable {


    @JsonProperty("html_attributions")
    private List<String> htmlAttributions;
    @JsonProperty("next_page_token")
    private String nextPageToken;
    private List<Result> results;
    private Result result;
    private String status;

    @Data
    public static class Result {

        @JsonProperty("address_components")
        private List<AddressComponent> addressComponents;
        @JsonProperty("adr_address")
        private String adrAddress;
        @JsonProperty("formatted_address")
        private String formattedAddress;
        @JsonProperty("formatted_phone_number")
        private String formatterdPhoneNumber;
        private Geometry geometry;
        private String icon;
        private String id;
        private String name;
        @JsonProperty("international_phone_number")
        private String internationalPhoneNumber;
        @JsonProperty(value = "photos")
        private List<Photo> photos;
        @JsonProperty(value = "opening_hours")
        private OpeningHours openingHours;
        @JsonProperty(value = "place_id")
        private String placeId;
        private String reference;
        private List<String> types;
        private String vicinity;
        @JsonProperty("plus_code")
        private Map<String, Object> plusCode;
        private double rating;
        @JsonProperty("user_ratings_total")
        private long userRatingsTotal;
        private String scope;
        @JsonProperty("price_level")
        private long priceLevel;
        private List<Object> reviews;
        private String url;
        @JsonProperty("utc_offset")
        private long utcOffset;
        private String website;

        @Data
        public static class Geometry {
            private Location location;
            private Map<String, Object> viewport;

            @Data
            public static class Location {
                private double lat;
                private double lng;
            }
        }

        @Data
        public static class Photo {
            @JsonProperty("height")
            private long height;
            @JsonProperty("width")
            private long width;
            @JsonProperty("html_attributions")
            private List<String> htmlAttributions;
            @JsonProperty("photo_reference")
            private String photoReference;
        }

        @Data
        public static class AddressComponent {
            @JsonProperty("long_name")
            private String longName;
            @JsonProperty("short_name")
            private String shortName;
            private List<String> types;
        }

        @Data
        public static class OpeningHours {
            @JsonProperty("open_now")
            private boolean openNow;
        }
    }

}
