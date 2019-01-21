package ru.bmstu.iu7.simplemusic.authservice.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ru.bmstu.iu7.simplemusic.authservice.model.AuthCredentials
import ru.bmstu.iu7.simplemusic.authservice.model.RefreshInfo
import ru.bmstu.iu7.simplemusic.authservice.model.SessionInfo
import ru.bmstu.iu7.simplemusic.authservice.repository.CredentialsRepository
import ru.bmstu.iu7.simplemusic.authservice.repository.SessionRepository

interface SessionService {
    fun startSession(authCredentials: AuthCredentials): SessionInfo
    fun refreshSession(refreshInfo: RefreshInfo): SessionInfo
    fun endSession(refreshInfo: RefreshInfo)
}

@Service
class SessionServiceImpl(@Autowired private val sessionRepository: SessionRepository,
                         @Autowired private val credentialsService: CredentialsService) : SessionService {
    override fun startSession(authCredentials: AuthCredentials): SessionInfo {
        TODO("not implemented")
    }

    override fun refreshSession(refreshInfo: RefreshInfo): SessionInfo {
        TODO("not implemented")
    }

    override fun endSession(refreshInfo: RefreshInfo) {
        TODO("not implemented")
    }
}
