package com.depromeet.wepet.domains.category.dto;

import com.depromeet.wepet.domains.category.Category;
import com.depromeet.wepet.domains.common.constans.DtoType;
import lombok.Builder;
import lombok.Getter;

@Getter
@ValidCategory
@Builder
public class CategoryDto {

    private long categoryId;

    private DtoType dtoType;

    private String displayName;

    private String searchKeyword;

    public static Category toEntity(CategoryDto categoryDto) {
        return Category
                .builder()
                .categoryId(getId(categoryDto))
                .displayName(categoryDto.getDisplayName())
                .searchKeyword(categoryDto.getSearchKeyword())
                .build();
    }

    public static long getId(CategoryDto categoryDto) {
        return categoryDto.getDtoType() == DtoType.INSERT ? 0 : categoryDto.getCategoryId();
    }
}
