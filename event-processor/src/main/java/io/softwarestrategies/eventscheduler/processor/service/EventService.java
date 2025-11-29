package io.softwarestrategies.eventscheduler.processor.service;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.softwarestrategies.eventscheduler.common.data.dto.ProcessOrderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class EventService {

	private final ObjectMapper objectMapper;

	public void processEvent(@RequestBody JsonNode eventAsJsonNode) throws JsonProcessingException {
		log.debug("Processing event: {}", eventAsJsonNode);

		String eventTopic = eventAsJsonNode.get("topic").asText();
		String eventData = eventAsJsonNode.get("data").toString();

		switch (eventTopic) {
			case "topic-process-order" -> {
				ProcessOrderEvent processOrderEvent = objectMapper.readValue(eventData, ProcessOrderEvent.class);
				log.info("Processing order: {}", processOrderEvent);
			}
			case "topic-ship-order" ->
				log.info("Shipping order..");
			default ->
				log.warn("Unknown event topic: {}", eventTopic);
		}
	}
}
