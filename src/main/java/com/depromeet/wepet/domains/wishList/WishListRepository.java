package com.depromeet.wepet.domains.wishList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    List<WishList> findByDeviceId(String deviceId);

    WishList findByDeviceIdAndPlaceId(String deviceId, String placeId);
}
