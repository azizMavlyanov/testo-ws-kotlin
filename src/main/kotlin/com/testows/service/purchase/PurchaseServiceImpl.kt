package com.testows.service.purchase

import com.testows.dao.PurchaseItemRepository
import com.testows.dao.PurchaseRepository
import com.testows.entity.PurchaseEntity
import com.testows.entity.PurchaseItemEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.PurchaseRequestModel
import com.testows.model.PurchaseUpdateModel
import com.testows.service.product.ProductService
import com.testows.service.user.UserService
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class PurchaseServiceImpl(private val purchaseRepository: PurchaseRepository,
                          private val purchaseItemRepository: PurchaseItemRepository,
                          private val userService: UserService,
                          private val productService: ProductService) : PurchaseService {
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
        val savedPurchaseEntity = purchaseRepository.save(purchaseEntity)
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

        purchaseItemRepository.saveAll(purchaseItemList)

        savedPurchaseEntity.totalPrice = totalPrice

        return savedPurchaseEntity
    }

    override fun update(userId: Long, purchaseId: Long, purchaseUpdateModel: PurchaseUpdateModel): PurchaseEntity {
        userService.finOne(userId)

        val purchaseEntity = this.findOne(userId, purchaseId)
        val purchaseToUpdate = purchaseEntity.copy(
                purchaseStatus = purchaseUpdateModel.purchaseStatus?:purchaseEntity.purchaseStatus,
                address = purchaseUpdateModel.address?:purchaseEntity.address
        )

        return purchaseRepository.save(purchaseToUpdate)
    }

    override fun findOne(userId: Long, purchaseId: Long): PurchaseEntity {
        userService.finOne(userId)
        return purchaseRepository.findById(purchaseId).orElseThrow { Error("Purchase not found") }
    }

    override fun findPurchaseItems(userId: Long, purchaseId: Long, page: Int, size: Int): PageableAndSortableData<PurchaseItemEntity> {
        userService.finOne(userId)

        val purchaseEntity = this.findOne(userId, purchaseId)

        val pageableRequest: Pageable = PageRequest.of(if (page != 0) { page - 1 } else page, size)
        val purchaseItemList = purchaseItemRepository.findByPurchase(purchaseEntity, pageableRequest)

        return PageableAndSortableData(
                page = purchaseItemList.pageable.pageNumber + 1,
                size = purchaseItemList.pageable.pageSize,
                hasPrevious = purchaseItemList.hasPrevious(),
                hasNext = purchaseItemList.hasNext(),
                totalElements = purchaseItemList.totalElements,
                sort = purchaseItemList.sort.toString(),
                data = purchaseItemList.content
        )
    }
}