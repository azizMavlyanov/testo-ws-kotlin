package com.testows.services.user

import com.testows.entities.UserEntity
import com.testows.models.*
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService: UserDetailsService {
    @Throws(Exception::class)
    fun create(userRequestModel: UserRequestModel): UserEntity
    @Throws(Exception::class)
    fun update(userId: Long, userUpdateModel: UserUpdateModel): UserEntity
    @Throws(Exception::class)
    fun finOne(userId: Long): UserEntity
    @Throws(Exception::class)
    fun findAll(page: Int, size: Int): PageableAndSortableData<UserEntity>
    @Throws(Exception::class)
    fun verifyEmailToken(token: String): Boolean
    @Throws(Exception::class)
    fun requestPasswordReset(email: String): TokenResponseModel
    @Throws(Exception::class)
    fun resetPassword(passwordResetModel: PasswordResetModel): Boolean
}