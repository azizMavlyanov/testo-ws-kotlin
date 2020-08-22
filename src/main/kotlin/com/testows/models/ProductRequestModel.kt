package com.testows.models

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class ProductRequestModel(
        @field:NotBlank(message = "Product name can be neither null nor blank")
        var productName: String,
        @field:NotNull(message = "Product price cannot be null")
        var productPrice: Float,
        var active: Boolean = true,
        var productDescription: String = "No description",
        var categoryId: Long?
)