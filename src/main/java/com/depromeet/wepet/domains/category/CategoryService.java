package com.depromeet.wepet.domains.category;

import com.depromeet.wepet.domains.category.dto.CategoryDto;
import com.depromeet.wepet.domains.common.constans.ErrorCode;
import com.depromeet.wepet.domains.common.exceptions.WepetException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Collection<Category> getCategories() {
        Collection<Category> categories = categoryRepository.findAll();
        categories.add(Category.getWishListCategory());
        return categories;
    }

    public Category getCategory(long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(() -> new WepetException(ErrorCode.NOT_EXISTS, HttpStatus.BAD_REQUEST, "category not found categoryId : " + categoryId));
    }

    public Category insertCategory(CategoryDto categoryDto) {
        Category category = CategoryDto.toEntity(categoryDto);
        return categoryRepository.save(category);
    }

    public Category updateCategory(long categoryId, CategoryDto categoryDto) {
        Category category = CategoryDto.toEntity(categoryDto);
        if (category.getCategoryId() != categoryId) {
            throw new WepetException(ErrorCode.INVALID_INPUT_VALUE, HttpStatus.BAD_REQUEST, "categoryId not match");
        }
        Category originCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new WepetException(ErrorCode.NOT_EXISTS, HttpStatus.BAD_REQUEST, "category not found categoryId: " + category));
        originCategory.setDisplayName(category.getDisplayName());
        originCategory.setSearchKeyword(category.getSearchKeyword());
        return categoryRepository.save(originCategory);
    }

    public void deleteCategory(long categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new WepetException(ErrorCode.NOT_EXISTS, HttpStatus.BAD_REQUEST, "category not found categoryId: " + categoryId));
        category.delete();
        categoryRepository.save(category);
    }

    public List<Category> getCategories(List<Long> categoryIds) {
        List<Category> categories = categoryRepository.findAllById(categoryIds);
        if (categoryIds.contains(new Long(-1))) {
            categories.add(Category.getWishListCategory());
        }
        return categories;
    }
}
