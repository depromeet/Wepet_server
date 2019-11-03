package com.depromeet.wepet.supports;

import com.depromeet.wepet.config.GoogleConfig;
import com.depromeet.wepet.domains.category.CategoryController;
import com.depromeet.wepet.domains.weather.Weather;
import com.depromeet.wepet.domains.weather.WeatherCode;
import com.depromeet.wepet.domains.weather.WeatherController;
import com.depromeet.wepet.domains.weather.WeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static com.depromeet.wepet.supports.ApiDocumentUtils.getDocumentRequest;
import static com.depromeet.wepet.supports.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WeatherController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class WeatherDocumentTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;
    @MockBean
    private GoogleConfig googleConfig;

    @Test
    public void getWeather() throws Exception {
        double latitude = 37.547069;
        double longitude = 129.950055;

        Weather weather = Weather
                .builder()
                .weatherCode(WeatherCode.CLEAR)
                .temperature("23")
                .build();

        given(weatherService.getWeather(latitude, longitude)).willReturn(weather);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/weather/{latitude}/{longitude}", latitude, longitude)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("GET_weather",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("latitude").description("latitude"),
                                parameterWithName("longitude").description("longitude")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message"),
                                fieldWithPath("data.weatherCode").description("category PK"),
                                fieldWithPath("data.temperature").description("current temperature")
                        )
                ));
    }
}
