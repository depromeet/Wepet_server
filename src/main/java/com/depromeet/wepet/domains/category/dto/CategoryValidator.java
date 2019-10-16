package com.depromeet.wepet.domains.category.dto;

import com.depromeet.wepet.domains.common.constans.DtoType;
import io.micrometer.core.instrument.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CategoryValidator implements ConstraintValidator<ValidCategory, CategoryDto> {

    @Override
    public void initialize(ValidCategory constraintAnnotation) {

    }

    @Override
    public boolean isValid(CategoryDto value, ConstraintValidatorContext context) {
        int invalidCount = 0;

        if (value.getDtoType() == null) {
            addConstraintViolation(context, "dtoType is required", "dtoType");
            invalidCount++;
        }

        if (value.getDtoType() == DtoType.UPDATE && value.getCategoryId() == 0) {
            addConstraintViolation(context, "categoryId is required", "categoryId");
            invalidCount++;
        }

        if (value.getDtoType() == DtoType.INSERT && value.getCategoryId() > 0) {
            addConstraintViolation(context, "Insert does not require categoryId.", "categoryId");
            invalidCount++;
        }

        if (StringUtils.isBlank(value.getDisplayName())) {
            addConstraintViolation(context, "displayName is required", "displayName");
            invalidCount++;
        }

        if (StringUtils.isBlank(value.getSearchKeyword())) {
            addConstraintViolation(context, "searchKeyWord is required", "searchKeyword");
            invalidCount++;
        }

        return invalidCount == 0;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String errorMessage, String firstNode) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errorMessage)
                .addPropertyNode(firstNode)
                .addConstraintViolation();
    }
}
