package com.testows.services.image

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface ImageService {
    @Throws(Exception::class)
    fun upload(imageFolder: String, file: MultipartFile, w: Int, h: Int): String?
    @Throws(Exception::class)
    fun load(imageFolder: String, imageName: String): Resource?
}