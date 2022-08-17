package com.vsharkovski.easyrideshare.api

import com.vsharkovski.easyrideshare.domain.*
import com.vsharkovski.easyrideshare.security.jwt.JwtUtils
import com.vsharkovski.easyrideshare.security.service.AuthService
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/auth")
class AuthController(
    val authService: AuthService,
    val jwtUtils: JwtUtils
) {
    @PostMapping("/sign_in")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest): ResponseEntity<AuthResponse> =
        when (val result = authService.authenticateUser(loginRequest)) {
            is AuthLoginSuccess ->
                ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, result.jwtCookie)
                    .body(AuthUserInfoResponse(result.id, result.username, result.roles))
            is AuthLoginFail ->
                ResponseEntity.badRequest().body(AuthMessageResponse(false, "Bad credentials"))
        }

    @PostMapping("/sign_up")
    fun registerUser(@Valid @RequestBody registrationRequest: RegistrationRequest): ResponseEntity<AuthResponse> =
        when (authService.registerUser(
            username = registrationRequest.username,
            password = registrationRequest.password,
            rolesStrings = registrationRequest.role,
            email = registrationRequest.email
        )) {
            is AuthRegisterSuccess ->
                ResponseEntity.ok(AuthMessageResponse(true, "User registered successfully"))
            is AuthUsernameExistsFail ->
                ResponseEntity.badRequest().body(AuthMessageResponse(false, "Username already exists"))
            is AuthEmailExistsFail ->
                ResponseEntity.badRequest().body(AuthMessageResponse(false, "Email is already in use"))
            is AuthInvalidParametersFail ->
                ResponseEntity.badRequest().body(AuthMessageResponse(false, "Invalid parameters"))
            is AuthSavingUserFail ->
                ResponseEntity.badRequest().body(AuthMessageResponse(false, "Internal server error"))
        }

    @PostMapping("/sign_out")
    fun logoutUser(): ResponseEntity<AuthResponse> {
        val cookie = jwtUtils.getCleanJwtCookie()
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
            .body(AuthMessageResponse(true, "Sign out successful."))
    }
}