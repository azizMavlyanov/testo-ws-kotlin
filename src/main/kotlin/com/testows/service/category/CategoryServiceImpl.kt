package com.testows.service.category

import com.testows.dao.CategoryRepository
import com.testows.entity.CategoryEntity
import com.testows.model.CategoryRequestModel
import com.testows.model.CategoryUpdateModel
import com.testows.model.PageableAndSortableData
import com.testows.service.image.ImageService
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.name.Rename
import org.springframework.core.io.Resource
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

@Service
@Transactional
class CategoryServiceImpl(private val categoryRepository: CategoryRepository,
                          private val imageService: ImageService) : CategoryService {

    override fun create(categoryRequestModel: CategoryRequestModel): CategoryEntity {
        val categoryEntity = CategoryEntity(0, categoryRequestModel.categoryName)

        return categoryRepository.save(categoryEntity)
    }

    override fun findAll(page: Int, size: Int): PageableAndSortableData<CategoryEntity> {
        val pageableRequest: Pageable = PageRequest.of(if (page != 0) {
            page - 1
        } else page, size)
        val categoriesList = categoryRepository.findAll(pageableRequest)

        return PageableAndSortableData(
                page = categoriesList.pageable.pageNumber + 1,
                size = categoriesList.pageable.pageSize,
                hasPrevious = categoriesList.hasPrevious(),
                hasNext = categoriesList.hasNext(),
                totalElements = categoriesList.totalElements,
                sort = categoriesList.sort.toString(),
                data = categoriesList.content
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

    override fun uploadImage(categoryId: Long, file: MultipartFile): CategoryEntity {
        val categoryEntity = this.findOne(categoryId)

        return this.update(categoryEntity.categoryId, CategoryUpdateModel(
                categoryName = null,
                categoryImg = imageService.upload("categories", file, 70, 70)
        ))
    }

    override fun loadImage(categoryId: Long, imageName: String): Resource? {
        this.findOne(categoryId)

        return imageService.load("categories", imageName)
    }
}