package com.testows.controller

import com.testows.entity.CategoryEntity
import com.testows.entity.PurchaseEntity
import com.testows.entity.PurchaseItemEntity
import com.testows.entity.UserEntity
import com.testows.model.*
import com.testows.service.purchase.PurchaseService
import com.testows.service.user.UserService
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
class UserController(private val userService: UserService, private val purchaseService: PurchaseService) {
    @PostMapping(
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]

    )
    fun create(@Valid @RequestBody userRequestModel: UserRequestModel): ResponseEntity<UserEntity> {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(userRequestModel))
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    fun findAll(
            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = "page must be greater than 0")
            page: Int,
            @RequestParam(value = "size", defaultValue = "1")
            @Min(value = 1, message = "size must be greater than 0")
            size: Int
    ): ResponseEntity<PageableAndSortableData<UserEntity>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(page, size))
    }

    @GetMapping(
            value = ["/{userId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun findOne(@Min(value = 1, message = "User ID must be greater than 0")
                @PathVariable(value = "userId") userId: Long): ResponseEntity<UserEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(userService.finOne(userId))
    }

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