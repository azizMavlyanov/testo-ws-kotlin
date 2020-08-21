package com.testows.services.amazon

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.*
import com.testows.entities.UserEntity
import com.testows.exceptions.CommonServiceException
import org.springframework.stereotype.Service

@Service
class AmazonSES {
    companion object {
        fun verifyEmail(userEntity: UserEntity) {
            val FROM = "alex.mavlyanov95@gmail.com"
            val SUBJECT = "Email verification process"
            val HTMLBODY = "Please verify your email address " +
                    "<a href='http://localhost:8080/api/v1/users/email-verification?token=" +
                    "${userEntity.emailVerificationToken}'>here</a>"
            val TEXTBODY = "Please verify your email address:" +
                    "http://localhost:8080/api/v1/users/email-verification?token=${userEntity.emailVerificationToken}"
            try {
                val client: AmazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder
                        .standard()
                        .withCredentials(
                                AWSStaticCredentialsProvider(
                                        BasicAWSCredentials("AKIAUED62SNF7YDYDSIO",
                                                "fIc09kehMHFbpIHHMv+00mE7iyj7TiHq21xtXkRs")
                                )
                        )
                        .withRegion(Regions.US_EAST_1)
                        .build()
                val request = SendEmailRequest()
                        .withDestination(Destination().withToAddresses(userEntity.email))
                        .withMessage(Message()
                                .withBody(Body()
                                        .withHtml(Content()
                                                .withCharset("UTF-8").withData(HTMLBODY))
                                        .withText(Content()
                                                .withCharset("UTF-8").withData(TEXTBODY)))
                                .withSubject(Content()
                                        .withCharset("UTF-8").withData(SUBJECT)))
                        .withSource(FROM)

                client.sendEmail(request);
            } catch (e: Exception) {
                throw CommonServiceException(e.localizedMessage)
            }
        }
    }
}