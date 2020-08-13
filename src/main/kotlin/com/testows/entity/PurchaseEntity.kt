package com.testows.entity

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity(name = "purchase")
@DynamicUpdate
class PurchaseEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "purchase_id")
        private var purchaseId: Long,
        @Column(nullable = false, name="order_status")
        private var purchaseStatus: String,
        @Column(nullable = false)
        private var address: String,
        @Transient
        private var totalPrice: String,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private var user: UserEntity
)