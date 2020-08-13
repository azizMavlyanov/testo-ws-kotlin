package com.testows.entity

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity(name = "category")
@DynamicUpdate
class CategoryEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "category_id")
        private var categoryId: Long,
        @Column(nullable = false, name="category_name", unique = true)
        private var categoryName: String,
        @Column(nullable = false, name="category_image")
        private var categoryImg: String = "default.png"
)