package com.testows.service.user

import com.testows.dao.UserRepository
import com.testows.entity.UserEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.UserRequestModel
import com.testows.model.UserUpdateModel
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(private val userRepository: UserRepository,
                      private val bCryptPasswordEncoder: BCryptPasswordEncoder) : UserService {
    override fun create(userRequestModel: UserRequestModel): UserEntity {
        userRepository.findByEmail(userRequestModel.email)?.let {
            throw Error("User with provided email (${it.email}) already exists")
        }

        return userRepository.save(UserEntity(
                userId = 0,
                firstName = userRequestModel.firstName,
                lastName = userRequestModel.lastName,
                email = userRequestModel.email,
                encryptedPassword = bCryptPasswordEncoder.encode(userRequestModel.password),
                emailVerificationToken = null,
                emailVerificationStatus = false
        ))
    }

    override fun update(userId: Long, userUpdateModel: UserUpdateModel): UserEntity {
        val userEntity = this.finOne(userId)
        val userToSave = userEntity.copy(
                firstName = userUpdateModel.firstName ?: userEntity.firstName,
                lastName = userUpdateModel.lastName ?: userEntity.lastName
        )

        return userRepository.save(userToSave)
    }

    override fun finOne(userId: Long): UserEntity {
        return userRepository.findById(userId).orElseThrow { Error("User not found") }
    }

    override fun findAll(page: Int, size: Int): PageableAndSortableData<UserEntity> {
        val pageableRequest: Pageable = PageRequest.of(if (page != 0) { page - 1 } else page, size)
        val usersList = userRepository.findAll(pageableRequest)

        return PageableAndSortableData(
                page = usersList.pageable.pageNumber + 1,
                size = usersList.pageable.pageSize,
                hasPrevious = usersList.hasPrevious(),
                hasNext = usersList.hasNext(),
                totalElements = usersList.totalElements,
                sort = usersList.sort.toString(),
                data = usersList.content
        )
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(email: String?): UserDetails {
        val userEntity = userRepository.findByEmail(email) ?: throw UsernameNotFoundException(email)

        return User(userEntity.email, userEntity.encryptedPassword, true,
                true, true, true, ArrayList())
    }
}