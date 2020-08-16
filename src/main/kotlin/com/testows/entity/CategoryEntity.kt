package com.testows.entity

import lombok.NoArgsConstructor
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity(name = "category")
@DynamicUpdate
data class CategoryEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "category_id")
        var categoryId: Long,
        @Column(nullable = false, name="category_name", unique = true)
        var categoryName: String,
        @Column(nullable = false, name="category_image")
        var categoryImg: String = "default.jpg"
)