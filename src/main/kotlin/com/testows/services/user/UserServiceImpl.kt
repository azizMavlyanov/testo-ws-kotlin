package com.testows.services.user

import com.testows.dao.PasswordResetTokenRepository
import com.testows.dao.UserRepository
import com.testows.entities.PasswordResetTokenEntity
import com.testows.entities.UserEntity
import com.testows.exceptions.BadRequestException
import com.testows.exceptions.CommonServiceException
import com.testows.exceptions.ResourceAlreadyExistsException
import com.testows.exceptions.ResourceNotFoundException
import com.testows.models.*
import com.testows.services.amazon.AmazonSES
import com.testows.utils.PaginationUtil
import com.testows.utils.Utils
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(private val userRepository: UserRepository,
                      private val passwordResetTokenRepository: PasswordResetTokenRepository,
                      private val bCryptPasswordEncoder: BCryptPasswordEncoder,
                      private val utils: Utils,
                      private val paginationUtil: PaginationUtil) : UserService {
    @Throws(Exception::class)
    override fun create(userRequestModel: UserRequestModel): UserEntity {
        userRepository.findByEmail(userRequestModel.email)?.let {
            throw ResourceAlreadyExistsException(ErrorMessages.RECORD_ALREADY_EXISTS.errorMessage)
        }

        try {
            val userEntity = userRepository.save(UserEntity(
                    userId = 0,
                    firstName = userRequestModel.firstName,
                    lastName = userRequestModel.lastName,
                    email = userRequestModel.email,
                    encryptedPassword = bCryptPasswordEncoder.encode(userRequestModel.password),
                    emailVerificationToken = utils.generateEmailVerificationToken(userRequestModel.email),
                    emailVerificationStatus = false
            ))

            // Send message to user's email to verify one
            AmazonSES.verifyEmail(userEntity)

            return userEntity
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

        return User(userEntity.email, userEntity.encryptedPassword, userEntity.emailVerificationStatus,
                true, true, true, ArrayList())
    }

    @Throws(Exception::class)
    override fun verifyEmailToken(token: String): Boolean {
        var returnValue = false

        userRepository.findByEmailVerificationToken(token)?.let {
            val hasTokenExpired = utils.hasTokenExpired(token)
            if (hasTokenExpired.not()) {
                it.emailVerificationToken = null
                it.emailVerificationStatus = true
                try {
                    userRepository.save(it)
                } catch (e: Exception) {
                    throw CommonServiceException(e.localizedMessage)
                }
                returnValue = true
            } else {
                throw BadRequestException(ErrorMessages.TOKEN_HAS_EXPIRED.errorMessage)
            }
        }

        return returnValue
    }

    @Throws(Exception::class)
    override fun requestPasswordReset(email: String): TokenResponseModel {
        val returnValue = false
        val userEntity = userRepository
                .findByEmail(email) ?: throw ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.errorMessage)
        val token = utils.generatePasswordResetToken(userEntity.email)

        try {
            passwordResetTokenRepository.save(PasswordResetTokenEntity(0, token, userEntity))
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }

        AmazonSES.resetPassword(userEntity, token)

        return TokenResponseModel(token)
    }

    @Throws(Exception::class)
    override fun resetPassword(passwordResetModel: PasswordResetModel): Boolean {
        val (token, password) = passwordResetModel
        var returnValue = false

        if (utils.hasTokenExpired(token)) {
            throw BadRequestException(ErrorMessages.TOKEN_HAS_EXPIRED.errorMessage)
        }

        val passwordResetTokenEntity = passwordResetTokenRepository.findByToken(token)
                ?: throw ResourceNotFoundException(ErrorMessages.NO_RECORD_FOUND.errorMessage)

        val encodedPassword = bCryptPasswordEncoder.encode(password)

        val userEntity = passwordResetTokenEntity.userEntity
        userEntity.encryptedPassword = encodedPassword

        try {
            userRepository.save(userEntity)
            returnValue = true
            passwordResetTokenRepository.delete(passwordResetTokenEntity)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }

        return returnValue
    }
}