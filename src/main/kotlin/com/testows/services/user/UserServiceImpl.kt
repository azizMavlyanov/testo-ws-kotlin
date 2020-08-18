package com.testows.services.user

import com.testows.dao.UserRepository
import com.testows.entities.UserEntity
import com.testows.exceptions.CommonServiceException
import com.testows.exceptions.ResourceAlreadyExistsException
import com.testows.exceptions.ResourceNotFoundException
import com.testows.models.ErrorMessages
import com.testows.models.PageableAndSortableData
import com.testows.models.UserRequestModel
import com.testows.models.UserUpdateModel
import com.testows.utils.PaginationUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(private val userRepository: UserRepository,
                      private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                      private val paginationUtil: PaginationUtil) : UserService {
    @Throws(Exception::class)
    override fun create(userRequestModel: UserRequestModel): UserEntity {
        userRepository.findByEmail(userRequestModel.email)?.let {
            throw ResourceAlreadyExistsException(ErrorMessages.RECORD_ALREADY_EXISTS.errorMessage)
        }

        try {
            return userRepository.save(UserEntity(
                    userId = 0,
                    firstName = userRequestModel.firstName,
                    lastName = userRequestModel.lastName,
                    email = userRequestModel.email,
                    encryptedPassword = bCryptPasswordEncoder.encode(userRequestModel.password),
                    emailVerificationToken = null,
                    emailVerificationStatus = false
            ))
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun update(userId: Long, userUpdateModel: UserUpdateModel): UserEntity {
        val userEntity = this.finOne(userId)
        val userToSave = userEntity.copy(
                firstName = userUpdateModel.firstName ?: userEntity.firstName,
                lastName = userUpdateModel.lastName ?: userEntity.lastName
        )

        try {
            return userRepository.save(userToSave)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }

    @Throws(Exception::class)
    override fun finOne(userId: Long): UserEntity {
        return userRepository.findById(userId)
                .orElseThrow { ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.errorMessage) }
    }

    @Throws(Exception::class)
    override fun findAll(page: Int, size: Int): PageableAndSortableData<UserEntity> {
        val usersList = try {
            userRepository.findAll(paginationUtil.customPaginate(page, size))
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }

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

    @Throws(ResourceNotFoundException::class)
    override fun loadUserByUsername(email: String?): UserDetails {
        val userEntity = userRepository
                .findByEmail(email) ?: throw ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.errorMessage)

        return User(userEntity.email, userEntity.encryptedPassword, true,
                true, true, true, ArrayList())
    }
}