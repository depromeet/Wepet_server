package com.depromeet.wepet.supports;

import com.depromeet.wepet.domains.location.Location;
import com.depromeet.wepet.domains.location.LocationController;
import com.depromeet.wepet.domains.location.LocationService;
import com.depromeet.wepet.domains.weather.WeatherController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
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

    @Test
    public void getLocations() throws Exception {
        double latitude = 37.547069;
        double longitude = 129.950055;
        long distance = 3000;
        long categoryId = 200;


        Location location = Location
                .builder()
                .name("JW 메리어트 서울")
                .photoUrl("https://maps.googleapis.com/maps/api/place/photo?maxwidth=?&photoreference=CmRaAAAAVJIgqzHsp9cy607WV0L8UvWQX-CaofK42cbW5xx29KdABJB4SUNvKq6W06oQg_nCL0ci3yuM0r2nEYceXfQievkxCAr7NxIvLOaSU5NoPdMzqbpdAh_V8PAHuYGytDIvEhBmWcR4ofNbKYkdltuwtEMTGhTafMPiZPrWtlHsxe0ENIe7mV-jWQ")
                .placeId("ChIJL1X9rHuhfDUR8egAvHo1-7A")
                .latitude(37.5036507)
                .longitude(127.004724)
                .build();

        List<Location> locationList = Arrays.asList(location);

        given(locationService.getLocations(latitude, longitude, distance, categoryId)).willReturn(locationList);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/location/{latitude}/{longitude}?distance={distance}&categoryId={categoryId}", latitude, longitude, distance, categoryId)
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
                                parameterWithName("categoryId").description("search categoryId if categoryId is Empty, all Category search").attributes(key("defaultValue").value("0")).optional()
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message"),
                                fieldWithPath("data[].name").description("place name"),
                                fieldWithPath("data[].photoUrl").description("place photo url required maxWith And API Key"),
                                fieldWithPath("data[].placeId").description("google placeId"),
                                fieldWithPath("data[].latitude").description("latitude"),
                                fieldWithPath("data[].longitude").description("longitude")
                        )
                ));

    }


    @Test
    public void getLocation() throws Exception {
        String placeId = "ChIJL1X9rHuhfDUR8egAvHo1-7A";

        Location location = Location
                .builder()
                .name("JW 메리어트 서울")
                .photoUrl("https://maps.googleapis.com/maps/api/place/photo?maxwidth=?&photoreference=CmRaAAAAVJIgqzHsp9cy607WV0L8UvWQX-CaofK42cbW5xx29KdABJB4SUNvKq6W06oQg_nCL0ci3yuM0r2nEYceXfQievkxCAr7NxIvLOaSU5NoPdMzqbpdAh_V8PAHuYGytDIvEhBmWcR4ofNbKYkdltuwtEMTGhTafMPiZPrWtlHsxe0ENIe7mV-jWQ")
                .placeId("ChIJL1X9rHuhfDUR8egAvHo1-7A")
                .latitude(37.5036507)
                .longitude(127.004724)
                .homePage("https://www.marriott.com/hotels/travel/seljw-jw-marriott-hotel-seoul/?scid=bb1a189a-fec3-4d19-a255-54ba596febe2")
                .address("대한민국 서울특별시 서초구 반포동 신반포로 176")
                .phoneNumber("02-6282-6262")
                .build();


        given(locationService.getLocation(placeId)).willReturn(location);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/location/{placeId}", placeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("GET_location",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
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
                                fieldWithPath("data.phoneNumber").description("place phoneNumber")
                        )
                ));

    }
}
