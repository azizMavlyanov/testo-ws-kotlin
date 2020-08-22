package com.testows.controllers

import com.testows.entities.PurchaseEntity
import com.testows.entities.PurchaseItemEntity
import com.testows.entities.UserEntity
import com.testows.models.*
import com.testows.services.purchase.PurchaseService
import com.testows.services.user.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@RequestMapping(value = ["/api/v1/users"])
@Validated
@CrossOrigin
class UserController(private val userService: UserService, private val purchaseService: PurchaseService) {
    @Throws(Exception::class)
    @PostMapping(
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]

    )
    fun create(@Valid @RequestBody userRequestModel: UserRequestModel): ResponseEntity<UserEntity> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userRequestModel))
    }

    @Throws(Exception::class)
    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    fun findAll(
            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = "page must be greater than 0")
            page: Int,
            @RequestParam(value = "size", defaultValue = "1")
            @Min(value = 1, message = "size must be greater than 0")
            size: Int
    ): ResponseEntity<PageableAndSortableData<UserEntity>> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(page, size))
    }

    @Throws(Exception::class)
    @GetMapping(
            value = ["/email-verification-request"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    fun verifyEmailToken(@RequestParam(value = "token")
                         token: String): ResponseEntity<UserEntity> {
        userService.verifyEmailToken(token)

        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Throws(Exception::class)
    @PostMapping(
            value = ["/password-reset-request"],
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun requestPasswordReset(
            @Valid @RequestBody passwordResetRequestModel: PasswordResetRequestModel
    ): ResponseEntity<TokenResponseModel> = ResponseEntity
            .status(HttpStatus.OK)
            .body(userService.requestPasswordReset(passwordResetRequestModel.email))


    @Throws(Exception::class)
    @PostMapping(
            value = ["/password-reset"],
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun resetPassword(@Valid @RequestBody passwordResetModel: PasswordResetModel): ResponseEntity<UserEntity> {
        userService.resetPassword(passwordResetModel)

        return ResponseEntity.status(HttpStatus.OK).build()
    }

    @Throws(Exception::class)
    @GetMapping(
            value = ["/{userId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun findOne(@Min(value = 1, message = "User ID must be greater than 0")
                @PathVariable(value = "userId") userId: Long): ResponseEntity<UserEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.finOne(userId))
    }

    @Throws(Exception::class)
    @PatchMapping(
            value = ["/{userId}"],
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun update(@Min(value = 1, message = "User ID must be greater than 0")
               @PathVariable(value = "userId") userId: Long,
               @Valid @RequestBody userUpdateModel: UserUpdateModel): ResponseEntity<UserEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(userId, userUpdateModel))
    }

    @Throws(Exception::class)
    @PostMapping(
            value = ["/{userId}/purchases"],
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]

    )
    fun createPurchase(@Valid @RequestBody purchaseRequestModel: PurchaseRequestModel,
                       @Min(value = 1, message = "User ID must be greater than 0")
                       @PathVariable(value = "userId") userId: Long): ResponseEntity<PurchaseEntity> {
        return ResponseEntity.status(HttpStatus.CREATED).body(purchaseService.create(userId, purchaseRequestModel))
    }

    @Throws(Exception::class)
    @GetMapping(
            value = ["/{userId}/purchases/{purchaseId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun findPurchase(@Min(value = 1, message = "User ID must be greater than 0")
                     @PathVariable(value = "userId") userId: Long,
                     @Min(value = 1, message = "Purchase ID must be greater than 0")
                     @PathVariable(value = "purchaseId") purchaseId: Long): ResponseEntity<PurchaseEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService.findOne(userId, purchaseId))
    }

    @Throws(Exception::class)
    @PatchMapping(
            value = ["/{userId}/purchases/{purchaseId}"],
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun updatePurchase(@Min(value = 1, message = "User ID must be greater than 0")
                       @PathVariable(value = "userId") userId: Long,
                       @Min(value = 1, message = "Purchase ID must be greater than 0")
                       @PathVariable(value = "purchaseId") purchaseId: Long,
                       @Valid @RequestBody purchaseUpdateModel: PurchaseUpdateModel
    ): ResponseEntity<PurchaseEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService
                .update(userId, purchaseId, purchaseUpdateModel))
    }

    @Throws(Exception::class)
    @GetMapping(
            value = ["/{userId}/purchases/{purchaseId}/purchaseItems"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun findPurchaseItems(@Min(value = 1, message = "User ID must be greater than 0")
                          @PathVariable(value = "userId") userId: Long,
                          @Min(value = 1, message = "Purchase ID must be greater than 0")
                          @PathVariable(value = "purchaseId") purchaseId: Long,
                          @RequestParam(value = "page", defaultValue = "1")
                          @Min(value = 1, message = "page must be greater than 0")
                          page: Int,
                          @RequestParam(value = "size", defaultValue = "1")
                          @Min(value = 1, message = "size must be greater than 0")
                          size: Int
    ): ResponseEntity<PageableAndSortableData<PurchaseItemEntity>> {
        return ResponseEntity.status(HttpStatus.OK).body(purchaseService
                .findPurchaseItems(userId, purchaseId, page, size))
    }
}