package team4.footwithme.stadium.api.request.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = StadiumAllowedValuesValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StadiumAllowedValues {
    String message() default "유효하지 않은 값입니다.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}