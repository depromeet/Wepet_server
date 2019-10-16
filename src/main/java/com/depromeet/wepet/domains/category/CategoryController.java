package com.depromeet.wepet.domains.category;

import com.depromeet.wepet.domains.category.dto.CategoryDto;
import com.depromeet.wepet.domains.common.respose.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getCategories() {
        return new ResponseEntity(Response.of(categoryService.getCategories()), HttpStatus.OK);
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<Category> getCategory(@PathVariable("categoryId") long categoryId) {
        return new ResponseEntity(Response.of(categoryService.getCategory(categoryId)), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> insertCategory(@RequestBody @Valid CategoryDto categoryInsertDto) {
        Category category = categoryService.insertCategory(categoryInsertDto);
        return new ResponseEntity(Response.of(category), HttpStatus.CREATED);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") long categoryId,
                                         @RequestBody @Valid CategoryDto categoryDto) {
        return new ResponseEntity(Response.of(categoryService.updateCategory(categoryId, categoryDto)), HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity(Response.of(null), HttpStatus.OK);
    }
}
