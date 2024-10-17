package team4.footwithme.stadium.api.request.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class StadiumAllowedValuesValidator : ConstraintValidator<StadiumAllowedValues?, String?> {
    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value == null) {
            return true
        }

        if (!VALID_SORT_FIELDS.contains(value)) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate(
                "정렬 기준은 다음과 같습니다: " + VALID_SORT_FIELDS
            ).addConstraintViolation()
            return false
        }
        return true
    }

    companion object {
        private val VALID_SORT_FIELDS: List<String> = mutableListOf("STADIUM", "NAME", "ADDRESS")
    }
}
