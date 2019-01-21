package ru.bmstu.iu7.simplemusic.authservice.controller

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ru.bmstu.iu7.simplemusic.authservice.exception.NotFoundException
import ru.bmstu.iu7.simplemusic.authservice.model.AuthCredentials
import ru.bmstu.iu7.simplemusic.authservice.model.Error
import ru.bmstu.iu7.simplemusic.authservice.model.SessionInfo
import ru.bmstu.iu7.simplemusic.authservice.service.SessionService
import java.util.*
import javax.ws.rs.core.MediaType

@RunWith(SpringRunner::class)
@WebMvcTest(controllers = [SessionController::class])
class SessionControllerTests {
    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val mockService: SessionService? = null

    private val notFoundException = NotFoundException("error message")
    private val notFoundErrorStr = this.mapObject(Error(this.notFoundException.message!!))

    @Test
    fun startSession() {
        val id = this.generateUserId()

        val authCredentials = this.generateAuthCredentials(id)
        val authCredentialsStr = this.mapObject(authCredentials)

        val sessionInfo = this.generateSessionInfo(id)
        val sessionInfoStr = this.mapObject(sessionInfo)

        Mockito
                .`when`(this.mockService!!.startSession(this.any()))
                .thenReturn(sessionInfo)

        this.mockMvc!!
                .perform(MockMvcRequestBuilders
                        .post("/sessions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(authCredentialsStr))
                .andExpect(MockMvcResultMatchers
                        .status().isOk)
                .andExpect(MockMvcResultMatchers
                        .content().string(sessionInfoStr))
    }

    @Test
    fun refreshSession() {
        val id = this.generateUserId()

        val sessionInfo = this.generateSessionInfo(id)
        val sessionInfoStr = this.mapObject(sessionInfo)

        Mockito
                .`when`(this.mockService!!.refreshSession(sessionInfo.refreshToken))
                .thenReturn(sessionInfo)

        this.mockMvc!!
                .perform(MockMvcRequestBuilders
                        .patch("/sessions?refreshToken=${sessionInfo.refreshToken}"))
                .andExpect(MockMvcResultMatchers
                        .status().isOk)
                .andExpect(MockMvcResultMatchers
                        .content().string(sessionInfoStr))

        Mockito
                .`when`(this.mockService.refreshSession(sessionInfo.refreshToken))
                .thenThrow(this.notFoundException)

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .patch("/sessions?refreshToken=${sessionInfo.refreshToken}"))
                .andExpect(MockMvcResultMatchers
                        .status().isNotFound)
                .andExpect(MockMvcResultMatchers
                        .content().string(this.notFoundErrorStr))
    }

    @Test
    fun endSession() {
        val refreshToken = this.generateRefreshToken()

        Mockito
                .doNothing()
                .`when`(this.mockService!!)
                .endSession(refreshToken)

        this.mockMvc!!
                .perform(MockMvcRequestBuilders
                        .delete("/sessions?refreshToken=$refreshToken"))
                .andExpect(MockMvcResultMatchers
                        .status().isOk)
    }

    private fun mapObject(obj: Any): String {
        return ObjectMapper().writeValueAsString(obj)
    }

    private fun generateAuthCredentials(id: String = this.generateUserId()): AuthCredentials {
        return AuthCredentials(
                username = this.generateUsername(id),
                password = this.generatePassword(id)
        )
    }

    private fun generateUserId(): String {
        return UUID.randomUUID().toString()
    }

    private fun generateUsername(id: String = this.generateUserId()): String {
        return "username$id"
    }

    private fun generatePassword(id: String = this.generateUserId()): String {
        return "password$id"
    }

    private fun generateSessionInfo(id: String = this.generateUserId()): SessionInfo {
        return SessionInfo(
                userId = id,
                authToken = this.generateAuthToken(id),
                refreshToken = this.generateRefreshToken(id)
        )
    }

    private fun generateAuthToken(id: String = this.generateUserId()): String {
        return "authToken$id"
    }

    private fun generateRefreshToken(id: String = this.generateUserId()): String {
        return "refreshToken$id"
    }

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress(names = ["UNCHECKED_CAST"])
    private fun <T> uninitialized(): T = null as T
}
