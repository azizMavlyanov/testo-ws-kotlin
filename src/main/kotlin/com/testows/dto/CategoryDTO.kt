package com.testows.dto

import java.io.Serializable

data class CategoryDTO(
        private var categoryId: Long,
        private var categoryName: String,
        private var categoryImg: String
)