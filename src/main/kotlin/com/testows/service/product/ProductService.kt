package com.testows.service.product

import com.testows.entity.ProductEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.ProductRequestModel
import com.testows.model.ProductUpdateModel

interface ProductService {
    fun create(productRequestModel: ProductRequestModel): ProductEntity
    fun update(productId: Long, productUpdateModel: ProductUpdateModel): ProductEntity
    fun findAll(page:Int, size:Int): PageableAndSortableData<ProductEntity>
    fun findOne(productId: Long): ProductEntity
    fun delete(productId: Long): Unit
}