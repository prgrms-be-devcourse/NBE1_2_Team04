package team4.footwithme.member.api.request.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordMatchesValidator.class)
@Target({ElementType.TYPE}) // 클래스 수준에서 검증
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordMatches {
    String message() default "패스워드가 일치하지 않습니다.";

    String passwordField();

    String passwordConfirmField();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}