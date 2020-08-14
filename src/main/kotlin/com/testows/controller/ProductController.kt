package com.testows.controller

import com.testows.entity.ProductEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.ProductRequestModel
import com.testows.model.ProductUpdateModel
import com.testows.service.product.ProductService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@RequestMapping(value = ["/api/v1/products"])
@Validated
class ProductController(val productService: ProductService) {
    @PostMapping(
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]

    )
    fun create(@Valid @RequestBody productRequestModel: ProductRequestModel): ResponseEntity<ProductEntity> {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.create(productRequestModel))
    }

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    fun findAll(
            @RequestParam(value = "page", defaultValue = "1")
            @Min(value = 1, message = "page must be greater than 0")
            page: Int,
            @RequestParam(value = "size", defaultValue = "1")
            @Min(value = 1, message = "size must be greater than 0")
            size: Int
    ): ResponseEntity<PageableAndSortableData<ProductEntity>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll(page, size))
    }

    @PatchMapping(
            value = ["/{productId}"],
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun update(@Min(value = 1, message = "Product ID must be greater than 0")
               @PathVariable(value = "productId") productId: Long,
               @Valid @RequestBody productUpdateModel: ProductUpdateModel): ResponseEntity<ProductEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(productId, productUpdateModel))
    }
}