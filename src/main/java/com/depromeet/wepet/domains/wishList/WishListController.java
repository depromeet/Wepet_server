package com.depromeet.wepet.domains.wishList;

import com.depromeet.wepet.domains.common.respose.DefaultPage;
import com.depromeet.wepet.domains.common.respose.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishList")
public class WishListController {

    private WishListService wishListService;

    public WishListController(WishListService wishListService) {
        this.wishListService = wishListService;
    }

    @PostMapping
    public ResponseEntity<?> createWishList(@RequestBody WishList wishList) {
        wishListService.save(wishList);
        return new ResponseEntity(Response.of(null), HttpStatus.CREATED);
    }

    @DeleteMapping("/{deviceId}/{placeId}")
    public ResponseEntity<?> deleteWishList(@PathVariable("deviceId") String deviceId,
                                            @PathVariable("placeId") String placeId) {
        wishListService.delete(deviceId, placeId);
        return new ResponseEntity(Response.of(null), HttpStatus.OK);
    }
}
