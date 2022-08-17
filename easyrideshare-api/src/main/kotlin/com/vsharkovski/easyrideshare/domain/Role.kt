package com.vsharkovski.easyrideshare.domain

import javax.persistence.*

@Entity
@Table(name = "roles")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    val name: ERole,
)
