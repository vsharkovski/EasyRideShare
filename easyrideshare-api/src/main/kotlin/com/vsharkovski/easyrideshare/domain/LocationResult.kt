package com.vsharkovski.easyrideshare.domain

sealed interface LocationResult

data class LocationCreated(val location: Location) : LocationResult

object LocationInvalidParametersError : LocationResult

object LocationDatabaseSavingError : LocationResult