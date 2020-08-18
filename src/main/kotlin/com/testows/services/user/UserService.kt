package com.testows.services.user

import com.testows.entities.UserEntity
import com.testows.models.PageableAndSortableData
import com.testows.models.UserRequestModel
import com.testows.models.UserUpdateModel
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
}