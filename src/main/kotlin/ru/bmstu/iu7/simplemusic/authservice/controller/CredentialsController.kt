package ru.bmstu.iu7.simplemusic.authservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.bmstu.iu7.simplemusic.authservice.model.NewCredentials
import ru.bmstu.iu7.simplemusic.authservice.service.CredentialsService

@RestController
@RequestMapping(value = ["/credentials"])
class CredentialsController(@Autowired private val credentialsService: CredentialsService) {
    @PostMapping
    fun addCredentials(@RequestBody newCredentials: NewCredentials): ResponseEntity<Any> {
        this.credentialsService.addCredentials(newCredentials)
        return ResponseEntity.ok().build()
    }

    @DeleteMapping(value = ["/{userId}"])
    fun deleteCredentials(@PathVariable(value = "userId") userId: String): ResponseEntity<Any> {
        this.credentialsService.deleteCredentials(userId)
        return ResponseEntity.ok().build()
    }
}
