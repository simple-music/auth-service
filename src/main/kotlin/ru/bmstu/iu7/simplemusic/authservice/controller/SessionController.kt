package ru.bmstu.iu7.simplemusic.authservice.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ru.bmstu.iu7.simplemusic.authservice.model.AuthCredentials
import ru.bmstu.iu7.simplemusic.authservice.model.RefreshInfo
import ru.bmstu.iu7.simplemusic.authservice.model.SessionInfo
import ru.bmstu.iu7.simplemusic.authservice.service.SessionService

@RestController
@RequestMapping(value = ["/sessions"])
class SessionController(@Autowired private val sessionService: SessionService) {
    @PostMapping
    fun startSession(@RequestBody authCredentials: AuthCredentials): ResponseEntity<SessionInfo> {
        val info = this.sessionService.startSession(authCredentials)
        return ResponseEntity.ok(info)
    }

    @PatchMapping
    fun refreshSession(@RequestBody refreshInfo: RefreshInfo): ResponseEntity<SessionInfo> {
        val info = this.sessionService.refreshSession(refreshInfo)
        return ResponseEntity.ok(info)
    }

    @DeleteMapping
    fun endSession(@RequestBody refreshInfo: RefreshInfo): ResponseEntity<Any> {
        TODO()
    }
}
