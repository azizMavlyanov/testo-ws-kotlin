package com.testows.entity

import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity(name = "user")
@DynamicUpdate
class UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "user_id")
        private var userId: Long,
        @Column(nullable = false, name = "first_name")
        private var firstName: String,
        @Column(name = "last_name")
        private var lastName: String,
        @Column(nullable = false, unique = true)
        private var email: String,
        @Column(nullable= false, name = "encrypted_password")
        private var encryptedPassword: String,
        @Column(name = "email_verification_token")
        private var emailVerificationToken: String,
        @Column(nullable= false, name = "email_verification_status")
        private var emailVerificationStatus: Boolean = false
)