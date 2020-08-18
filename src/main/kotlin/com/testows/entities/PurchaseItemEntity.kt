package com.testows.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity(name = "purchase_item")
@DynamicUpdate
data class PurchaseItemEntity (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "purchase_item_id")
        var purchaseItemId: Long,
        @Column(nullable = false)
        var quantity: Long,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id", nullable = false)
        var product: ProductEntity,
        @field:JsonIgnore
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "purchase_id", nullable = false)
        var purchase: PurchaseEntity
)