package team4.footwithme.vote.api.request.annotation.validator

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import team4.footwithme.vote.api.request.annotation.Duplicate

class DuplicateValidator : ConstraintValidator<Duplicate?, List<*>> {
    override fun isValid(list: List<*>, context: ConstraintValidatorContext): Boolean {
        return list.stream().distinct().count() == list.size.toLong()
    }
}
