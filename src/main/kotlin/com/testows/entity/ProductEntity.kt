package com.testows.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "product")
@DynamicUpdate
class ProductEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "product_id")
        private var productId: Long,
        @Column(nullable = false, name = "product_name")
        private var productName: String,
        @Column(nullable = false, name = "product_price")
        private var productPrice: Float,
        @Column(nullable = false)
        private var active: Boolean,
        @Column(nullable = false, name = "product_image")
        private var productImg: String = "default.png",
        @Column(nullable = false, name = "product_description")
        private var productDescription: String = "No description",
        @CreationTimestamp
        @Column(name = "created_at")
        private var createdAt: LocalDateTime,
        @CreationTimestamp
        @Column(name = "updated_at")
        private var updatedAt: LocalDateTime,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "category_id", nullable = false")
        private var category: CategoryEntity
)