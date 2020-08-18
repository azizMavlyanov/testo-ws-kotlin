package com.testows.entities

import com.testows.models.PurchaseStatus
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*
import javax.validation.constraints.Pattern

@Entity(name = "purchase")
@DynamicUpdate
data class PurchaseEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "purchase_id")
        var purchaseId: Long,
        @field:Pattern(regexp = "(open|pending|closed|rejected)", message = "Invalid status value")
        @Column(nullable = false, name="order_status")
        var purchaseStatus: String = PurchaseStatus.OPEN.status,
        @Column(nullable = false)
        var address: String,
        @Transient
        var totalPrice: Float?,
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        var user: UserEntity
)