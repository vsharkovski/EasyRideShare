package com.vsharkovski.easyrideshare.api

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class RegistrationRequest(
    @field:NotBlank
    @field:Size(min = 3, max = 20)
    @field:Pattern(regexp = "^[A-Za-z\\d]+\$")
    val username: String,

    @field:NotBlank
    @field:Size(max = 50)
    @field:Email
    val email: String,

    @field:NotBlank
    @field:Size(min = 6, max = 40)
    val password: String,
)