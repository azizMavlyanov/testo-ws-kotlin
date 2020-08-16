package com.testows.service.image

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface ImageService {
    fun upload(imageFolder: String, file: MultipartFile, w: Int, h: Int): String?
    fun load(imageFolder: String, imageName: String): Resource?
}