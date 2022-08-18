package com.vsharkovski.easyrideshare.service

import com.vsharkovski.easyrideshare.domain.*
import com.vsharkovski.easyrideshare.repository.LocationRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class LocationService(
    val locationRepository: LocationRepository
) {
    private val logger = LoggerFactory.getLogger(LocationService::class.java)

    fun findLocationById(id: Long): Location? = locationRepository.findByIdOrNull(id)

    fun createLocation(name: String, latitude: Float, longitude: Float, creator: User? = null): LocationResult {
        val location = Location(name = name, latitude = latitude, longitude = longitude, creator = creator)
        if (locationRepository.existsByNameIgnoreCase(name)) {
            return LocationNameExistsFail
        }
        logger.info("Saving location [{}]", location)
        return try {
            LocationCreated(locationRepository.save(location))
        } catch (e: Exception) {
            logger.error("Error in saving location [{}]", e.message, e)
            LocationDatabaseSavingFail
        }
    }

    fun findOrCreateLocation(
        id: Long? = null,
        name: String? = null,
        latitude: Float? = null,
        longitude: Float? = null,
        creator: User? = null
    ): Location? =
        if (id == null) {
            if (name != null && latitude != null && longitude != null) {
                when (val result = createLocation(name, latitude, longitude, creator)) {
                    is LocationCreated -> result.location
                    else -> null
                }
            } else {
                null
            }
        } else {
            findLocationById(id)
        }
}