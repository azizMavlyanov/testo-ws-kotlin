package com.testows.models

import org.springframework.validation.annotation.Validated
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

@Validated
data class PurchaseItemRequestModel(
        @field:NotNull(message = "Product ID cannot be null")
        @field:Min(value = 1, message = "Product ID must be greater than")
        var productId: Long,
        @field:NotNull(message = "Quantity cannot be null")
        @field:Min(value = 1, message = "Quantity must be greater than 0")
        var quantity: Long
)
