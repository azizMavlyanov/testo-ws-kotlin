package com.testows.service.user

import com.testows.entity.UserEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.UserRequestModel
import com.testows.model.UserUpdateModel
import org.springframework.security.core.userdetails.UserDetailsService

interface UserService: UserDetailsService {
    fun create(userRequestModel: UserRequestModel): UserEntity
    fun update(userId: Long, userUpdateModel: UserUpdateModel): UserEntity
    fun finOne(userId: Long): UserEntity
    fun findAll(page: Int, size: Int): PageableAndSortableData<UserEntity>
}