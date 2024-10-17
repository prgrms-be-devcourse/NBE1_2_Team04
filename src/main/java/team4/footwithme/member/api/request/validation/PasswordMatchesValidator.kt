package team4.footwithme.member.api.request.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class PasswordMatchesValidator : ConstraintValidator<PasswordMatches, Any> {
    private var passwordField: String? = null
    private var passwordConfirmField: String? = null

    override fun initialize(constraintAnnotation: PasswordMatches) {
        this.passwordField = constraintAnnotation.passwordField
        this.passwordConfirmField = constraintAnnotation.passwordConfirmField
    }

    override fun isValid(value: Any, constraintValidatorContext: ConstraintValidatorContext): Boolean {
        try {
            val password = value.javaClass.getDeclaredField(passwordField)
            val passwordConfirm = value.javaClass.getDeclaredField(passwordConfirmField)

            password.isAccessible = true
            passwordConfirm.isAccessible = true

            val passwordValue = password[value] as String
            val passwordConfirmValue = passwordConfirm[value] as String

            if (passwordValue == null && passwordConfirmValue == null) { // OAuth 2.0 로그인 일 시
                return true
            }

            return passwordValue == passwordConfirmValue
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}