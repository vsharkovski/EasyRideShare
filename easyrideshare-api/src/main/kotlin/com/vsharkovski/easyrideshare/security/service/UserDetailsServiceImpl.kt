package com.vsharkovski.easyrideshare.security.service

import com.vsharkovski.easyrideshare.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import javax.transaction.Transactional

/*
https://stackoverflow.com/questions/11746499/how-to-solve-the-failed-to-lazily-initialize-a-collection-of-role-hibernate-ex
 */

@Service
class UserDetailsServiceImpl(
    val userRepository: UserRepository
) : UserDetailsService {
    @Transactional
    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByUsernameIgnoreCase(username)?.let {
            UserDetailsImpl(it)
        } ?: throw UsernameNotFoundException("User not found with username: $username")
}