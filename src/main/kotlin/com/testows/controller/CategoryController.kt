package com.testows.controller

import com.testows.entity.CategoryEntity
import com.testows.model.CategoryRequestModel
import com.testows.model.CategoryUpdateModel
import com.testows.model.PageableAndSortableData
import com.testows.service.category.CategoryService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Min

@RestController
@RequestMapping(value = ["/api/v1/categories"])
@Validated
class CategoryController(private val categoryService: CategoryService) {
    @PostMapping(
            consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]

    )
    fun create(@Valid @RequestBody categoryRequestModel: CategoryRequestModel): ResponseEntity<CategoryEntity> {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(categoryRequestModel))
    }

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

    @GetMapping(
            value = ["/{categoryId}"],
            produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    fun findOne(@Min(value = 1, message = "Category ID must be greater than 0")
                @PathVariable(value = "categoryId") categoryId: Long): ResponseEntity<CategoryEntity> {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findOne(categoryId))
    }

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

    @DeleteMapping(
            value = ["/{categoryId}"]
    )
    fun update(@Min(value = 1, message = "Category ID must be greater than 0")
               @PathVariable(value = "categoryId") categoryId: Long): ResponseEntity<CategoryEntity> {
        categoryService.delete(categoryId)

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}