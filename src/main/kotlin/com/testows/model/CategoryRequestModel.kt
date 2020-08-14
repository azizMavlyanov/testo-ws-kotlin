package com.testows.model

import javax.validation.constraints.NotBlank

data class CategoryRequestModel(
        @NotBlank(message = "Category name cannot be neither null nor blank")
        private var categoryName: String,
        private var categoryImg: String = "default.png"
)