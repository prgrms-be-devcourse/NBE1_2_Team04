package team4.footwithme.member.api.request.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [PasswordMatchesValidator::class])
@Target(AnnotationTarget.CLASS) // 클래스 수준에서 검증
@Retention(AnnotationRetention.RUNTIME)
annotation class PasswordMatches(
    val message: String = "패스워드가 일치하지 않습니다.",
    val passwordField: String,
    val passwordConfirmField: String,
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)