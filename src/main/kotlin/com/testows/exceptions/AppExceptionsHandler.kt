package com.testows.exceptions

import com.testows.models.ErrorMessage
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import java.util.*

@ControllerAdvice
class AppExceptionsHandler {
    @ExceptionHandler(value = [CommonServiceException::class])
    fun handleCategoryServiceException(ex: CommonServiceException, request: WebRequest?): ResponseEntity<Any?>? {
        val errorMessage = ErrorMessage(Date(), ex.localizedMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(value = [ResourceAlreadyExistsException::class])
    fun handleResourceAlreadyExistsException(ex: ResourceAlreadyExistsException,
                                             request: WebRequest?): ResponseEntity<Any?>? {
        val errorMessage = ErrorMessage(Date(), ex.localizedMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.CONFLICT)
    }

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun handleResourceNotFoundException(ex: ResourceNotFoundException,
                                        request: WebRequest?): ResponseEntity<Any?>? {
        val errorMessage = ErrorMessage(Date(), ex.localizedMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [BadRequestException::class])
    fun handleBadRequestException(ex: BadRequestException,
                                        request: WebRequest?): ResponseEntity<Any?>? {
        val errorMessage = ErrorMessage(Date(), ex.localizedMessage)
        return ResponseEntity(errorMessage, HttpHeaders(), HttpStatus.BAD_REQUEST)
    }
}