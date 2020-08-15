package com.testows.dao

import com.testows.entity.PurchaseEntity
import com.testows.entity.PurchaseItemEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseItemRepository: JpaRepository<PurchaseItemEntity, Long> {
    fun findByPurchase(purchaseEntity: PurchaseEntity, pageable: Pageable): Page<PurchaseItemEntity>
}