package com.testows

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class TestoWsApplication {
    @Bean
    open fun bCryptPasswordEncoder(): BCryptPasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}

fun main(args: Array<String>) {
    runApplication<TestoWsApplication>(*args)
}




