package com.depromeet.wepet.domains.category;

import com.depromeet.wepet.domains.category.dto.CategoryDto;
import com.depromeet.wepet.domains.common.constans.DtoType;
import com.depromeet.wepet.domains.common.exceptions.WepetException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.util.Collection;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category originCategory;

    @Rule
    public ExpectedException wepetException = ExpectedException.none();

    @Before
    public void setup() {
        originCategory =
                Category
                        .builder()
                        .displayName("test")
                        .searchKeyword("test")
                        .build();

        originCategory = categoryRepository.save(originCategory);
    }

    @Test
    public void 카테고리_조회() {
        Category category = categoryService.getCategory(originCategory.getCategoryId());
        Assert.assertNotNull(category);
        Assert.assertTrue(category.getCategoryId() == originCategory.getCategoryId());
        Assert.assertTrue(category.getDisplayName().equals(originCategory.getDisplayName()));
        Assert.assertTrue(category.getSearchKeyword().equals(originCategory.getSearchKeyword()));

        wepetException.expect(WepetException.class);
        categoryService.getCategory(9999);
    }

    @Test
    public void 카테고리_ID_조회() {
        Collection<Category> categories = categoryService.getCategories();
        Assert.assertNotNull(categories);
        Assert.assertTrue(!CollectionUtils.isEmpty(categories));
    }

    @Test
    public void 카테고리_등록() {
        CategoryDto categoryDto = CategoryDto
                .builder()
                .displayName("insertCategory")
                .dtoType(DtoType.INSERT)
                .searchKeyword("insertCategory")
                .build();
        Category insertCategory = categoryService.insertCategory(categoryDto);
        Assert.assertNotNull(insertCategory);
        Assert.assertTrue(categoryDto.getDisplayName().equals(insertCategory.getDisplayName()));
        Assert.assertTrue(categoryDto.getSearchKeyword().equals(insertCategory.getSearchKeyword()));
    }

    @Test
    public void 카테고리_수정() {
        CategoryDto categoryDto = CategoryDto
                .builder()
                .categoryId(originCategory.getCategoryId())
                .dtoType(DtoType.UPDATE)
                .displayName("updateCategory")
                .searchKeyword("updateCategory")
                .build();

        Category updateCategory = categoryService.updateCategory(originCategory.getCategoryId(), categoryDto);
        Assert.assertNotNull(updateCategory);
        Assert.assertTrue(categoryDto.getCategoryId() == updateCategory.getCategoryId());
        Assert.assertTrue(categoryDto.getSearchKeyword().equals(updateCategory.getSearchKeyword()));
        Assert.assertTrue(categoryDto.getDisplayName().equals(updateCategory.getDisplayName()));

        CategoryDto exceptionCategory = CategoryDto
                .builder()
                .categoryId(9999)
                .dtoType(DtoType.UPDATE)
                .searchKeyword("updateCategory")
                .searchKeyword("updateCategory")
                .build();
        wepetException.expect(WepetException.class);
        categoryService.updateCategory(originCategory.getCategoryId(), exceptionCategory);
    }

    @Test
    public void 카테고리_삭제() {
        categoryService.deleteCategory(originCategory.getCategoryId());
        Category category = categoryService.getCategory(originCategory.getCategoryId());
        Assert.assertTrue(category.isDeleted());

        wepetException.expect(WepetException.class);
        categoryService.deleteCategory(99999);
    }

}
