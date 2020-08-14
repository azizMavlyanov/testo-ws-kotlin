package com.testows.service.product

import com.testows.dao.ProductRepository
import com.testows.entity.ProductEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.ProductRequestModel
import com.testows.model.ProductUpdateModel
import com.testows.service.category.CategoryService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ProductServiceImpl(private val productRepository: ProductRepository,
                         private val categoryService: CategoryService): ProductService {
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

        return productRepository.save(productEntity)
    }

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
                 category = categoryEntity?: productEntity.category
        )

        return productRepository.save(productToSave)
    }

    override fun findAll(page: Int, size: Int): PageableAndSortableData<ProductEntity> {
        val pageableRequest: Pageable = PageRequest.of(if (page != 0) { page - 1 } else page, size)
        val productEntities = productRepository.findAll(pageableRequest)

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

    override fun findOne(productId: Long): ProductEntity {
        return productRepository.findById(productId).orElseThrow { Error("Product not found") }
    }

    override fun delete(productId: Long) {
        productRepository.deleteById(this.findOne(productId).productId)
    }
}