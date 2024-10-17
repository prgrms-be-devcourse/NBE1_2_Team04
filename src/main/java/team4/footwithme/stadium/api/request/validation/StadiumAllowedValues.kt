package team4.footwithme.stadium.api.request.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [StadiumAllowedValuesValidator::class])
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FIELD)
@Retention(
    AnnotationRetention.RUNTIME
)
annotation class StadiumAllowedValues(
    val message: String = "유효하지 않은 값입니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload?>> = []
)