package com.vsharkovski.easyrideshare.service

import com.vsharkovski.easyrideshare.api.PublicLocation
import com.vsharkovski.easyrideshare.api.PublicPost
import com.vsharkovski.easyrideshare.api.PublicUser
import com.vsharkovski.easyrideshare.api.UserAuthEmbed
import com.vsharkovski.easyrideshare.domain.*
import org.springframework.stereotype.Service

@Service
class ApiDomainService {
    final val roleStringToEnum: Map<String, ERole> =
        mapOf(
            "admin" to ERole.ROLE_ADMIN,
            "moderator" to ERole.ROLE_MODERATOR,
            "user" to ERole.ROLE_USER
        )

    private final val roleEnumToString: Map<ERole, String> = roleStringToEnum.entries.associate { it.value to it.key }

    final val transportTypeStringToEnum: Map<String, ETransportType> =
        mapOf(
            "owned_vehicle" to ETransportType.OWNED_VEHICLE,
            "hired_vehicle" to ETransportType.HIRED_VEHICLE
        )

    private final val transportTypeEnumToString: Map<ETransportType, String> =
        transportTypeStringToEnum.entries.associate { it.value to it.key }

    private final val postStatusStringToEnum: Map<String, EPostStatus> =
        mapOf(
            "available" to EPostStatus.AVAILABLE,
            "completed" to EPostStatus.COMPLETED,
            "cancelled" to EPostStatus.CANCELLED,
            "full" to EPostStatus.FULL
        )

    private final val postStatusEnumToString: Map<EPostStatus, String> =
        postStatusStringToEnum.entries.associate { it.value to it.key }

    fun userToPublic(
        user: User,
        includeRoles: Boolean = false,
        includeAuthData: Boolean = false
    ): PublicUser = PublicUser(
        id = user.id,
        username = user.username,
        roles = if (includeRoles) user.roles.mapNotNull { roleEnumToString[it.name] } else null,
        authData = if (includeAuthData) UserAuthEmbed() else null
    )

    fun locationToPublic(location: Location): PublicLocation = PublicLocation(
        id = location.id,
        name = location.name,
        latitude = location.latitude,
        longitude = location.longitude
    )

    fun postToPublic(
        post: Post,
        includeCreatorRoles: Boolean = false,
        includeAuthData: Boolean = false
    ): PublicPost = PublicPost(
        id = post.id,
        creationTime = post.creationTime.time,
        creator = userToPublic(
            user = post.creator,
            includeRoles = includeCreatorRoles,
            includeAuthData = includeAuthData
        ),
        status = postStatusEnumToString[post.status]!!,
        transportType = transportTypeEnumToString[post.transportType]!!,
        startLocation = locationToPublic(location = post.startLocation),
        endLocation = locationToPublic(location = post.endLocation),
        description = post.description,
        intendedTravelTime = post.intendedTravelTime?.time,
    )
}