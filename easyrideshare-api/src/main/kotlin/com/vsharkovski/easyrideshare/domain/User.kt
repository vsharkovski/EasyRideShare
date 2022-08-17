package com.vsharkovski.easyrideshare.domain

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @get:NotBlank
    @get:Size(max = 20)
    val username: String,

    @get:NotBlank
    @get:Size(max = 120)
    val password: String,

    @get:NotBlank
    @get:Size(max = 50)
    val email: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: Set<Role>,
) {
    override fun toString(): String = "User(id=$id, username=$username, roles=$roles)"
}
