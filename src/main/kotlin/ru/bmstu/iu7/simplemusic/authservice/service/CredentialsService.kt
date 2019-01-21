package ru.bmstu.iu7.simplemusic.authservice.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.EmptyResultDataAccessException
import org.springframework.stereotype.Service
import ru.bmstu.iu7.simplemusic.authservice.domain.Credentials
import ru.bmstu.iu7.simplemusic.authservice.exception.NotFoundException
import ru.bmstu.iu7.simplemusic.authservice.model.AuthCredentials
import ru.bmstu.iu7.simplemusic.authservice.model.NewCredentials
import ru.bmstu.iu7.simplemusic.authservice.repository.CredentialsRepository
import java.security.MessageDigest

interface CredentialsService {
    fun addCredentials(newCredentials: NewCredentials)
    fun getUserId(authCredentials: AuthCredentials): String
    fun deleteCredentials(userId: String)
}

@Service
class CredentialsServiceImpl(
        @Autowired
        private val credentialsRepository: CredentialsRepository) : CredentialsService {
    override fun addCredentials(newCredentials: NewCredentials) {
        this.credentialsRepository.save(Credentials(
                userId = newCredentials.userId,
                username = newCredentials.username,
                password = this.encodePassword(newCredentials.password)
        ))
    }

    override fun getUserId(authCredentials: AuthCredentials): String {
        val credentials = this.credentialsRepository
                .findByUsernameAndPassword(
                        username = authCredentials.username,
                        password = this.encodePassword(authCredentials.password)
                )
        return if (!credentials.isPresent) {
            throw NotFoundException("credentials not found")
        } else {
            credentials.get().userId
        }
    }

    override fun deleteCredentials(userId: String) {
        try {
            this.credentialsRepository.deleteById(userId)
        }
        catch (exception: EmptyResultDataAccessException) {
            throw NotFoundException("credentials not found")
        }
    }

    private fun encodePassword(password: String): String {
        val bytes = MessageDigest
                .getInstance("SHA-256")
                .digest(password.toByteArray())
        val result = StringBuilder(bytes.size * 2)

        bytes.forEach {
            val i = it.toInt()
            result.append(HEX_CHARS[i shr 4 and 0x0f])
            result.append(HEX_CHARS[i and 0x0f])
        }

        return result.toString()
    }

    private companion object {
        private const val HEX_CHARS = "0123456789ABCDEF"
    }
}
