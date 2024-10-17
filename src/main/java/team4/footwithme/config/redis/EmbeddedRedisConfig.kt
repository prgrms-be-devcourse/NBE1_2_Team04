package team4.footwithme.config.redis

import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.util.StringUtils
import redis.embedded.RedisServer
import java.io.*
import java.util.*

/**
 * 로컬 환경일경우 내장 레디스가 실행됩니다.
 */
@Profile("local")
@Configuration
class EmbeddedRedisConfig {
    @Value("\${spring.data.redis.port}")
    private val redisPort = 0

    private var redisServer: RedisServer? = null

    @PostConstruct
    @Throws(IOException::class)
    fun redisServer() {
        val port = if (isRedisRunning) findAvailablePort() else redisPort
        if (isArmMac) {
            redisServer = RedisServer(redisServerExecutable, redisPort)
        } else {
            redisServer = RedisServer.builder()
                .port(port)
                .setting("maxmemory 128M")
                .build()
            redisServer.start()
        }
    }

    @PreDestroy
    fun stopRedis() {
        if (redisServer != null) {
            redisServer!!.stop()
        }
    }

    private val redisServerExecutable: File
        get() {
            try {
                return File("src/main/resources/binary/redis/redis-server-6.2.5-mac-arm64")
            } catch (e: Exception) {
                throw NoSuchElementException("Redis Server Executable File Not Found")
            }
        }

    private val isArmMac: Boolean
        get() = System.getProperty("os.arch") == "aarch64" && System.getProperty("os.name") == "Mac OS X"

    @get:Throws(IOException::class)
    private val isRedisRunning: Boolean
        /**
         * Embedded Redis가 현재 실행중인지 확인
         */
        get() = isRunning(executeGrepProcessCommand(redisPort))

    /**
     * 현재 PC/서버에서 사용가능한 포트 조회
     */
    @Throws(IOException::class)
    fun findAvailablePort(): Int {
        for (port in 10000..65535) {
            val process = executeGrepProcessCommand(port)
            if (!isRunning(process)) {
                return port
            }
        }

        throw IllegalArgumentException("Not Found Available port: 10000 ~ 65535")
    }

    /**
     * 해당 port를 사용중인 프로세스 확인하는 sh 실행
     */
    @Throws(IOException::class)
    private fun executeGrepProcessCommand(port: Int): Process {
        val OS = System.getProperty("os.name").lowercase(Locale.getDefault())
        if (OS.contains("win")) {
            val command = String.format("netstat -nao | find \"LISTEN\" | find \"%d\"", port)
            val shell = arrayOf("cmd.exe", "/y", "/c", command)
            return Runtime.getRuntime().exec(shell)
        }
        val command = String.format("netstat -nat | grep LISTEN|grep %d", port)
        val shell = arrayOf("/bin/sh", "-c", command)
        return Runtime.getRuntime().exec(shell)
    }

    /**
     * 해당 Process가 현재 실행중인지 확인
     */
    private fun isRunning(process: Process): Boolean {
        var line: String?
        val pidInfo = StringBuilder()

        try {
            BufferedReader(InputStreamReader(process.inputStream)).use { input ->
                while ((input.readLine().also { line = it }) != null) {
                    pidInfo.append(line)
                }
            }
        } catch (e: Exception) {
        }

        return !StringUtils.isEmpty(pidInfo.toString())
    }
}
