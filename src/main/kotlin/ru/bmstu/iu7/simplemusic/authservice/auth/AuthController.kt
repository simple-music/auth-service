package ru.bmstu.iu7.simplemusic.authservice.auth

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/auth"])
class AuthController(@Autowired private val authManager: AuthManager) {
    @PostMapping
    fun authorize(@RequestBody serviceCredentials: ServiceCredentials): ResponseEntity<ServiceAuthToken> {
        val token = this.authManager.getToken(
                serviceCredentials.serviceId,
                serviceCredentials.servicePassword
        )
        return if (token == null) {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        } else {
            ResponseEntity.ok(ServiceAuthToken(token))
        }
    }
}

data class ServiceCredentials(
        @JsonProperty(value = "serviceId", required = true)
        val serviceId: String,
        @JsonProperty(value = "servicePassword", required = true)
        val servicePassword: String
)

data class ServiceAuthToken(
        @JsonProperty(value = "authToken")
        val authToken: String
)
