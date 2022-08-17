package com.vsharkovski.easyrideshare.api

data class PublicUser(
    val id: Long,
    val username: String,
    val roles: List<String>?,
    val authData: UserAuthEmbed?
)
