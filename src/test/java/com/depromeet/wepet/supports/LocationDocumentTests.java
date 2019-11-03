package com.depromeet.wepet.supports;

import com.depromeet.wepet.config.GoogleConfig;
import com.depromeet.wepet.domains.common.respose.DefaultPage;
import com.depromeet.wepet.domains.location.Location;
import com.depromeet.wepet.domains.location.LocationController;
import com.depromeet.wepet.domains.location.LocationService;
import com.depromeet.wepet.domains.weather.WeatherController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.springframework.restdocs.snippet.Attributes.key;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.depromeet.wepet.supports.ApiDocumentUtils.getDocumentRequest;
import static com.depromeet.wepet.supports.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LocationController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class LocationDocumentTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationService locationService;

    @MockBean
    private GoogleConfig googleConfig;

    @Test
    public void getLocations() throws Exception {
        double latitude = 37.547069;
        double longitude = 129.950055;
        long distance = 3000;
        long categoryId = 200;

        Pageable pageable = new PageRequest(1, 10);

        Location location = Location
                .builder()
                .name("JW 메리어트 서울")
                .photoUrl("https://maps.googleapis.com/maps/api/place/photo?maxwidth=?&photoreference=CmRaAAAAVJIgqzHsp9cy607WV0L8UvWQX-CaofK42cbW5xx29KdABJB4SUNvKq6W06oQg_nCL0ci3yuM0r2nEYceXfQievkxCAr7NxIvLOaSU5NoPdMzqbpdAh_V8PAHuYGytDIvEhBmWcR4ofNbKYkdltuwtEMTGhTafMPiZPrWtlHsxe0ENIe7mV-jWQ")
                .placeId("ChIJL1X9rHuhfDUR8egAvHo1-7A")
                .latitude(37.5036507)
                .longitude(127.004724)
                .build();

        List<Location> locationList = Arrays.asList(location);

        given(locationService.getLocations(latitude, longitude, distance, categoryId, pageable)).willReturn(new DefaultPage(locationList, pageable, 1));
//        doReturn(new DefaultPage(locationList, any(Pageable.class), locationList.size()))
//                .when(locationService)
//                .getLocations(latitude, longitude, distance, categoryId , any(Pageable.class));

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/location/{latitude}/{longitude}?distance={distance}&categoryId={categoryId}&page=1&size=10", latitude, longitude, distance, categoryId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("GET_locations",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("latitude").description("latitude"),
                                parameterWithName("longitude").description("longitude")
                        ),
                        requestParameters(
                                parameterWithName("distance").description("search distance").attributes(key("defaultValue").value("3000")).optional(),
                                parameterWithName("categoryId").description("search categoryId if categoryId is Empty, all Category search").attributes(key("defaultValue").value("0")).optional(),
                                parameterWithName("size").description("page size"),
                                parameterWithName("page").description("page Count")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message"),
                                fieldWithPath("data.pageable.sort.sorted").description("not used"),
                                fieldWithPath("data.pageable.sort.unsorted").description("not used"),
                                fieldWithPath("data.pageable.sort.empty").description("not used"),
                                fieldWithPath("data.pageable.offset").description("not used"),
                                fieldWithPath("data.pageable.pageNumber").description("not used"),
                                fieldWithPath("data.pageable.pageSize").description("not used"),
                                fieldWithPath("data.pageable.paged").description("not used"),
                                fieldWithPath("data.pageable.unpaged").description("not used"),
                                fieldWithPath("data.content[].name").description("place name"),
                                fieldWithPath("data.content[].photoUrl").description("place photo url required maxWith And API Key"),
                                fieldWithPath("data.content[].placeId").description("google placeId"),
                                fieldWithPath("data.content[].latitude").description("latitude"),
                                fieldWithPath("data.content[].longitude").description("longitude"),
                                fieldWithPath("data.content[].distance").description("distance from current location"),
                                fieldWithPath("data.size").description("page size"),
                                fieldWithPath("data.number").description("page number"),
                                fieldWithPath("data.first").description("is first"),
                                fieldWithPath("data.numberOfElements").description("number of elements"),
                                fieldWithPath("data.sort.sorted").description("is sorted"),
                                fieldWithPath("data.sort.unsorted").description("is unsorted"),
                                fieldWithPath("data.sort.empty").description("is empty"),
                                fieldWithPath("data.last").description("is last elements"),
                                fieldWithPath("data.totalElements").description("total elements"),
                                fieldWithPath("data.totalPages").description("total pages"),
                                fieldWithPath("data.empty").description("is empty")
                        )
                ));

    }


    @Test
    public void getLocation() throws Exception {
        String placeId = "ChIJL1X9rHuhfDUR8egAvHo1-7A";
        double latitude = 37.5036507;
        double longitude = 127.004724;

        Location location = Location
                .builder()
                .name("JW 메리어트 서울")
                .photoUrl("https://maps.googleapis.com/maps/api/place/photo?maxwidth=?&photoreference=CmRaAAAAVJIgqzHsp9cy607WV0L8UvWQX-CaofK42cbW5xx29KdABJB4SUNvKq6W06oQg_nCL0ci3yuM0r2nEYceXfQievkxCAr7NxIvLOaSU5NoPdMzqbpdAh_V8PAHuYGytDIvEhBmWcR4ofNbKYkdltuwtEMTGhTafMPiZPrWtlHsxe0ENIe7mV-jWQ")
                .placeId("ChIJL1X9rHuhfDUR8egAvHo1-7A")
                .latitude(latitude)
                .longitude(longitude)
                .homePage("https://www.marriott.com/hotels/travel/seljw-jw-marriott-hotel-seoul/?scid=bb1a189a-fec3-4d19-a255-54ba596febe2")
                .address("대한민국 서울특별시 서초구 반포동 신반포로 176")
                .phoneNumber("02-6282-6262")
                .build();


        given(locationService.getLocation(latitude, longitude, placeId)).willReturn(location);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/location/{latitude}/{longitude}/{placeId}", latitude, longitude, placeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("GET_location",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("latitude").description("latitude"),
                                parameterWithName("longitude").description("longitude"),
                                parameterWithName("placeId").description("google placeId")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message"),
                                fieldWithPath("data.name").description("place name"),
                                fieldWithPath("data.photoUrl").description("place photo url required maxWith And API Key"),
                                fieldWithPath("data.placeId").description("google placeId"),
                                fieldWithPath("data.latitude").description("latitude"),
                                fieldWithPath("data.longitude").description("longitude"),
                                fieldWithPath("data.homePage").description("place homepageUrl"),
                                fieldWithPath("data.address").description("place address"),
                                fieldWithPath("data.phoneNumber").description("place phoneNumber"),
                                fieldWithPath("data.distance").description("distance from current location")
                        )
                ));

    }
}
