package ru.bmstu.iu7.simplemusic.authservice.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.bmstu.iu7.simplemusic.authservice.model.AuthCredentials
import ru.bmstu.iu7.simplemusic.authservice.model.RefreshInfo
import ru.bmstu.iu7.simplemusic.authservice.model.SessionInfo

@RestController
@RequestMapping(value = ["/sessions"])
class SessionController {
    @PostMapping
    fun startSession(@RequestBody authCredentials: AuthCredentials): ResponseEntity<SessionInfo> {
        TODO()
    }

    @PatchMapping
    fun refreshSession(@RequestBody refreshInfo: RefreshInfo): ResponseEntity<SessionInfo> {
        TODO()
    }

    @DeleteMapping
    fun endSession(@RequestBody refreshInfo: RefreshInfo): ResponseEntity<Any> {
        TODO()
    }
}
