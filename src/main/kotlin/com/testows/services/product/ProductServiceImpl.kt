package com.testows.services.product

import com.testows.dao.ProductRepository
import com.testows.entities.ProductEntity
import com.testows.exceptions.CommonServiceException
import com.testows.exceptions.ResourceNotFoundException
import com.testows.models.ErrorMessages
import com.testows.models.PageableAndSortableData
import com.testows.models.ProductRequestModel
import com.testows.models.ProductUpdateModel
import com.testows.services.category.CategoryService
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
class ProductServiceImpl(private val productRepository: ProductRepository,
                         private val categoryService: CategoryService,
                         private val imageService: ImageService,
                         private val paginationUtil: PaginationUtil) : ProductService {

    @Throws(Exception::class)
    override fun create(productRequestModel: ProductRequestModel): ProductEntity {
        val categoryEntity = productRequestModel.categoryId?.let {
            categoryService.findOne(it)
        }

        val productEntity = ProductEntity(
                productId = 0,
                productName = productRequestModel.productName,
                productPrice = productRequestModel.productPrice,
                active = productRequestModel.active,
                productDescription = productRequestModel.productDescription,
                category = categoryEntity
        )

        try {
            return productRepository.save(productEntity)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun update(productId: Long, productUpdateModel: ProductUpdateModel): ProductEntity {
        val categoryEntity = productUpdateModel.categoryId?.let {
            categoryService.findOne(it)
        }

        val productEntity = this.findOne(productId)
        val productToSave = productEntity.copy(
                productName = productUpdateModel.productName ?: productEntity.productName,
                productPrice = productUpdateModel.productPrice ?: productEntity.productPrice,
                active = productUpdateModel.active ?: productEntity.active,
                productImg = productUpdateModel.productImg ?: productEntity.productImg,
                productDescription = productUpdateModel.productDescription ?: productEntity.productDescription,
                category = categoryEntity ?: productEntity.category
        )

        try {
            return productRepository.save(productToSave)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun findAll(page: Int, size: Int): PageableAndSortableData<ProductEntity> {
        val productEntities = try {
            productRepository.findAll(paginationUtil.customPaginate(page, size))
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }

        return PageableAndSortableData(
                page = productEntities.pageable.pageNumber + 1,
                size = productEntities.pageable.pageSize,
                hasPrevious = productEntities.hasPrevious(),
                hasNext = productEntities.hasNext(),
                totalElements = productEntities.totalElements,
                sort = productEntities.sort.toString(),
                data = productEntities.content
        )
    }

    @Throws(Exception::class)
    override fun findOne(productId: Long): ProductEntity {
        return productRepository.findById(productId)
                .orElseThrow { ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.errorMessage) }
    }

    @Throws(Exception::class)
    override fun delete(productId: Long) {
        val productEntity = this.findOne(productId)

        try {
            productRepository.deleteById(productEntity.productId)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun uploadImage(productId: Long, file: MultipartFile): ProductEntity {
        val productEntity = this.findOne(productId)

        return this.update(productId, ProductUpdateModel(
                productName = null,
                productPrice = null,
                productImg = imageService.upload("products", file, 150, 150),
                active = null,
                productDescription = null,
                categoryId = null

        ))
    }

    @Throws(Exception::class)
    override fun loadImage(productId: Long, imageName: String): Resource? {
        this.findOne(productId)

        return imageService.load("products", imageName)
    }
}