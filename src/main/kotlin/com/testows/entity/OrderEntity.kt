package com.testows.entity

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity(name = "purchase")
@DynamicUpdate
class OrderEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "order_id")
        private var orderId: Long,
        @Column(nullable = false, name="order_status")
        private var orderStatus: String,
        @Column(nullable = false)
        private var address: String,
        @Transient
        private var totalPrice: String,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private var userEntity: UserEntity
)