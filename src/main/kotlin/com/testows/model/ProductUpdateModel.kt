package com.testows.model

import com.testows.entity.CategoryEntity
import java.time.LocalDateTime

data class ProductUpdateModel(
        var productName: String?,
        var productPrice: Float?,
        var active: Boolean?,
        var productImg: String?,
        var productDescription: String?,
        var categoryId: Long?
)