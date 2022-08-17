package com.vsharkovski.easyrideshare.security.service

import com.vsharkovski.easyrideshare.api.LoginRequest
import com.vsharkovski.easyrideshare.domain.*
import com.vsharkovski.easyrideshare.repository.RoleRepository
import com.vsharkovski.easyrideshare.repository.UserRepository
import com.vsharkovski.easyrideshare.security.jwt.JwtUtils
import org.slf4j.LoggerFactory
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
    val jwtUtils: JwtUtils
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)
    private val stringRoleToEnum =
        mapOf("admin" to ERole.ROLE_ADMIN, "moderator" to ERole.ROLE_MODERATOR, "user" to ERole.ROLE_USER)

    fun authenticateUser(loginRequest: LoginRequest): AuthLoginResult {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        val userDetails = authentication.principal as UserDetailsImpl
        val jwtCookie = jwtUtils.generateJwtCookie(userDetails)
        return AuthLoginSuccess(
            userDetails.id, userDetails.username, userDetails.email,
            userDetails.authorities.map { it.authority.toString() }, jwtCookie.toString()
        )
    }

    fun registerUser(
        username: String,
        password: String,
        rolesStr: Set<String>,
        email: String,
    ): AuthRegisterResult {
        if (userRepository.existsByUsernameIgnoreCase(username)) {
            return AuthUsernameExistsFail
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            return AuthEmailExistsFail
        }
        val roles = HashSet<Role>()
        if (rolesStr.isEmpty()) {
            roleRepository.findByName(ERole.ROLE_USER)?.let { roles.add(it) }
                ?: throw RuntimeException("Role not found.")
        } else {
            rolesStr.forEach {
                val roleEnum = stringRoleToEnum[it] ?: throw RuntimeException("Unrecognized role.")
                val role = roleRepository.findByName(roleEnum) ?: throw RuntimeException("Role not found.")
                roles.add(role)
            }
        }
        val user =
            User(id = 0, username = username, password = passwordEncoder.encode(password), roles = roles, email = email)
        return try {
            logger.info("Saving new user [{}]", user)
            userRepository.save(user)
            AuthRegisterSuccess
        } catch (e: Exception) {
            AuthSavingUserFail
        }
    }
}