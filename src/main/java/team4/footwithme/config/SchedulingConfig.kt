package team4.footwithme.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

@EnableScheduling
@Configuration
class SchedulingConfig {
    @Bean
    fun taskScheduler(): ThreadPoolTaskScheduler {
        val taskScheduler = ThreadPoolTaskScheduler()
        taskScheduler.poolSize = POOL_SIZE
        taskScheduler.threadNamePrefix = THREAD_NAME_PREFIX
        taskScheduler.initialize()
        return taskScheduler
    }

    companion object {
        const val POOL_SIZE: Int = 10
        const val THREAD_NAME_PREFIX: String = "TaskScheduler-"
    }
}
