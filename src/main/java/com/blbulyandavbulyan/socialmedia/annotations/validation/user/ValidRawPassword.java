package com.blbulyandavbulyan.socialmedia.annotations.validation.user;

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
@Size(min = 10, max = 100)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface ValidRawPassword {
    String message() default "Password is not valid!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
