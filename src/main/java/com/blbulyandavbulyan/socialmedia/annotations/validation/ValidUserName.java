package com.blbulyandavbulyan.socialmedia.annotations.validation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@NotBlank(message = "User name is blank!")
@Size(min = 1, message = "User name too short, minimum size in symbols is 1")
@Size(max = 50, message = "User name too long, maximum size in symbols is 50")
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.RECORD_COMPONENT})
public @interface ValidUserName {
}
