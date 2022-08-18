package com.vsharkovski.easyrideshare.api

data class CreateOrFindLocationHolder(
    val id: Long,

    val name: String? = null,

    val latitude: Float? = null,

    val longitude: Float? = null,
)