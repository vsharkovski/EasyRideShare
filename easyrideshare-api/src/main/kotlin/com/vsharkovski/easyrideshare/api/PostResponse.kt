package com.vsharkovski.easyrideshare.api

sealed interface PostResponse : Response

data class PostMessageResponse(override val success: Boolean, val message: String) : PostResponse

data class PostInfoResponse(val post: PublicPost) : PostResponse {
    override val success = true
}

data class PostInfoListResponse(val posts: List<PublicPost>) : PostResponse {
    override val success = true
}