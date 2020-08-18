package com.testows.services.category

import com.testows.entities.CategoryEntity
import com.testows.models.CategoryRequestModel
import com.testows.models.CategoryUpdateModel
import com.testows.models.PageableAndSortableData
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface CategoryService {
    @Throws(Exception::class)
    fun create(categoryRequestModel: CategoryRequestModel): CategoryEntity
    @Throws(Exception::class)
    fun update(categoryId:Long, categoryUpdateModel: CategoryUpdateModel): CategoryEntity
    @Throws(Exception::class)
    fun findAll(page:Int, size:Int): PageableAndSortableData<CategoryEntity>
    @Throws(Exception::class)
    fun findOne(categoryId:Long): CategoryEntity
    @Throws(Exception::class)
    fun delete(categoryId:Long): Unit
    @Throws(Exception::class)
    fun uploadImage(categoryId: Long, file: MultipartFile): CategoryEntity
    @Throws(Exception::class)
    fun loadImage(categoryId: Long, imageName: String): Resource?
}