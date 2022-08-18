package com.vsharkovski.easyrideshare.domain

sealed interface LocationResult

data class LocationCreated(val location: Location) : LocationResult

object LocationInvalidParametersFail : LocationResult

object LocationNameExistsFail : LocationResult

object LocationDatabaseSavingFail : LocationResult