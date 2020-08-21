package com.testows.models

import javax.validation.constraints.Email

data class PasswordResetModel (
        @field:Email(message = "Invalid email address")
        var email: String
)
