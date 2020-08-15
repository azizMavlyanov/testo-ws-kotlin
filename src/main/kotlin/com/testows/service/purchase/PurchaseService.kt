package com.testows.service.purchase

import com.testows.entity.PurchaseEntity
import com.testows.entity.PurchaseItemEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.PurchaseRequestModel
import com.testows.model.PurchaseUpdateModel

interface PurchaseService {
    fun create(userId: Long, purchaseRequestModel: PurchaseRequestModel): PurchaseEntity
    fun update(userId: Long, purchaseId: Long, purchaseUpdateModel: PurchaseUpdateModel): PurchaseEntity
    fun findOne(userId: Long, purchaseId: Long): PurchaseEntity
    fun findPurchaseItems(userId: Long, purchaseId: Long, page: Int, size: Int): PageableAndSortableData<PurchaseItemEntity>
}