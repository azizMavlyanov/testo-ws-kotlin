package com.testows.entity

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity(name = "purchase_item")
@DynamicUpdate
data class PurchaseItemEntity (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "purchase_item_id")
        private var purchaseItemId: Long,
        @Column(nullable = false)
        private var quantity: Long,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        private var product: ProductEntity,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "purchase_id", nullable = false)
        private var purchase: PurchaseEntity
)