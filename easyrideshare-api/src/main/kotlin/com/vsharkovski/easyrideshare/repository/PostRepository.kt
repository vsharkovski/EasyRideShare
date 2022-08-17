package com.vsharkovski.easyrideshare.repository

import com.vsharkovski.easyrideshare.domain.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository : JpaRepository<Post, Long> {
    fun findAllByOrderByCreationTimeDesc(): List<Post>
}