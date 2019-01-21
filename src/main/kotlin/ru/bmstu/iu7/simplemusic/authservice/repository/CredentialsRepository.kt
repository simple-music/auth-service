package ru.bmstu.iu7.simplemusic.authservice.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.bmstu.iu7.simplemusic.authservice.domain.Credentials

interface CredentialsRepository : JpaRepository<Credentials, String>
