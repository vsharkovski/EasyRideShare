package com.vsharkovski.easyrideshare.service

import com.vsharkovski.easyrideshare.api.CreateOrFindLocationHolder
import com.vsharkovski.easyrideshare.domain.*
import com.vsharkovski.easyrideshare.repository.PostRepository
import com.vsharkovski.easyrideshare.repository.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.sql.Timestamp
import javax.transaction.Transactional

@Service
class PostService(
    val locationService: LocationService,
    val postRepository: PostRepository,
    val userRepository: UserRepository,
) {
    private val logger = LoggerFactory.getLogger(PostService::class.java)

    fun findPostById(id: Long): Post? = postRepository.findByIdOrNull(id)

    fun findAllPosts(): List<Post> = postRepository.findAllByOrderByCreationTimeDesc()

    @Transactional
    fun createPost(
        creatorId: Long,
        transportType: ETransportType,
        startLocationHolder: CreateOrFindLocationHolder,
        endLocationHolder: CreateOrFindLocationHolder,
        description: String?,
        intendedTravelTime: Timestamp?
    ): PostResult {
        val creator = userRepository.findByIdOrNull(creatorId) ?: return PostMissingUserFail
        val (startLocation, endLocation) = listOf(startLocationHolder, endLocationHolder).map {
            locationService.findOrCreateLocation(it.id, it.name, it.latitude, it.longitude, creator)
                ?: return PostInvalidLocationFail
        }
        val post = try {
            Post(
                creator = creator,
                status = EPostStatus.AVAILABLE,
                transportType = transportType,
                startLocation = startLocation,
                endLocation = endLocation,
                description = description,
                intendedTravelTime = intendedTravelTime
            )
        } catch (e: Exception) {
            return PostInvalidParametersFail
        }
        logger.info("Saving post [{}]", post)
        return try {
            PostCreated(postRepository.save(post))
        } catch (e: Exception) {
            logger.error("Error in saving post [{}]", e.message, e)
            PostDatabaseSavingFail
        }
    }
}