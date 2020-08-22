package com.testows.config

import com.testows.exceptions.CustomAuthorizationException
import com.testows.models.ErrorMessages
import com.testows.services.user.UserService
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
@EnableWebSecurity
class WebSecurity(private val userService: UserService,
                  private val bCryptPasswordEncoder: BCryptPasswordEncoder) : WebSecurityConfigurerAdapter() {
    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http.cors().and().csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.headers().frameOptions().disable()
        http.exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        http.authorizeRequests()
                .antMatchers(SecurityConstants.H2_CONSOLE_URL).permitAll()
                .antMatchers(SecurityConstants.STATIC_DIR).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_IN_URL).permitAll()
                .antMatchers(HttpMethod.GET, SecurityConstants.EMAIL_VERIFICATION_URL).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.REQUEST_PASSWORD_RESET_URL).permitAll()
                .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(getAuthenticationFilter())
                .addFilter(AuthorizationFilter(authenticationManager()))
    }

    @Throws(Exception::class)
    private fun getAuthenticationFilter(): AuthenticationFilter? {
        val authenticationFilter = AuthenticationFilter(
                userService,
                authenticationManager())
        authenticationFilter.setFilterProcessesUrl(SecurityConstants.SIGN_IN_URL)
        return authenticationFilter
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userService)?.passwordEncoder(bCryptPasswordEncoder)
    }
}