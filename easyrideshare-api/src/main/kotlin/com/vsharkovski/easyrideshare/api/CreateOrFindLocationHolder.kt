package com.vsharkovski.easyrideshare.api

data class CreateOrFindLocationHolder(
    val id: Long,

    val name: String?,

    val latitude: Float?,

    val longitude: Float?,
)