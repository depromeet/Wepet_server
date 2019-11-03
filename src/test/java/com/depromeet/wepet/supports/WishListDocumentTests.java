package com.depromeet.wepet.supports;

import com.depromeet.wepet.config.GoogleConfig;
import com.depromeet.wepet.domains.common.respose.DefaultPage;
import com.depromeet.wepet.domains.wishList.WishList;
import com.depromeet.wepet.domains.wishList.WishListController;
import com.depromeet.wepet.domains.wishList.WishListService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static com.depromeet.wepet.supports.ApiDocumentUtils.getDocumentRequest;
import static com.depromeet.wepet.supports.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WishListController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class WishListDocumentTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WishListService wishListService;
    @MockBean
    private GoogleConfig googleConfig;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void postWishList() throws Exception {
        String deviceId = "123awef";

        WishList wishList = WishList
                .builder()
                .placeId("123213")
                .deviceId(deviceId)
                .build();

        doNothing().when(wishListService).save(wishList);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/wishList")
                        .content(objectMapper.writeValueAsString(wishList))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isCreated())
                .andDo(document("POST_wishList",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("wishListId").description("wishList PK"),
                                fieldWithPath("placeId").description("google placeId"),
                                fieldWithPath("deviceId").description("user deviceId")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message")
                        )
                ));
    }


    @Test
    public void deleteCategory() throws Exception {
        String placeId = "sadgaesg";
        String deviceId = "123";

        doNothing().when(wishListService).delete(deviceId, placeId);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/wishList/{deviceId}/{placeId}", deviceId, placeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("DELETE_wishList",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("deviceId").description("user deviceId"),
                                parameterWithName("placeId").description("google placeId")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message")
                        )
                ));
    }
}
