package team4.footwithme

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
object FootwithmeApplication {
    @JvmStatic
    fun main(args: Array<String>) {
        SpringApplication.run(FootwithmeApplication::class.java, *args)
    }
}
