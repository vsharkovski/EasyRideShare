package com.vsharkovski.easyrideshare.security.service

import com.vsharkovski.easyrideshare.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(
    val userRepository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsernameIgnoreCase(username)?.let {
            UserDetailsImpl(it)
        } ?: throw UsernameNotFoundException("User not found with username: $username")
}