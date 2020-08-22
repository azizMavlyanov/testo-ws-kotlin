package com.testows.models

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class PasswordResetModel(
        @field:NotBlank(message = "Token can be neither null or empty")
        var token: String,
        @field:Size(min = 4, max = 16, message = "Password length must be between 4 and 16 characters long")
        var password: String
)
