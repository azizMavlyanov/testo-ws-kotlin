package com.testows.dao

import com.testows.entities.PurchaseEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PurchaseRepository: JpaRepository<PurchaseEntity, Long> {
}