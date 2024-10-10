package team4.footwithme.stadium.api.request.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.List;

public class StadiumAllowedValuesValidator implements ConstraintValidator<StadiumAllowedValues, String> {

    private static final List<String> VALID_SORT_FIELDS = Arrays.asList("STADIUM", "NAME", "ADDRESS");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        if (!VALID_SORT_FIELDS.contains(value)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                "정렬 기준은 다음과 같습니다: " + VALID_SORT_FIELDS
            ).addConstraintViolation();
            return false;
        }
        return true;
    }
}
