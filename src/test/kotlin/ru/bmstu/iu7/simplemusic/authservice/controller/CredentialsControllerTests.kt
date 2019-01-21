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
import ru.bmstu.iu7.simplemusic.authservice.model.Error
import ru.bmstu.iu7.simplemusic.authservice.model.NewCredentials
import ru.bmstu.iu7.simplemusic.authservice.service.CredentialsService
import java.util.*
import javax.ws.rs.core.MediaType

@RunWith(SpringRunner::class)
@WebMvcTest(controllers = [CredentialsController::class])
class CredentialsControllerTests {
    @Autowired
    private val mockMvc: MockMvc? = null

    @MockBean
    private val mockService: CredentialsService? = null

    private val notFoundException = NotFoundException("error message")
    private val notFoundErrorStr = this.mapObject(Error(this.notFoundException.message!!))

    @Test
    fun addCredentials() {
        val newCredentials = this.generateNewCredentials()
        val newCredentialsStr = this.mapObject(newCredentials)

        Mockito
                .doNothing().`when`(this.mockService)
                ?.addCredentials(this.any())

        this.mockMvc!!
                .perform(MockMvcRequestBuilders
                        .post("/credentials")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(newCredentialsStr))
                .andExpect(MockMvcResultMatchers
                        .status().isOk)
    }

    @Test
    fun deleteCredentials() {
        val userId = this.generateUserId()

        Mockito
                .doNothing().`when`(this.mockService)
                ?.deleteCredentials(userId)

        this.mockMvc!!
                .perform(MockMvcRequestBuilders
                        .delete("/credentials/$userId"))
                .andExpect(MockMvcResultMatchers
                        .status().isOk)

        Mockito
                .doThrow(this.notFoundException)
                .`when`(this.mockService)
                ?.deleteCredentials(userId)

        this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/credentials/$userId"))
                .andExpect(MockMvcResultMatchers
                        .status().isNotFound)
                .andExpect(MockMvcResultMatchers
                        .content().string(this.notFoundErrorStr))
    }

    private fun mapObject(obj: Any): String {
        return ObjectMapper().writeValueAsString(obj)
    }

    private fun generateNewCredentials(id: String = this.generateUserId()): NewCredentials {
        return NewCredentials(
                userId = id,
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

    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress(names = ["UNCHECKED_CAST"])
    private fun <T> uninitialized(): T = null as T
}
