package com.testows.exceptions

class BadRequestException(override val message: String): RuntimeException(message) {
}