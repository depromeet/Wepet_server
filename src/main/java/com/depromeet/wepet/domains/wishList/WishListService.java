package com.depromeet.wepet.domains.wishList;

import com.depromeet.wepet.domains.common.constans.ErrorCode;
import com.depromeet.wepet.domains.common.exceptions.WepetException;
import com.depromeet.wepet.domains.common.respose.DefaultPage;
import com.depromeet.wepet.domains.place.PlaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListService {

    private WishListRepository wishListRepository;
    private PlaceService placeService;

    public WishListService(WishListRepository wishListRepository,
                           PlaceService placeService) {
        this.wishListRepository = wishListRepository;
        this.placeService = placeService;
    }

    public void save(WishList wishList) {
        WishList originWishList = wishListRepository.findByDeviceIdAndPlaceId(wishList.getDeviceId(), wishList.getPlaceId());
        if (originWishList != null) {
            throw new WepetException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, "wishList exists");
        }
        wishListRepository.save(wishList);
        placeService.savePlace(wishList.getPlaceId());
    }

    public List<WishList> selectByDeviceId(String deviceId) {
        return wishListRepository.findByDeviceId(deviceId);
    }

    public void delete(String deviceId, String placeId) {
        WishList wishList = wishListRepository.findByDeviceIdAndPlaceId(deviceId, placeId);
        if (wishList == null) {
            new WepetException(ErrorCode.NOT_EXISTS, HttpStatus.BAD_REQUEST, "wishList not found wishListId: " + placeId);
        }
        wishList.delete();
        wishListRepository.save(wishList);
    }

}
