package com.testows.config

class SecurityConstants {
    companion object {
        const val SIGN_UP_URL="/api/v1/users"
        const val SIGN_IN_URL="/api/v1/login"
        const val EMAIL_VERIFICATION_URL="/api/v1/users/email-verification-request"
        const val REQUEST_PASSWORD_RESET_URL="/api/v1/users/password-reset-request"
        const val PASSWORD_RESET_URL="/api/v1/users/reset-password"
        const val HEADER_STRING="Authorization"
        const val TOKEN_PREFIX="Bearer"
        const val EXPIRATION_TIME=864000000 // 10 days
        const val PASSWORD_EXPIRATION_TIME=3600000 // 1 hour
        const val TOKEN_SECRET="mavlyanov95"
        const val H2_CONSOLE_URL="/h2-console/**"
        const val STATIC_DIR="/static/**"
    }
}