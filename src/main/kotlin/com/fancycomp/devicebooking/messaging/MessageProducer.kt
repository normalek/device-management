package com.fancycomp.devicebooking.messaging

import com.fancycomp.devicebooking.messaging.dto.DeviceEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class MessageProducer(private val kafkaTemplate: KafkaTemplate<String, DeviceEvent>,
                      @Value("\${device-management.state-changed-topic}") private val topic: String) {
    fun sendEvent(event: DeviceEvent) {
        kafkaTemplate.send(topic, event.id.toString(), event)
    }
}