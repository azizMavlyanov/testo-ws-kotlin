package com.testows.exceptions

class CommonServiceException(override val message: String): RuntimeException(message) {
}