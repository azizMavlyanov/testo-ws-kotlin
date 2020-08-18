package com.testows.config

class SecurityConstants {
    companion object {
        const val SIGN_UP_URL="/api/v1/users"
        const val SIGN_IN_URL="/api/v1/login"
        const val HEADER_STRING="Authorization"
        const val TOKEN_PREFIX="Bearer"
        const val EXPIRATION_TIME=864000000
        const val TOKEN_SECRET="mavlyanov95"
        const val H2_CONSOLE_URL="/h2-console/**"
    }
}