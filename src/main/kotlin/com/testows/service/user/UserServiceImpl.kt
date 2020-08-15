package com.testows.service.user

import com.testows.dao.UserRepository
import com.testows.entity.UserEntity
import com.testows.model.UserRequestModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(private val userRepository: UserRepository): UserService {
    override fun create(userRequestModel: UserRequestModel): UserEntity {
        userRepository.findByEmail(userRequestModel.email)?.let {
            throw Error("User with provided email (${it.email}) already exists")
        }

        return userRepository.save(UserEntity(
                userId = 0,
                firstName = userRequestModel.firstName,
                lastName = userRequestModel.lastName,
                email = userRequestModel.email,
                encryptedPassword = userRequestModel.password,
                emailVerificationToken = null,
                emailVerificationStatus = false
        ))
    }

    override fun finOne(userId: Long): UserEntity {
        return userRepository.findById(userId).orElseThrow { Error("User not found") }
    }
}