package com.vsharkovski.easyrideshare.api

data class PublicPost(
    val id: Long,
    val creationTime: Long,
    val creator: PublicUser,
    val status: String,
    val transportType: String,
    val startLocation: PublicLocation,
    val endLocation: PublicLocation,
    val description: String?,
    val intendedTravelTime: Long?
)
