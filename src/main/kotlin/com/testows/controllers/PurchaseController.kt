package com.testows.controllers

import com.testows.services.purchase.PurchaseService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1/purchases"])
@Validated
class PurchaseController(private val purchaseService: PurchaseService)