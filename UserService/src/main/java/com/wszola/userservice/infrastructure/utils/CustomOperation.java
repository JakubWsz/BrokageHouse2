package com.wszola.userservice.infrastructure.utils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Operation(responses = {
		@ApiResponse(description = "Successful operation", responseCode = "200", content = @Content),
		@ApiResponse(description = "Bad request", responseCode = "400", content = @Content),
		@ApiResponse(description = "Internal server error", responseCode = "500", content = @Content)
})
public @interface CustomOperation {
	@AliasFor(annotation = Operation.class, attribute = "summary")
	String summary() default "";
}