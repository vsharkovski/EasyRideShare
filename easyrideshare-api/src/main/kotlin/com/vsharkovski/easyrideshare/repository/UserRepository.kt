package com.vsharkovski.easyrideshare.repository

import com.vsharkovski.easyrideshare.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsernameIgnoreCase(username: String): User?
    fun existsByUsernameIgnoreCase(username: String): Boolean
    fun existsByEmailIgnoreCase(email: String): Boolean
    fun findByUsernameContainingIgnoreCase(term: String): List<User>
}