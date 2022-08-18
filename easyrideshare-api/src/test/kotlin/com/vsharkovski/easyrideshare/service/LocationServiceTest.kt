package com.vsharkovski.easyrideshare.service

import com.vsharkovski.easyrideshare.domain.*
import com.vsharkovski.easyrideshare.repository.LocationRepository
import io.mockk.*

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

internal class LocationServiceTest {

    private val locationRepositoryMock = mockk<LocationRepository>()

    private val locationService: LocationService = LocationService(locationRepositoryMock)

    @Test
    fun `test create`() {
        val name = "Skopje"
        val latitude = 0.0f
        val longitude = 0.0f
        val creator = mockk<User>()
        val saved = slot<Location>()
        every { locationRepositoryMock.existsByNameIgnoreCase(name) } returns false
        every { locationRepositoryMock.save(capture(saved)) } returns Location(
            name = name, latitude = latitude, longitude = longitude, creator = creator
        )
        val result = locationService.createLocation(name, latitude, longitude, creator)
        assertTrue(result is LocationCreated)
        val created = (result as LocationCreated).location
        assertEquals(name, created.name)
        assertEquals(latitude, created.latitude)
        assertEquals(longitude, created.longitude)
        assertEquals(creator, created.creator)
    }

    @Test
    fun `test create with existing name`() {
        val name = "Skopje"
        every { locationRepositoryMock.existsByNameIgnoreCase(name) } returns true
        every { locationRepositoryMock.existsByNameIgnoreCase(neq(name)) } returns false
        val result = locationService.createLocation(name, 0.0f, 0.0f)
        assertTrue(result is LocationNameExistsFail)
    }

    @Test
    fun `test create with invalid name`() {
        val names = listOf("", "aa", "a".repeat(10001))
        every { locationRepositoryMock.existsByNameIgnoreCase(any()) } returns false
        names.forEach {
            val result = locationService.createLocation(it, 0.0f, 0.0f)
            assertTrue(result is LocationDatabaseSavingFail)
        }
    }

    @Test
    fun `test find or create with existing id`() {
        val id = 1L
        val name = "Skopje"
        val latitude = 0.0f
        val longitude = 0.0f
        every { locationRepositoryMock.findByIdOrNull(id) } returns Location(
            id = id, name = name, latitude = latitude, longitude = longitude
        )
        val result = locationService.findOrCreateLocation(id)
        assertNotEquals(null, result)
        assertEquals(id, result!!.id)
        verify { locationRepositoryMock.findByIdOrNull(id) }
    }

    @Test
    fun `test find or create with nonexistent id`() {
        val id = 81724172L
        every { locationRepositoryMock.findByIdOrNull(id) } returns null
        val result = locationService.findOrCreateLocation(id)
        assertEquals(null, result)
        verify { locationRepositoryMock.findByIdOrNull(id) }
    }

    @Test
    fun `test find or create with null id and non-null name latitude and longitude`() {
        val id = null
        val name = "Skopje"
        val latitude = 0.0f
        val longitude = 0.0f
        val creator = mockk<User>()
        every { locationRepositoryMock.existsByNameIgnoreCase(name) } returns false
        every { locationRepositoryMock.save(any()) } returns Location(
            name = name, latitude = latitude, longitude = longitude, creator = creator
        )
        val result = locationService.findOrCreateLocation(id, name, latitude, longitude, creator)
        assertNotEquals(null, result)
        verify { locationRepositoryMock.save(any()) }
    }

    @Test
    fun `test find or create with null id and some null between name latitude and longitude`() {
        val id = null
        val names = listOf("Skopje", null)
        val latitudes = listOf(0.0f, null)
        val longitudes = listOf(0.0f, null)
        val creator = mockk<User>()
        var callsWhereNoParamIsNull = 0
        names.forEach { it?.let { every { locationRepositoryMock.existsByNameIgnoreCase(it) } returns false } }
        names.forEach { name ->
            latitudes.forEach { latitude ->
                longitudes.forEach { longitude ->
                    if (name != null && latitude != null && longitude != null) {
                        every {
                            locationRepositoryMock.save(match {
                                it.name == name && it.latitude == latitude && it.longitude == longitude
                            })
                        } returns Location(name = name, latitude = latitude, longitude = longitude, creator = creator)
                        val result = locationService.findOrCreateLocation(id, name, latitude, longitude, creator)
                        assertNotEquals(null, result)
                        callsWhereNoParamIsNull++
                    } else {
                        val result = locationService.findOrCreateLocation(id, name, latitude, longitude, creator)
                        assertEquals(null, result)
                    }
                }
            }
        }
        verify(exactly = callsWhereNoParamIsNull) { locationRepositoryMock.save(any()) }
    }

}