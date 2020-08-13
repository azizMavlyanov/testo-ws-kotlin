package com.testows

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TestoWsApplication

fun main(args: Array<String>) {
    runApplication<TestoWsApplication>(*args)
}
