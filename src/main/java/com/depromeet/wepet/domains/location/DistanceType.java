package com.depromeet.wepet.domains.location;

import lombok.Getter;

@Getter
public enum DistanceType {

    METER(1.609344 * 1000),
    KILO_METER(1.609344);

    private double unit;

    DistanceType(double unit) {
        this.unit = unit;
    }
}
