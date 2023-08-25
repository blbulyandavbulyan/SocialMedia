package com.blbulyandavbulyan.socialmedia.annotations.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@NotBlank
@Size(min = 10, message = "Password too short, must be at least 10 symbols")
@Size(max = 100, message = "Password too long, maximum length is 100")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface ValidPassword {
}
