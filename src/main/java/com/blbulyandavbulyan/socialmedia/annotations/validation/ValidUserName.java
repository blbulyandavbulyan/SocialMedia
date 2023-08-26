package com.blbulyandavbulyan.socialmedia.annotations.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@Constraint(validatedBy = {})
@NotBlank
@Size(min = 1, max = 50)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface ValidUserName {
    String message() default "User name is not valid!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
