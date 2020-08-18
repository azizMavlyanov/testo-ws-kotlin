package com.testows.models

data class ProductUpdateModel(
        var productName: String?,
        var productPrice: Float?,
        var active: Boolean?,
        var productImg: String?,
        var productDescription: String?,
        var categoryId: Long?
)