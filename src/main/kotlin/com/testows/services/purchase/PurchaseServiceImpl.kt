package com.testows.services.purchase

import com.testows.dao.PurchaseItemRepository
import com.testows.dao.PurchaseRepository
import com.testows.entities.PurchaseEntity
import com.testows.entities.PurchaseItemEntity
import com.testows.exceptions.CommonServiceException
import com.testows.exceptions.ResourceNotFoundException
import com.testows.models.ErrorMessages
import com.testows.models.PageableAndSortableData
import com.testows.models.PurchaseRequestModel
import com.testows.models.PurchaseUpdateModel
import com.testows.services.product.ProductService
import com.testows.services.user.UserService
import com.testows.utils.PaginationUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PurchaseServiceImpl(private val purchaseRepository: PurchaseRepository,
                          private val purchaseItemRepository: PurchaseItemRepository,
                          private val userService: UserService,
                          private val productService: ProductService,
                          private val paginationUtil: PaginationUtil) : PurchaseService {
    @Throws(Exception::class)
    override fun create(userId: Long, purchaseRequestModel: PurchaseRequestModel): PurchaseEntity {
        val userEntity = userService.finOne(userId)

        var totalPrice = 0f

        val purchaseEntity = PurchaseEntity(
                purchaseId = 0,
                purchaseStatus = purchaseRequestModel.status,
                address = purchaseRequestModel.address,
                totalPrice = totalPrice,
                user = userEntity
        )
        val savedPurchaseEntity = try {
            purchaseRepository.save(purchaseEntity)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
        val purchaseItemList = mutableListOf<PurchaseItemEntity>()

        purchaseRequestModel.purchaseItems.forEach {
            purchaseItemList.add(PurchaseItemEntity(
                    purchaseItemId = 0,
                    quantity = it.quantity,
                    product = productService.findOne(it.productId),
                    purchase = savedPurchaseEntity
            ))
        }

        purchaseItemList.forEach {
            totalPrice += it.quantity.toFloat() * it.product.productPrice
        }

        try {
            purchaseItemRepository.saveAll(purchaseItemList)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }

        savedPurchaseEntity.totalPrice = totalPrice

        return savedPurchaseEntity
    }

    @Throws(Exception::class)
    override fun update(userId: Long, purchaseId: Long, purchaseUpdateModel: PurchaseUpdateModel): PurchaseEntity {
        userService.finOne(userId)

        val purchaseEntity = this.findOne(userId, purchaseId)
        val purchaseToUpdate = purchaseEntity.copy(
                purchaseStatus = purchaseUpdateModel.purchaseStatus ?: purchaseEntity.purchaseStatus,
                address = purchaseUpdateModel.address ?: purchaseEntity.address
        )

        try {
            return purchaseRepository.save(purchaseToUpdate)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun findOne(userId: Long, purchaseId: Long): PurchaseEntity {
        userService.finOne(userId)
        return purchaseRepository.findById(purchaseId)
                .orElseThrow { ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.errorMessage) }
    }

    @Throws(Exception::class)
    override fun findPurchaseItems(userId: Long, purchaseId: Long, page: Int, size: Int): PageableAndSortableData<PurchaseItemEntity> {
        userService.finOne(userId)

        val purchaseEntity = this.findOne(userId, purchaseId)

        val purchaseItemsList = try {
            purchaseItemRepository.findByPurchase(purchaseEntity, paginationUtil.customPaginate(page, size))
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }

        return PageableAndSortableData(
                page = purchaseItemsList.pageable.pageNumber + 1,
                size = purchaseItemsList.pageable.pageSize,
                hasPrevious = purchaseItemsList.hasPrevious(),
                hasNext = purchaseItemsList.hasNext(),
                totalElements = purchaseItemsList.totalElements,
                sort = purchaseItemsList.sort.toString(),
                data = purchaseItemsList.content
        )
    }
}