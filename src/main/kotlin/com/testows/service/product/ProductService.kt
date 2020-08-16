package com.testows.service.product

import com.testows.entity.CategoryEntity
import com.testows.entity.ProductEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.ProductRequestModel
import com.testows.model.ProductUpdateModel
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface ProductService {
    fun create(productRequestModel: ProductRequestModel): ProductEntity
    fun update(productId: Long, productUpdateModel: ProductUpdateModel): ProductEntity
    fun findAll(page:Int, size:Int): PageableAndSortableData<ProductEntity>
    fun findOne(productId: Long): ProductEntity
    fun delete(productId: Long): Unit
    fun uploadImage(productId: Long, file: MultipartFile): ProductEntity
    fun loadImage(productId: Long, imageName: String): Resource?
}