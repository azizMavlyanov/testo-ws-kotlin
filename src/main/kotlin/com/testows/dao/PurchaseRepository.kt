package com.testows.dao

import com.testows.entity.PurchaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseRepository: JpaRepository<PurchaseEntity, Long> {
}