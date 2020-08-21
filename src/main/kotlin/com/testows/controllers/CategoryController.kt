package com.testows.controllers

import com.testows.entities.CategoryEntity
import com.testows.models.CategoryRequestModel
import com.testows.models.CategoryUpdateModel
import com.testows.models.PageableAndSortableData
import com.testows.services.category.CategoryService
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
import javax.validation.constraints.NotNull

@RestController
@RequestMapping(value = ["/api/v1/categories"])
@Validated
@CrossOrigin
class CategoryController(private val categoryService: CategoryService) {
    @Throws(Exception::class)
    @PostMapping(
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]

    )
    fun create(@Valid @RequestBody categoryRequestModel: CategoryRequestModel): ResponseEntity<CategoryEntity> {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryRequestModel))
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
                ): ResponseEntity<PageableAndSortableData<CategoryEntity>>
    {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll(page, size))
    }

    @Throws(Exception::class)
    @GetMapping(
            value = ["/{categoryId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun findOne(@Min(value = 1, message = "Category ID must be greater than 0")
                @PathVariable(value = "categoryId") categoryId: Long): ResponseEntity<CategoryEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findOne(categoryId))
    }

    @Throws(Exception::class)
    @PatchMapping(
            value = ["/{categoryId}"],
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun update(@Min(value = 1, message = "Category ID must be greater than 0")
               @PathVariable(value = "categoryId") categoryId: Long,
               @Valid @RequestBody categoryUpdateModel: CategoryUpdateModel): ResponseEntity<CategoryEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.update(categoryId, categoryUpdateModel))
    }

    @Throws(Exception::class)
    @DeleteMapping(
            value = ["/{categoryId}"]
    )
    fun update(@Min(value = 1, message = "Category ID must be greater than 0")
               @PathVariable(value = "categoryId") categoryId: Long): ResponseEntity<CategoryEntity> {
        categoryService.delete(categoryId)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @Throws(Exception::class)
    @PostMapping(
            value = ["/{categoryId}/images"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun uploadImage(@Min(value = 1, message = "Category ID must be greater than 0")
                    @PathVariable(value = "categoryId") categoryId: Long,
                    @NotNull(message = "File cannot be null")
                    @RequestParam("file") file: MultipartFile
                    ): ResponseEntity<CategoryEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.uploadImage(categoryId, file))
    }

    @Throws(Exception::class)
    @GetMapping(value = ["/{categoryId}/images/{imageName}"])
    fun loadImage(@Min(value = 1, message = "Category ID must be greater than 0")
                  @PathVariable(value = "categoryId") categoryId: Long,
                  @NotBlank(message = "Image name cannot be neither null nor blank")
                  @PathVariable(value = "imageName") imageName: String): ResponseEntity<*> {
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpg")
                .body(categoryService.loadImage(categoryId, imageName))
    }
}