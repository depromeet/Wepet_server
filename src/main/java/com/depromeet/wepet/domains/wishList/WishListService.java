package com.depromeet.wepet.domains.wishList;

import com.depromeet.wepet.domains.common.constans.ErrorCode;
import com.depromeet.wepet.domains.common.exceptions.WepetException;
import com.depromeet.wepet.domains.common.respose.DefaultPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishListService {

    private WishListRepository wishListRepository;

    public WishListService(WishListRepository wishListRepository) {
        this.wishListRepository = wishListRepository;
    }

    public void save(WishList wishList) {
        WishList originWishList = wishListRepository.findByDeviceIdAndPlaceId(wishList.getDeviceId(), wishList.getPlaceId());
        if (originWishList != null) {
            throw new WepetException(ErrorCode.BAD_REQUEST, HttpStatus.BAD_REQUEST, "wishList exists");
        }
        wishListRepository.save(wishList);
    }

    public List<WishList> selectByDeviceId(String deviceId) {
        return wishListRepository.findByDeviceId(deviceId);
    }

    public void delete(long wishListId) {
        WishList wishList = wishListRepository.findById(wishListId)
                .orElseThrow(() -> new WepetException(ErrorCode.NOT_EXISTS, HttpStatus.BAD_REQUEST, "wishList not found wishListId: " + wishListId));;
        wishList.delete();
        wishListRepository.save(wishList);
    }

}
