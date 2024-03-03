package com.fancycomp.devicebooking.messaging

import com.fancycomp.devicebooking.messaging.dto.DeviceEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class MessageConsumer {
    companion object {
        val LOGGER: Logger = LoggerFactory.getLogger(MessageConsumer::class.java)
    }

    @KafkaListener(topics = ["#{'\${device-management.state-changed-topic}'}"], groupId = "#{'\${device-management.state-changed-group-id}'}")
    fun receiveEvent(event: DeviceEvent) {
        LOGGER.info("Received DeviceEvent with id ${event.id}")
    }
}