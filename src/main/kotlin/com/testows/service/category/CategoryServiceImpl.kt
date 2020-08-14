package com.testows.service.category

import com.testows.dao.CategoryRepository
import com.testows.entity.CategoryEntity
import com.testows.model.CategoryRequestModel
import com.testows.model.CategoryUpdateModel
import com.testows.model.PageableAndSortableData
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CategoryServiceImpl(private val categoryRepository: CategoryRepository) : CategoryService {

    override fun create(categoryRequestModel: CategoryRequestModel): CategoryEntity {
        val categoryEntity = CategoryEntity(0,
                categoryRequestModel.categoryName,
                categoryRequestModel.categoryImg)

        return categoryRepository.save(categoryEntity)
    }

    override fun findAll(page: Int, size: Int): PageableAndSortableData<CategoryEntity> {
        val pageableRequest: Pageable = PageRequest.of(if (page != 0) { page - 1 } else page, size)
        val categoryEntities = categoryRepository.findAll(pageableRequest)

        return PageableAndSortableData(
                page = categoryEntities.pageable.pageNumber + 1,
                size = categoryEntities.pageable.pageSize,
                hasPrevious = categoryEntities.hasPrevious(),
                hasNext = categoryEntities.hasNext(),
                totalElements = categoryEntities.totalElements,
                sort = categoryEntities.sort.toString(),
                data = categoryEntities.content
        )
    }

    override fun findOne(categoryId: Long): CategoryEntity {
        return categoryRepository.findById(categoryId).orElseThrow { Error("Category not found") }
    }

    override fun update(categoryId: Long, categoryUpdateModel: CategoryUpdateModel): CategoryEntity {
        val categoryEntity = this.findOne(categoryId)
        val categoryToSave = categoryEntity.copy(
                categoryName = categoryUpdateModel.categoryName ?: categoryEntity.categoryName,
                categoryImg = categoryUpdateModel.categoryImg ?: categoryEntity.categoryImg
        )

        return categoryRepository.save(categoryToSave)
    }

    override fun delete(categoryId: Long) {
        categoryRepository.deleteById(this.findOne(categoryId).categoryId)
    }
}