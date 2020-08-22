package com.testows.exceptions

class ResourceNotFoundException(override val message: String): RuntimeException(message)