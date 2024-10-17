package team4.footwithme.vote.api.request.annotation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import team4.footwithme.vote.api.request.annotation.Duplicate;

import java.util.List;

public class DuplicateValidator implements ConstraintValidator<Duplicate, List<?>> {
    @Override
    public boolean isValid(List<?> list, ConstraintValidatorContext context) {
        return list.stream().distinct().count() == list.size();
    }
}
