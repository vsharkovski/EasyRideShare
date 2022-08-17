package com.vsharkovski.easyrideshare.repository

import com.vsharkovski.easyrideshare.domain.ERole
import com.vsharkovski.easyrideshare.domain.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {
    fun findByName(name: ERole): Role?
}