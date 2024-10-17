package team4.footwithme.vote.api.request.annotation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import team4.footwithme.vote.api.request.annotation.validator.DuplicateValidator
import kotlin.reflect.KClass

@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DuplicateValidator::class])
annotation class Duplicate(
    val message: String = "중복된 값이 포함되어 있습니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Any>> = []
)
