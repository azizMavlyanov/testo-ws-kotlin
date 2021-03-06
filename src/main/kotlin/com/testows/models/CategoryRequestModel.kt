package com.testows.models

import javax.validation.constraints.NotBlank

data class CategoryRequestModel(
        @field:NotBlank(message = "Category name can be neither null nor blank")
        var categoryName: String
)