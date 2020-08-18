package com.testows.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.DynamicUpdate
import javax.persistence.*

@Entity(name = "user")
@DynamicUpdate
data class UserEntity(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "user_id")
        var userId: Long,
        @Column(nullable = false, name = "first_name")
        var firstName: String,
        @Column(name = "last_name")
        var lastName: String?,
        @Column(nullable = false, unique = true)
        var email: String,
        @field:JsonIgnore
        @Column(nullable= false, name = "encrypted_password")
        var encryptedPassword: String,
        @field:JsonIgnore
        @Column(name = "email_verification_token")
        var emailVerificationToken: String?,
        @field:JsonIgnore
        @Column(nullable= false, name = "email_verification_status")
        var emailVerificationStatus: Boolean = false
)