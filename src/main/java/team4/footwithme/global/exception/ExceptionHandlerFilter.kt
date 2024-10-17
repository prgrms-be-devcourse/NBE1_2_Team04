package team4.footwithme.global.exception

import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import team4.footwithme.global.api.ApiResponse
import java.io.IOException

@Component
class ExceptionHandlerFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: JwtException) {
            log.error(e.message)
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, e)
        }
    }

    fun setErrorResponse(status: HttpStatus, response: HttpServletResponse, ex: Throwable) {
        val objectMapper = ObjectMapper()
        response.status = status.value()
        response.contentType = "application/json"
        val apiResponse: ApiResponse<*> = ApiResponse.Companion.of<Any?>(
            HttpStatus.UNAUTHORIZED,
            ex.message,
            null
        )
        try {
            val jsonResponse = objectMapper.writeValueAsString(apiResponse)
            response.characterEncoding = "UTF-8"
            response.writer.write(jsonResponse)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ExceptionHandlerFilter::class.java)
    }
}
