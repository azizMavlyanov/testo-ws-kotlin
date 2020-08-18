package com.testows.services.image

import com.testows.exceptions.CommonServiceException
import net.coobird.thumbnailator.Thumbnails
import net.coobird.thumbnailator.name.Rename
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.*

@Service
class ImageServiceImpl: ImageService {
    @Throws(Exception::class)
    override fun upload(imageFolder: String, file: MultipartFile, w: Int, h: Int): String? {
        val categoryImagesDirectory = Path.of("src", "main", "resources",
                "static", "assets", "images", imageFolder.trim())
        val absolutePath = categoryImagesDirectory.toFile().absolutePath
        val uuidFile = UUID.randomUUID().toString()
        val resultFileName = uuidFile + "." + file.originalFilename?.let { StringUtils.cleanPath(it) }
        val copyLocation = Path.of(absolutePath, resultFileName)

        try {
            val uploadDir = File(absolutePath)

            if (!uploadDir.exists()) {
                uploadDir.mkdir()
            }

            Files.copy(file.inputStream, copyLocation, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }

        Thumbnails.of(copyLocation.toString()).size(w, h).outputFormat("jpg").toFiles(Rename.NO_CHANGE)

        return resultFileName
    }

    @Throws(Exception::class)
    override fun load(imageFolder: String, imageName: String): Resource? {
        val categoryImagesDirectory = Paths.get("src", "main", "resources",
                "static", "assets", "images", imageFolder.trim())
        val absolutePath = categoryImagesDirectory.toFile().absolutePath
        val copyLocation = Path
                .of(absolutePath, imageName)

        try {
            return UrlResource(copyLocation.toUri())
        } catch (e: Exception) {
            throw CommonServiceException(e.localizedMessage)
        }
    }
}