package com.testows.dao

import com.testows.entities.CategoryEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository: JpaRepository<CategoryEntity, Long> {
    fun findByCategoryName(categoryName: String): CategoryEntity?
}