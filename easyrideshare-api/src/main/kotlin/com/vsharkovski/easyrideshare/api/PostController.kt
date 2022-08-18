package com.vsharkovski.easyrideshare.api

import com.vsharkovski.easyrideshare.domain.*
import com.vsharkovski.easyrideshare.security.service.AuthService
import com.vsharkovski.easyrideshare.service.PostService
import com.vsharkovski.easyrideshare.service.ApiDomainService
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.sql.Timestamp
import javax.validation.Valid

@RestController
@RequestMapping("/api/post")
class PostController(
    val postService: PostService,
    val authService: AuthService,
    val apiDomainService: ApiDomainService,
) {
    @GetMapping("/{id}")
    fun getPostById(@PathVariable id: Long): ResponseEntity<PostResponse> =
        postService.findPostById(id)?.let { ResponseEntity.ok(PostInfoResponse(apiDomainService.postToPublic(it))) }
            ?: ResponseEntity.badRequest().body(PostMessageResponse(false, "Invalid post id"))

    @GetMapping(value = ["", "/"])
    fun getPosts(): ResponseEntity<PostResponse> {
        val posts = postService.findAllPosts()
        return ResponseEntity.ok(PostInfoListResponse(posts.map { apiDomainService.postToPublic(it) }))
    }

    @PostMapping(value = ["", "/"])
    @PreAuthorize("hasRole('USER')")
    fun createPost(@Valid @RequestBody request: CreatePostRequest): ResponseEntity<PostResponse> {
        val transportType = apiDomainService.transportTypeStringToEnum[request.transportType]
            ?: return ResponseEntity.badRequest().body(PostMessageResponse(false, "Invalid transport type"))
        val result = postService.createPost(
            creatorId = authService.getAuthenticatedUserId()!!,
            transportType = transportType,
            startLocationHolder = request.startLocation,
            endLocationHolder = request.endLocation,
            description = request.description,
            intendedTravelTime = request.intendedTravelTime?.let { Timestamp(it) }
        )
        return when (result) {
            is PostCreated ->
                ResponseEntity.ok(PostInfoResponse(apiDomainService.postToPublic(result.post)))
            is PostInvalidLocationFail ->
                ResponseEntity.badRequest().body(PostMessageResponse(false, "Invalid location data"))
            is PostMissingUserFail, PostDatabaseSavingFail ->
                ResponseEntity.internalServerError().body(PostMessageResponse(false, "Internal error"))
        }
    }
}