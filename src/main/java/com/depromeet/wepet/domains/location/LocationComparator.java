package com.depromeet.wepet.domains.location;

import java.util.Comparator;

public class LocationComparator implements Comparator<Location> {

    @Override
    public int compare(Location l1, Location l2) {
        if (l1.getDistance() > l2.getDistance()) {
            return 1;
        } else {
            return -1;
        }
    }
}
