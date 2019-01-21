package ru.bmstu.iu7.simplemusic.authservice.auth

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.*
import kotlin.random.Random

interface AuthManager {
    fun checkToken(token: String): Boolean
    fun getToken(id: String, password: String): String?
}

@Component
class AuthManagerImpl(
        @Value("\${security.service-id}")
        private val serviceId: String,

        @Value("\${security.service-password}")
        private val servicePassword: String,

        @Value("\${security.token-lifetime}")
        private val tokenLifetime: Int) : AuthManager {
    private var token = this.generateToken()
    private var tokenExpTime = this.countTokenExpTime()

    override fun checkToken(token: String): Boolean {
        synchronized(lock = this) {
            if (Date() > this.tokenExpTime) {
                this.token = this.generateToken()
                this.tokenExpTime = this.countTokenExpTime()
            }
            return token == this.token
        }
    }

    override fun getToken(id: String, password: String): String? {
        synchronized(lock = this) {
            if (id != this.serviceId || password != this.servicePassword) {
                return null
            }
            if (Date() > this.tokenExpTime) {
                this.token = this.generateToken()
                this.tokenExpTime = this.countTokenExpTime()
            }
            return this.token
        }
    }

    private fun generateToken(): String {
        val token = (1..TOKEN_LENGTH)
                .map {
                    Random.nextInt(from = 0, until = charPool.size)
                }
                .map(charPool::get)
                .joinToString(separator = "")
        log.info("Authorization token: $token")
        return token
    }

    private fun countTokenExpTime(): Date {
        val date = Calendar.getInstance()
        return Date(date.timeInMillis + this.tokenLifetime * TIME_MINUTE)
    }

    private companion object {
        private val log = LoggerFactory.getLogger(AuthManagerImpl::class.java)

        private const val TOKEN_LENGTH = 100
        private const val TIME_MINUTE = 60000

        private val charPool: List<Char> =
                ('a'..'z') + ('A'..'Z') + ('0'..'9')
    }
}
