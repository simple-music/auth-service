package ru.bmstu.iu7.simplemusic.authservice.repository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import ru.bmstu.iu7.simplemusic.authservice.exception.NotFoundException

interface SessionRepository {
    fun saveSession(token: String, userId: String)
    fun getUserId(token: String): String
    fun deleteSession(token: String)
}

@Repository
class SessionRepositoryImpl(@Autowired private val redisTemplate: StringRedisTemplate) : SessionRepository {
    override fun saveSession(token: String, userId: String) {
        this.redisTemplate.opsForValue().set(token, userId)
    }

    override fun getUserId(token: String): String {
        return this.redisTemplate.opsForValue().get(token)
                ?: throw NotFoundException("token not found")
    }

    override fun deleteSession(token: String) {
       this.redisTemplate.opsForValue().operations.delete(token)
    }
}
