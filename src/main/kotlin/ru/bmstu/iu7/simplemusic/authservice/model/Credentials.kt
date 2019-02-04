package ru.bmstu.iu7.simplemusic.authservice.model

import com.fasterxml.jackson.annotation.JsonProperty

data class NewCredentials(
        @JsonProperty(value = "userId", required = true)
        val userId: String,

        @JsonProperty(value = "username", required = true)
        val username: String,

        @JsonProperty(value = "password", required = true)
        val password: String
)

data class AuthCredentials(
        @JsonProperty(value = "username", required = true)
        val username: String,

        @JsonProperty(value = "password", required = true)
        val password: String
)
