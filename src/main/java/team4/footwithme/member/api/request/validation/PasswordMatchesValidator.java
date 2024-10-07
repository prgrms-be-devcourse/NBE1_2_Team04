package team4.footwithme.member.api.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private String passwordField;
    private String passwordConfirmField;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.passwordField = constraintAnnotation.passwordField();
        this.passwordConfirmField = constraintAnnotation.passwordConfirmField();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {

        try {
            Field password = value.getClass().getDeclaredField(passwordField);
            Field passwordConfirm = value.getClass().getDeclaredField(passwordConfirmField);

            password.setAccessible(true);
            passwordConfirm.setAccessible(true);

            String passwordValue = (String) password.get(value);
            String passwordConfirmValue = (String) passwordConfirm.get(value);

            if(passwordValue == null && passwordConfirmValue == null){ // OAuth 2.0 로그인 일 시
                return true;
            }

            return passwordValue.equals(passwordConfirmValue);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}