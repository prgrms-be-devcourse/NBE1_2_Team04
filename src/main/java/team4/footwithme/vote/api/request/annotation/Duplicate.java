package team4.footwithme.vote.api.request.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import team4.footwithme.vote.api.request.annotation.validator.DuplicateValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DuplicateValidator.class)
public @interface Duplicate {
    String message() default "중복된 값이 포함되어 있습니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
