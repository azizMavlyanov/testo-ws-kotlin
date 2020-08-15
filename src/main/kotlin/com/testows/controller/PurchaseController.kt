package com.testows.controller

import com.testows.entity.PurchaseEntity
import com.testows.entity.UserEntity
import com.testows.model.PurchaseRequestModel
import com.testows.model.UserRequestModel
import com.testows.service.purchase.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping(value = ["/api/v1/purchases"])
@Validated
class PurchaseController(private val purchaseService: PurchaseService)