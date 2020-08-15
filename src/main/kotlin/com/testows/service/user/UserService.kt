package com.testows.service.user

import com.testows.entity.UserEntity
import com.testows.model.UserRequestModel

interface UserService {
    fun create(userRequestModel: UserRequestModel): UserEntity
    fun finOne(userId: Long): UserEntity
}