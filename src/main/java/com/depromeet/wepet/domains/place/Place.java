package com.depromeet.wepet.domains.place;

import com.depromeet.wepet.domains.location.Location;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "google_place")
@Entity
public class Place {

    @Id
    @Column(name = "placeId")
    private String placeId;

    @Column(name = "name")
    private String name;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "address")
    private String address;

    @Column(name = "homePage")
    private String homePage;

    @Column(name = "phone_number")
    private String phoneNumber;

    public static Place of(Location location) {
        return Place
                .builder()
                .address(location.getAddress())
                .latitude(location.getLatitude())
                .longitude(location.getLongitude())
                .name(location.getName())
                .photoUrl(location.getPhotoUrl())
                .placeId(location.getPlaceId())
                .homePage(location.getHomePage())
                .phoneNumber(location.getPhoneNumber())
                .build();
    }
}
