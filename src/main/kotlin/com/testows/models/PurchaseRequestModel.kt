package com.testows.models

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

data class PurchaseRequestModel(
        @field:NotBlank(message = "Address cannot be neither null nor blank")
        var address: String,
        @field:Pattern(regexp = "(open|pending|closed|rejected)", message = "Invalid status value")
        var status: String = PurchaseStatus.OPEN.status,
        @field:NotNull(message = "Purchase items cannot be null")
        var purchaseItems: MutableList<PurchaseItemRequestModel>
)
