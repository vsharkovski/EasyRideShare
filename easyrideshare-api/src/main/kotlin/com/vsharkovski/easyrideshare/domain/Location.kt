package com.vsharkovski.easyrideshare.domain

import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "locations")
data class Location(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val creationTime: Timestamp = Timestamp(System.currentTimeMillis()),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    val creator: User? = null,

    @field:NotBlank
    @field:Size(min = 3, max = 50)
    val name: String,

    val latitude: Float,

    val longitude: Float,
) {
    override fun toString() = "Location(id=$id, name=$name, latitude=$latitude, longitude=$longitude, creator=$creator)"
}
