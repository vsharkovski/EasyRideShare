package com.vsharkovski.easyrideshare.repository

import com.vsharkovski.easyrideshare.domain.Location
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface LocationRepository : JpaRepository<Location, Long> {
    fun existsByNameIgnoreCase(name: String): Boolean
}