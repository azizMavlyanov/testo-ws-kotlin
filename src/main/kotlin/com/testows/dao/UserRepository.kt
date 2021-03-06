package com.testows.dao

import com.testows.entities.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<UserEntity, Long> {
    fun findByEmail(email: String?): UserEntity?
    fun findByEmailVerificationToken(token: String): UserEntity?
}