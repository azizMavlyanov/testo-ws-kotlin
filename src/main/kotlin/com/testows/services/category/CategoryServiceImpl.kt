package com.testows.services.category

import com.testows.dao.CategoryRepository
import com.testows.entities.CategoryEntity
import com.testows.exceptions.CommonServiceException
import com.testows.exceptions.ResourceAlreadyExistsException
import com.testows.exceptions.ResourceNotFoundException
import com.testows.models.CategoryRequestModel
import com.testows.models.CategoryUpdateModel
import com.testows.models.ErrorMessages
import com.testows.models.PageableAndSortableData
import com.testows.services.image.ImageService
import com.testows.utils.PaginationUtil
import org.springframework.core.io.Resource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
@Transactional
class CategoryServiceImpl(private val categoryRepository: CategoryRepository,
                          private val imageService: ImageService,
                          private val paginationUtil: PaginationUtil) : CategoryService {

    @Throws(Exception::class)
    override fun create(categoryRequestModel: CategoryRequestModel): CategoryEntity {
        if (categoryRepository.findByCategoryName(categoryRequestModel.categoryName) != null) {
            throw ResourceAlreadyExistsException(ErrorMessages.RECORD_ALREADY_EXISTS.errorMessage)
        }

        val categoryEntity = CategoryEntity(0, categoryRequestModel.categoryName)

        try {
            return categoryRepository.save(categoryEntity)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun findAll(page: Int, size: Int): PageableAndSortableData<CategoryEntity> {
        try {
            val categoriesList = categoryRepository
                    .findAll(paginationUtil.customPaginate(page, size))

            return PageableAndSortableData(
                    page = categoriesList.pageable.pageNumber + 1,
                    size = categoriesList.pageable.pageSize,
                    hasPrevious = categoriesList.hasPrevious(),
                    hasNext = categoriesList.hasNext(),
                    totalElements = categoriesList.totalElements,
                    sort = categoriesList.sort.toString(),
                    data = categoriesList.content
            )
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun findOne(categoryId: Long): CategoryEntity {
        return categoryRepository.findById(categoryId)
                .orElseThrow { ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.errorMessage) }
    }

    @Throws(Exception::class)
    override fun update(categoryId: Long, categoryUpdateModel: CategoryUpdateModel): CategoryEntity {
        val categoryEntity = this.findOne(categoryId)
        val categoryToSave = categoryEntity.copy(
                categoryName = categoryUpdateModel.categoryName ?: categoryEntity.categoryName,
                categoryImg = categoryUpdateModel.categoryImg ?: categoryEntity.categoryImg
        )

        try {
            return categoryRepository.save(categoryToSave)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun delete(categoryId: Long) {
        val categoryEntity = this.findOne(categoryId)

        try {
            categoryRepository.deleteById(categoryEntity.categoryId)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun uploadImage(categoryId: Long, file: MultipartFile): CategoryEntity {
        val categoryEntity = this.findOne(categoryId)

        return this.update(categoryEntity.categoryId, CategoryUpdateModel(
                categoryName = null,
                categoryImg = imageService.upload("categories", file, 70, 70)
        ))

    }

    @Throws(Exception::class)
    override fun loadImage(categoryId: Long, imageName: String): Resource? {
        this.findOne(categoryId)

        return imageService.load("categories", imageName)

    }
}