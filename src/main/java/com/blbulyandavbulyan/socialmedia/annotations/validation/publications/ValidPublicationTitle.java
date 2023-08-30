package com.blbulyandavbulyan.socialmedia.annotations.validation.publications;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
@Constraint(validatedBy = {})
@NotBlank
public @interface ValidPublicationTitle {
    String message() default "Publication title is not valid!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
