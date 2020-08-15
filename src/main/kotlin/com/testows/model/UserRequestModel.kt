package com.testows.model

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class UserRequestModel(
        @field:NotBlank(message = "First name cannot be neither null nor blank")
        var firstName: String,
        var lastName: String?,
        @field:Email(message = "Invalid email address")
        var email: String,
        @field:Size(min = 4, max = 16, message = "Password length must be between 4 and 16 characters long")
        var password: String
)
