package com.vsharkovski.easyrideshare.domain

import java.sql.Timestamp
import javax.persistence.*
import javax.validation.constraints.Size

@Entity
@Table(name = "posts")
data class Post(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val creationTime: Timestamp = Timestamp(System.currentTimeMillis()),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    val creator: User,

    @Enumerated(EnumType.STRING)
    val status: EPostStatus,

    @Enumerated(EnumType.STRING)
    val transportType: ETransportType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "start_location_id")
    val startLocation: Location,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "end_location_id")
    val endLocation: Location,

    @field:Size(max = 10000)
    val description: String?,

    val intendedTravelTime: Timestamp?,
)
