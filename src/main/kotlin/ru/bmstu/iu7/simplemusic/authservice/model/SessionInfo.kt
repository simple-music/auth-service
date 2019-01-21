package ru.bmstu.iu7.simplemusic.authservice.model

import com.fasterxml.jackson.annotation.JsonProperty

data class SessionInfo(
        @JsonProperty(value = "userId")
        val userId: String,

        @JsonProperty(value = "authToken")
        val authToken: String,

        @JsonProperty(value = "refreshToken")
        val refreshToken: String
)

data class RefreshInfo(
        @JsonProperty(value = "refreshToken", required = true)
        val refreshToken: String
)
