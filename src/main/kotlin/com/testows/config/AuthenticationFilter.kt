package com.testows.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.testows.models.UserLoginRequest
import com.testows.services.user.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import java.util.*
import javax.servlet.FilterChain
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.ArrayList

class AuthenticationFilter(private val userService: UserService,
                           authenticationManager: AuthenticationManager?) : UsernamePasswordAuthenticationFilter() {

    init {
        super.setAuthenticationManager(authenticationManager)
    }

    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        try {
            val userCredentials = jacksonObjectMapper()
                    .readValue(request?.inputStream, UserLoginRequest::class.java)

            return authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken(
                            userCredentials.email,
                            userCredentials.password,
                            ArrayList()
                    )
            )

        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?,
                                          chain: FilterChain?, authResult: Authentication?) {
        val userName = (authResult?.principal as User).username
        val userDetails = userService.loadUserByUsername(userName)

        val token: String = Jwts.builder()
                .setSubject(userDetails?.password)
                .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME)) // 10 days
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
                .compact()

        response?.addHeader(SecurityConstants.HEADER_STRING, "${SecurityConstants.TOKEN_PREFIX} $token")
        response?.addCookie(Cookie("auth_token", token))
    }
}