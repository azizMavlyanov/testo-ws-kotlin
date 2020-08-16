package com.testows.controller

import com.testows.entity.CategoryEntity
import com.testows.entity.ProductEntity
import com.testows.model.PageableAndSortableData
import com.testows.model.ProductRequestModel
import com.testows.model.ProductUpdateModel
import com.testows.service.product.ProductService
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

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

    @GetMapping(
            value = ["/{productId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun findOne(@Min(value = 1, message = "Product ID must be greater than 0")
                @PathVariable(value = "productId") productId: Long): ResponseEntity<ProductEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findOne(productId))
    }

    @DeleteMapping(
            value = ["/{productId}"]
    )
    fun update(@Min(value = 1, message = "Product ID must be greater than 0")
               @PathVariable(value = "productId") productId: Long): ResponseEntity<ProductEntity> {
        productService.delete(productId)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @PostMapping(
            value = ["/{productId}/images"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun uploadImage(@Min(value = 1, message = "Product ID must be greater than 0")
                    @PathVariable(value = "productId") productId: Long,
                    @RequestParam("file") file: MultipartFile
    ): ResponseEntity<ProductEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(productService.uploadImage(productId, file))
    }

    @GetMapping(value = ["/{productId}/images/{imageName}"])
    fun loadImage(@Min(value = 1, message = "Product ID must be greater than 0")
                  @PathVariable(value = "productId") productId: Long,
                  @NotBlank(message = "Image name cannot be neither null nor blank")
                  @PathVariable(value = "imageName") imageName: String): ResponseEntity<*> {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg")
                .body(productService.loadImage(productId, imageName))
    }
}