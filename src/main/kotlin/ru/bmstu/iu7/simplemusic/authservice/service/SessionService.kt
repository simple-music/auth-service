package ru.bmstu.iu7.simplemusic.authservice.service

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import ru.bmstu.iu7.simplemusic.authservice.model.AuthCredentials
import ru.bmstu.iu7.simplemusic.authservice.model.RefreshInfo
import ru.bmstu.iu7.simplemusic.authservice.model.SessionInfo
import ru.bmstu.iu7.simplemusic.authservice.repository.SessionRepository
import kotlin.random.Random

interface SessionService {
    fun startSession(authCredentials: AuthCredentials): SessionInfo
    fun refreshSession(refreshInfo: RefreshInfo): SessionInfo
    fun endSession(refreshInfo: RefreshInfo)
}

@Service
class SessionServiceImpl(@Value(value = "\${security.token-signing-key}")
                         private val tokenSigningKey: String,
                         @Autowired private val sessionRepository: SessionRepository,
                         @Autowired private val credentialsService: CredentialsService) : SessionService {
    private val algorithm = Algorithm.HMAC256(this.tokenSigningKey)

    override fun startSession(authCredentials: AuthCredentials): SessionInfo {
        val userId = this.credentialsService.getUserId(authCredentials)
        val sessionInfo = SessionInfo(
                userId = userId,
                authToken = this.createAuthToken(userId),
                refreshToken = this.createRefreshToken(userId)
        )
        this.sessionRepository.saveSession(sessionInfo.refreshToken, userId)
        return sessionInfo
    }

    override fun refreshSession(refreshInfo: RefreshInfo): SessionInfo {
        val userId = this.sessionRepository
                .getUserId(refreshInfo.refreshToken)
        return SessionInfo(
                userId = userId,
                authToken = this.createAuthToken(userId),
                refreshToken = refreshInfo.refreshToken
        )
    }

    override fun endSession(refreshInfo: RefreshInfo) {
        TODO("not implemented")
    }

    private fun createAuthToken(userId: String): String {
        return JWT.create()
                .withClaim("userId", userId)
                .sign(algorithm)
    }

    private fun createRefreshToken(userId: String): String {
        return JWT.create()
                .withClaim("tokenId", "$userId${Random.nextDouble()}")
                .sign(algorithm)
    }
}
