package com.depromeet.wepet.domains.category.dto;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CategoryValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCategory {
    String message() default "CategoryDto is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
