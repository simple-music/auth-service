package ru.bmstu.iu7.simplemusic.authservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.bmstu.iu7.simplemusic.authservice.domain.Credentials
import java.util.*

interface CredentialsRepository : JpaRepository<Credentials, String> {
    fun findByUsernameAndPassword(username: String, password: String): Optional<Credentials>
}
