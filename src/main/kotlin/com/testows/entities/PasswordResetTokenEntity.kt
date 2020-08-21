package com.testows.entities

import javax.persistence.*

@Entity(name = "password_reset_token")
data class PasswordResetTokenEntity (
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "password_reset_token_id")
        var passwordResetTokenId: Long,
        var token: String,
        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        var userEntity: UserEntity
)