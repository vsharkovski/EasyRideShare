package com.vsharkovski.easyrideshare.service

import com.vsharkovski.easyrideshare.api.CreateOrFindLocationHolder
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

    fun createLocation(name: String, latitude: Float, longitude: Float, creator: User?): LocationResult {
        val location = try {
            Location(name = name, latitude = latitude, longitude = longitude, creator = creator)
        } catch (e: Exception) {
            return LocationInvalidParametersFail
        }
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

    fun findOrCreateLocation(holder: CreateOrFindLocationHolder, creator: User?): Location? =
        if (holder.id == 0L) {
            if (holder.name != null && holder.latitude != null && holder.longitude != null) {
                when (val result = createLocation(holder.name, holder.latitude, holder.longitude, creator)) {
                    is LocationCreated -> result.location
                    else -> null
                }
            } else {
                null
            }
        } else {
            findLocationById(holder.id)
        }
}