package com.testows.service.category

import com.testows.entity.CategoryEntity
import com.testows.model.CategoryRequestModel
import com.testows.model.CategoryUpdateModel
import com.testows.model.PageableAndSortableData
import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface CategoryService {
    fun create(categoryRequestModel: CategoryRequestModel): CategoryEntity
    fun update(categoryId:Long, categoryUpdateModel: CategoryUpdateModel): CategoryEntity
    fun findAll(page:Int, size:Int): PageableAndSortableData<CategoryEntity>
    fun findOne(categoryId:Long): CategoryEntity
    fun delete(categoryId:Long): Unit
    fun uploadImage(categoryId: Long, file: MultipartFile): CategoryEntity
    fun loadImage(categoryId: Long, imageName: String): Resource?
}