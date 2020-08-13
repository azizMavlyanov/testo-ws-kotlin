package com.testows.dao

import com.testows.entity.PurchaseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PurchaseRepository: JpaRepository<PurchaseEntity, Long> {
}