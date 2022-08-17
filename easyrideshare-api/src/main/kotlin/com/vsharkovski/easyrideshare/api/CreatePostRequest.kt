package com.vsharkovski.easyrideshare.api

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreatePostRequest(
    @field:NotNull
    val transportType: String,

    @field:NotNull
    val startLocation: CreateOrFindLocationHolder,

    @field:NotNull
    val endLocation: CreateOrFindLocationHolder,

    @field:Size(max = 10000)
    val description: String?,

    val intendedTravelTime: Long?
)