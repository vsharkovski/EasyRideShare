package com.vsharkovski.easyrideshare.security.service

import com.fasterxml.jackson.annotation.JsonIgnore
import com.vsharkovski.easyrideshare.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(
    val id: Long,
    private val username: String,
    @JsonIgnore
    private val password: String,
    private val authorities: Collection<GrantedAuthority>,
    val email: String
): UserDetails {
    constructor(user: User) : this(
        id = user.id,
        username = user.username,
        password = user.password,
        authorities = user.roles.map { SimpleGrantedAuthority(it.name.name) }.toList(),
        email = user.email
    )

    override fun getUsername(): String = username
    override fun getPassword(): String = password
    override fun isEnabled(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun getAuthorities(): Collection<GrantedAuthority> = authorities

    companion object {
        const val serialVersionUID: Long = 1L
    }
}