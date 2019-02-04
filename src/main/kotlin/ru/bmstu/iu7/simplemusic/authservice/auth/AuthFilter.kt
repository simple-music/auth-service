package ru.bmstu.iu7.simplemusic.authservice.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Profile(value = ["dev", "prod"])
class AuthFilter(private val authManager: AuthManager) : Filter {
    override fun doFilter(request: ServletRequest,
                          response: ServletResponse,
                          chain: FilterChain) {
        val req = request as HttpServletRequest
        val token = req.getHeader("X-Gateway-Token")
        if (token == null || !this.authManager.checkToken(token)) {
            val resp = response as HttpServletResponse
            resp.status = HttpStatus.UNAUTHORIZED.value()
        } else {
            chain.doFilter(request, response)
        }
    }
}

@Configuration
@Profile(value = ["dev", "prod"])
class AuthFilterConfig(@Autowired private val authManager: AuthManager) {
    @Bean
    fun authFilterBean(): FilterRegistrationBean<AuthFilter> {
        val bean = FilterRegistrationBean<AuthFilter>()

        bean.filter = AuthFilter(this.authManager)
        bean.addUrlPatterns("/credentials/*", "/sessions/*")

        return bean
    }
}
