package ru.bmstu.iu7.simplemusic.authservice.domain

import javax.persistence.*

@Entity
@Table(name = "credentials", indexes = [
        Index(name = "username_password_idx", columnList = "username,password")
])
data class Credentials(
        @Id
        @Column(name = "id")
        val userId: String = "",

        @Column(name = "username", unique = true)
        val username: String = "",

        @Column(name = "password")
        val password: String = ""
)
