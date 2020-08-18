package com.testows.services.purchase

import com.testows.entities.PurchaseEntity
import com.testows.entities.PurchaseItemEntity
import com.testows.models.PageableAndSortableData
import com.testows.models.PurchaseRequestModel
import com.testows.models.PurchaseUpdateModel

interface PurchaseService {
    @Throws(Exception::class)
    fun create(userId: Long, purchaseRequestModel: PurchaseRequestModel): PurchaseEntity
    @Throws(Exception::class)
    fun update(userId: Long, purchaseId: Long, purchaseUpdateModel: PurchaseUpdateModel): PurchaseEntity
    @Throws(Exception::class)
    fun findOne(userId: Long, purchaseId: Long): PurchaseEntity
    @Throws(Exception::class)
    fun findPurchaseItems(userId: Long, purchaseId: Long, page: Int, size: Int): PageableAndSortableData<PurchaseItemEntity>
}