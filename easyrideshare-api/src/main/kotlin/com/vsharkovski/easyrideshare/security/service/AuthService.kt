package com.vsharkovski.easyrideshare.security.service

import com.vsharkovski.easyrideshare.api.LoginRequest
import com.vsharkovski.easyrideshare.domain.*
import com.vsharkovski.easyrideshare.repository.RoleRepository
import com.vsharkovski.easyrideshare.repository.UserRepository
import com.vsharkovski.easyrideshare.security.jwt.JwtUtils
import com.vsharkovski.easyrideshare.service.ApiDomainService
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
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

    fun authenticateUser(loginRequest: LoginRequest): AuthLoginResult {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = authentication.principal as UserDetailsImpl
        val jwtCookie = jwtUtils.generateJwtCookie(userDetails)
        return AuthLoginSuccess(
            id = userDetails.id,
            username = userDetails.username,
            email = userDetails.email,
            roles = userDetails.authorities.map { it.authority.toString() },
            jwtCookie = jwtCookie.toString()
        )
    }

    fun registerUser(
        username: String,
        password: String,
        rolesStrings: Set<String>,
        email: String,
    ): AuthRegisterResult {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            return AuthUsernameExistsFail
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return AuthEmailExistsFail
        }
        val roles = if (rolesStrings.isEmpty()) {
            listOf(roleRepository.findByName(ERole.ROLE_USER) ?: throw RuntimeException("Role not found"))
        } else {
            rolesStrings.map {
                val roleEnum = apiDomainService.roleStringToEnum[it]
                    ?: throw RuntimeException("Unrecognized role")
                roleRepository.findByName(roleEnum) ?: throw RuntimeException("Role not found")
            }
        }.toSet()
        val user = try {
            User(username = username, password = passwordEncoder.encode(password), roles = roles, email = email)
        } catch (e: Exception) {
            return AuthInvalidParametersFail
        }
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