package com.testows.services.product

import com.testows.entities.ProductEntity
import com.testows.models.PageableAndSortableData
import com.testows.models.ProductRequestModel
import com.testows.models.ProductUpdateModel
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface ProductService {
    @Throws(Exception::class)
    fun create(productRequestModel: ProductRequestModel): ProductEntity
    @Throws(Exception::class)
    fun update(productId: Long, productUpdateModel: ProductUpdateModel): ProductEntity
    @Throws(Exception::class)
    fun findAll(page:Int, size:Int): PageableAndSortableData<ProductEntity>
    @Throws(Exception::class)
    fun findOne(productId: Long): ProductEntity
    @Throws(Exception::class)
    fun delete(productId: Long): Unit
    @Throws(Exception::class)
    fun uploadImage(productId: Long, file: MultipartFile): ProductEntity
    @Throws(Exception::class)
    fun loadImage(productId: Long, imageName: String): Resource?
}