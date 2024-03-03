package com.fancycomp.devicebooking.it

import com.fancycomp.devicebooking.dto.DeviceDto
import com.fancycomp.devicebooking.repository.DeviceRepository
import com.fancycomp.devicebooking.repository.DeviceTypeRepository
import com.fancycomp.devicebooking.utils.TestDeviceUtils.Companion.getAvailableDeviceWithTypeId
import com.fancycomp.devicebooking.utils.TestDeviceUtils.Companion.getTestDeviceType
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.lifecycle.Startables
import org.testcontainers.utility.DockerImageName


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
class DeviceManagementIT {
    @Autowired
    lateinit var deviceRepository: DeviceRepository
    @Autowired
    lateinit var deviceTypeRepository: DeviceTypeRepository
    @Autowired
    lateinit var mvc: MockMvc
    companion object {
        @Container
        @ServiceConnection
        val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:14-alpine")
        @Container
        @ServiceConnection
        private val kafka: KafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"))
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            Startables.deepStart(postgres, kafka).join()
        }
        @JvmStatic
        @AfterAll
        fun afterAll() {
            postgres.stop()
            kafka.stop()
        }
    }

    val objectMapper: ObjectMapper = ObjectMapper()
        .registerModule(JavaTimeModule())
        .registerKotlinModule()
        .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)

    @BeforeEach
    fun setUp() {
        deviceRepository.deleteAll()
        deviceTypeRepository.deleteAll()
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `GIVEN two test devices WHEN get all test devices THEN should return both with 200 response`() {
        // GIVEN
        val testDeviceTypeEntity = deviceTypeRepository.save(getTestDeviceType())
        val deviceOne = getAvailableDeviceWithTypeId(testDeviceTypeEntity.id)
        val deviceTwo = getAvailableDeviceWithTypeId(testDeviceTypeEntity.id)
        deviceRepository.saveAll(listOf(deviceOne, deviceTwo))
        // WHEN
        val result = mvc.perform(
            get("/api/v1/device/management/all").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn()
        // THEN
        val devices: List<DeviceDto> = objectMapper.readerForListOf(DeviceDto::class.java).readValue(result.response.contentAsString)
        assertThat(devices).hasSize(2)
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `GIVEN booked device WHEN try to book it THEN should return 400 response`() {
        // GIVEN
        val deviceTypeEntity = deviceTypeRepository.save(getTestDeviceType())
        val device = getAvailableDeviceWithTypeId(deviceTypeEntity.id)
        device.isAvailable = false
        val deviceEntity = deviceRepository.save(device)
        // WHEN
        mvc.perform(
            put("/api/v1/device/management/${deviceEntity.id}/book").contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
            .andExpect(content().string(containsString("Device with id ${deviceEntity.id} is already booked")))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `GIVEN no devices in the system WHEN try to book one by device_type_id THEN should return 404 response`() {
        // GIVEN
        val testId = 111
        // WHEN
        mvc.perform(
            put("/api/v1/device/management/device-type/$testId/book").contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(content().string(containsString("Not found device type with id $testId")))
    }

    @Test
    @WithMockUser(roles = ["USER"])
    fun `GIVEN booked device WHEN try to release it THEN should return 200 response`() {
        // GIVEN
        val deviceTypeEntity = deviceTypeRepository.save(getTestDeviceType())
        val device = getAvailableDeviceWithTypeId(deviceTypeEntity.id)
        device.isAvailable = false
        val deviceEntity = deviceRepository.save(device)
        // WHEN
        val result = mvc.perform(
            put("/api/v1/device/management/${deviceEntity.id}/release").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk).andReturn()
        // THEN
        val releasedDevice = objectMapper.readValue(result.response.contentAsString, DeviceDto::class.java)
        assertThat(releasedDevice.isAvailable).isTrue()
    }

    @Test
    fun `GIVEN no devices WHEN get all test devices without auth THEN should return 401 response`() {
        // WHEN
        mvc.perform(
            get("/api/v1/device/management/all").contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized).andReturn()
    }
}