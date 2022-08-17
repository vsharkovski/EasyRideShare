package com.vsharkovski.easyrideshare.security.jwt

import com.vsharkovski.easyrideshare.security.service.UserDetailsImpl
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import io.jsonwebtoken.security.SignatureException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import org.springframework.web.util.WebUtils
import java.util.Date
import javax.annotation.PostConstruct
import javax.crypto.SecretKey
import javax.servlet.http.HttpServletRequest

@Component
class JwtUtils(
    @Value("\${easyRideShare.app.jwtSecret}")
    val jwtSecret: String,
    @Value("\${easyRideShare.app.jwtExpirationMs}")
    val jwtExpirationMs: Int,
    @Value("\${easyRideShare.app.jwtCookieName}")
    val jwtCookie: String
) {
    var key: SecretKey = generateSecretKeyFromString("initJwtSecretKey")
    val logger: Logger = LoggerFactory.getLogger(JwtUtils::class.java)

    @PostConstruct
    private fun postConstruct() {
        key = generateSecretKeyFromString(jwtSecret)
    }

    fun getJwtFromCookies(request: HttpServletRequest): String? = WebUtils.getCookie(request, jwtCookie)?.value

    fun generateJwtCookie(userPrincipal: UserDetailsImpl): ResponseCookie {
        val jwt = generateTokenFromUsername(userPrincipal.username)
        return ResponseCookie.from(jwtCookie, jwt)
            .path("/api").maxAge(jwtExpirationMs.toLong()).httpOnly(true).build()
    }

    fun getCleanJwtCookie(): ResponseCookie =
        ResponseCookie.from(jwtCookie, "").path("/api").build()

    fun getUserNameFromJwtToken(token: String): String =
        Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token).body.subject

    fun validateJwtToken(authToken: String): Boolean = try {
        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken)
        true
    } catch (e: Exception) {
        when (e) {
            is SignatureException -> logger.error("Invalid JWT signature [{}]", e.message, e)
            is MalformedJwtException -> logger.error("Invalid JWT token [{}]", e.message, e)
            is ExpiredJwtException -> logger.error("JWT token is expired [{}]", e.message, e)
            is UnsupportedJwtException -> logger.error("JWT token is unsupported [{}]", e.message, e)
            is IllegalArgumentException -> logger.error("JWT claims string is empty [{}]", e.message, e)
        }
        false
    }

    fun generateTokenFromUsername(username: String): String =
        Jwts.builder()
            .setSubject(username)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact()

    private fun generateSecretKeyFromString(string: String): SecretKey {
        val bytes = string.toByteArray()
        return Keys.hmacShaKeyFor(
            if (bytes.size >= 64) bytes
            else bytes.plus(ByteArray(64 - bytes.size))
        )
    }
}