package com.depromeet.wepet.supports;

import com.depromeet.wepet.config.GoogleConfig;
import com.depromeet.wepet.domains.category.Category;
import com.depromeet.wepet.domains.category.CategoryController;
import com.depromeet.wepet.domains.category.CategoryService;
import com.depromeet.wepet.domains.category.dto.CategoryDto;
import com.depromeet.wepet.domains.common.constans.DtoType;
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

import java.util.Arrays;
import java.util.Collection;

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
@WebMvcTest(CategoryController.class)
@AutoConfigureRestDocs
@MockBean(JpaMetamodelMappingContext.class)
public class CategoryDocumentTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoryService categoryService;
    @MockBean
    private GoogleConfig googleConfig;

    @Test
    public void getCategories() throws Exception {
        Collection<Category> mockCategories = Arrays.asList(
                Category
                        .builder()
                        .categoryId(1L)
                        .displayName("병원")
                        .searchKeyword("hospital")
                        .build()
        );

        given(categoryService.getCategories()).willReturn(mockCategories);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("GET_categories",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        responseFields(
                                fieldWithPath("message").description("return_message"),
                                fieldWithPath("data[].categoryId").description("category PK"),
                                fieldWithPath("data[].displayName").description("display text"),
                                fieldWithPath("data[].searchKeyword").description("search keyword for map api")
                        )
                ));
    }

    @Test
    public void getCategory() throws Exception {
        Category mockCategory = Category
                .builder()
                .categoryId(1L)
                .displayName("병원")
                .searchKeyword("hospital")
                .build();

        given(categoryService.getCategory(1L)).willReturn(mockCategory);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.get("/api/category/{categoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("GET_category",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("categoryId").description("category PK")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message"),
                                fieldWithPath("data.categoryId").description("category PK"),
                                fieldWithPath("data.displayName").description("display text"),
                                fieldWithPath("data.searchKeyword").description("search keyword for map api")
                        )
                ));
    }

    @Test
    public void postCategory() throws Exception {
        CategoryDto categoryDto = CategoryDto
                .builder()
                .dtoType(DtoType.INSERT)
                .displayName("병원")
                .searchKeyword("hospital")
                .build();

        Category category = Category
                .builder()
                .categoryId(1L)
                .displayName("병원")
                .searchKeyword("hospital")
                .build();

        given(categoryService.insertCategory(categoryDto)).willReturn(category);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.post("/api/category")
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isCreated())
                .andDo(document("POST_category",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestFields(
                                fieldWithPath("categoryId").description("categoryId not needed in post"),
                                fieldWithPath("dtoType").description("fixed INSERT"),
                                fieldWithPath("displayName").description("display test"),
                                fieldWithPath("searchKeyword").description("search Keyword for map api")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message")
                        )
                ));
    }

    @Test
    public void putCategory() throws Exception {
        CategoryDto categoryDto = CategoryDto
                .builder()
                .categoryId(1L)
                .dtoType(DtoType.UPDATE)
                .displayName("공원")
                .searchKeyword("park")
                .build();

        Category category = Category
                .builder()
                .categoryId(1L)
                .displayName("공원")
                .searchKeyword("park")
                .build();

        given(categoryService.updateCategory(1L, categoryDto)).willReturn(category);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.put("/api/category/{categoryId}", 1L)
                        .content(objectMapper.writeValueAsString(categoryDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("PUT_category",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("categoryId").description("category PK")
                        ),
                        requestFields(
                                fieldWithPath("categoryId").description("categoryId needed in put"),
                                fieldWithPath("dtoType").description("fixed UPDATE"),
                                fieldWithPath("displayName").description("display test"),
                                fieldWithPath("searchKeyword").description("search Keyword for map api")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message")
                        )
                ));
    }

    @Test
    public void deleteCategory() throws Exception {
        doNothing().when(categoryService).deleteCategory(1L);

        ResultActions result = this.mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/api/category/{categoryId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
                .andDo(document("DELETE_category",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("categoryId").description("category PK")
                        ),
                        responseFields(
                                fieldWithPath("message").description("return_message")
                        )
                ));
    }
}
