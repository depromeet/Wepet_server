package com.depromeet.wepet.domains.wishList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class WishListServiceTest {

    @Autowired
    private WishListService wishListService;

    @Autowired
    private WishListRepository wishListRepository;

    @Before
    public void setup() {
        WishList wishList = WishList
                .builder()
                .deviceId("123123")
                .placeId("456456")
                .build();
        wishListRepository.save(wishList);
    }

    @Test
    public void 즐겨찾기_조회() {
        Page<List<WishList>> wishList = wishListService.selectByDeviceId("123123",new PageRequest(1, 10));
        Assert.assertNotNull(wishList);

    }
}
