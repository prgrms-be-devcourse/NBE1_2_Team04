package team4.footwithme.global.exception

import org.springframework.context.MessageSourceResolvable
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.web.bind.annotation.*
import org.springframework.web.method.annotation.HandlerMethodValidationException
import team4.footwithme.global.api.ApiResponse
import java.util.stream.Collectors

@RestControllerAdvice
class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        BindException::class
    )
    protected fun bindException(e: BindException): ApiResponse<Any?> {
        return ApiResponse.Companion.of<Any?>(
            HttpStatus.BAD_REQUEST,
            e.bindingResult.allErrors[0].defaultMessage,
            null
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        IllegalArgumentException::class
    )
    protected fun illegalArgumentException(e: IllegalArgumentException): ApiResponse<Any?> {
        return ApiResponse.Companion.of<Any?>(
            HttpStatus.BAD_REQUEST,
            e.message,
            null
        )
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(
        HandlerMethodValidationException::class
    )
    fun handleHandlerMethodValidationException(e: HandlerMethodValidationException): ApiResponse<Any?> {
        val message = e.allErrors.stream()
            .map { error: MessageSourceResolvable? -> error!!.defaultMessage }
            .collect(Collectors.joining(", "))

        return ApiResponse.Companion.of<Any?>(
            HttpStatus.BAD_REQUEST,
            message,
            null
        )
    }
}
