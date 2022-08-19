package com.vsharkovski.easyrideshare.security.service

import com.vsharkovski.easyrideshare.domain.*
import com.vsharkovski.easyrideshare.repository.RoleRepository
import com.vsharkovski.easyrideshare.repository.UserRepository
import com.vsharkovski.easyrideshare.security.jwt.JwtUtils
import com.vsharkovski.easyrideshare.service.ApiDomainService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    val authenticationManager: AuthenticationManager,
    val userRepository: UserRepository,
    val roleRepository: RoleRepository,
    val passwordEncoder: PasswordEncoder,
    val jwtUtils: JwtUtils,
    val apiDomainService: ApiDomainService
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun authenticateUser(username: String, password: String): AuthLoginResult {
        val authentication = try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(username, password)
            )
        } catch (e: AuthenticationException) {
            return AuthLoginFail
        }
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = authentication.principal as UserDetailsImpl
        val jwtCookie = jwtUtils.generateJwtCookie(userDetails)
        val roles = userDetails.authorities.map {
            apiDomainService.roleStringToEnum[it.authority.toString()]
                ?: throw RuntimeException("Could not convert authority to role")
        }
        return AuthLoginSuccess(
            id = userDetails.id,
            username = userDetails.username,
            email = userDetails.email,
            roles = roles,
            jwtCookie = jwtCookie.toString()
        )
    }

    fun registerUser(
        username: String,
        password: String,
        email: String,
    ): AuthRegisterResult {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            return AuthUsernameExistsFail
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return AuthEmailExistsFail
        }
        val roles =
            listOf(roleRepository.findByName(ERole.ROLE_USER) ?: throw RuntimeException("Role not found")).toSet()
        val user = User(username = username, password = passwordEncoder.encode(password), roles = roles, email = email)
        return try {
            logger.info("Saving new user [{}]", user)
            userRepository.save(user)
            AuthRegisterSuccess
        } catch (e: Exception) {
            AuthSavingUserFail
        }
    }

    fun getAuthenticatedUserId(): Long? =
        when (val authentication = SecurityContextHolder.getContext().authentication) {
            is AnonymousAuthenticationToken -> null
            else -> (authentication.principal as UserDetailsImpl).id
        }
}