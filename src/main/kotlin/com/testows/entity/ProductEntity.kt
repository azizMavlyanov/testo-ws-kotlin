package com.testows.entity

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.DynamicUpdate
import java.time.LocalDateTime
import javax.persistence.*

@Entity(name = "product")
@DynamicUpdate
data class ProductEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "product_id")
        var productId: Long,
        @Column(nullable = false, name = "product_name")
        var productName: String,
        @Column(nullable = false, name = "product_price")
        var productPrice: Float,
        @Column(nullable = false)
        var active: Boolean,
        @Column(nullable = false, name = "product_image")
        var productImg: String = "default.png",
        @Column(nullable = false, name = "product_description")
        var productDescription: String = "No description",
        @CreationTimestamp
        @Column(name = "created_at")
        var createdAt: LocalDateTime = LocalDateTime.now(),
        @CreationTimestamp
        @Column(name = "updated_at")
        var updatedAt: LocalDateTime = LocalDateTime.now(),
        @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "category_id")
        var category: CategoryEntity?
)