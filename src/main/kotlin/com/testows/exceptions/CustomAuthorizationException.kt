package com.testows.exceptions

class CustomAuthorizationException(override val message: String): RuntimeException(message)
