package com.testows.services.amazon

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder
import com.amazonaws.services.simpleemail.model.*
import com.testows.config.SecurityConstants
import com.testows.entities.UserEntity
import com.testows.exceptions.CommonServiceException
import org.springframework.stereotype.Service

@Service
class AmazonSES {
    companion object {
        val ACCESS_KEY = "AKIAUED62SNF7YDYDSIOO"
        val SECRET_KEY = "fIc09kehMHFbpIHHMv+00mE7iyj7TiHq21xtXkRss"
        val FROM = "alex.mavlyanov95@gmail.com"
        fun verifyEmail(userEntity: UserEntity) {
            val SUBJECT = "Email verification process"
            val HTMLBODY = "Please verify your email address " +
                    "<a href='http://localhost:8080${SecurityConstants.EMAIL_VERIFICATION_URL}?token=" +
                    "${userEntity.emailVerificationToken}'>here</a>"
            val TEXTBODY = "Please verify your email address:" +
                    "http://localhost:8080${SecurityConstants.EMAIL_VERIFICATION_URL}?token=${userEntity.emailVerificationToken}"
            try {
                val client: AmazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder
                        .standard()
                        .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
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

                client.sendEmail(request)
            } catch (e: Exception) {
                throw CommonServiceException(e.localizedMessage)
            }
        }

        fun resetPassword(userEntity: UserEntity, token: String) {
            val PASSWORDSUBJECT = "Password reset process"
            val PASSWORDHTMLBODY = "Reset password " +
                    "<a href='http://localhost:8080${SecurityConstants.REQUEST_PASSWORD_RESET_URL}?token=$token'>here</a>"
            val PASSWORDTEXTBODY = "Reset password " +
                    "http://localhost:8080${SecurityConstants.REQUEST_PASSWORD_RESET_URL}?token=$token"
            try {
                val client: AmazonSimpleEmailService = AmazonSimpleEmailServiceClientBuilder
                        .standard()
                        .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(ACCESS_KEY, SECRET_KEY)))
                        .withRegion(Regions.US_EAST_1)
                        .build()
                val request = SendEmailRequest()
                        .withDestination(Destination().withToAddresses(userEntity.email))
                        .withMessage(Message()
                                .withBody(Body()
                                        .withHtml(Content()
                                                .withCharset("UTF-8").withData(PASSWORDHTMLBODY))
                                        .withText(Content()
                                                .withCharset("UTF-8").withData(PASSWORDTEXTBODY)))
                                .withSubject(Content()
                                        .withCharset("UTF-8").withData(PASSWORDSUBJECT)))
                        .withSource(FROM)

                client.sendEmail(request)
            } catch (e: Exception) {
                throw CommonServiceException(e.localizedMessage)
            }
        }
    }
}
