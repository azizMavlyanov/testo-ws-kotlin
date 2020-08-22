package com.testows.utils

import com.testows.config.SecurityConstants
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import java.util.*

@Component
class Utils {
    fun hasTokenExpired(token: String): Boolean {
        val claims = Jwts.parser().setSigningKey((SecurityConstants.TOKEN_SECRET)).parseClaimsJws(token).body
        val tokenExpirationDate = claims.expiration;
        val todayDate = Date()

        return tokenExpirationDate.before(todayDate)
    }

    fun generatePasswordResetToken(email: String): String = Jwts
            .builder()
            .setSubject(email)
            .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
            .compact();

    fun generateEmailVerificationToken(email: String): String? = Jwts
            .builder()
            .setSubject(email)
            .setExpiration(Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
            .signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET)
            .compact();

    fun customPaginate(page: Int, size: Int): Pageable {
        return PageRequest.of(if (page != 0) { page - 1 } else page, size)
    }
}