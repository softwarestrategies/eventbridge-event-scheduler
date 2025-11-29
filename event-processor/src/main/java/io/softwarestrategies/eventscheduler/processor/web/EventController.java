package io.softwarestrategies.eventscheduler.processor.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

import io.softwarestrategies.eventscheduler.processor.service.EventService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/events")
@AllArgsConstructor
public class EventController {

	private final EventService eventService;

	@PostMapping
	public ResponseEntity<String> processEvent(@RequestBody JsonNode eventAsJsonNode) throws JsonProcessingException {
		eventService.processEvent(eventAsJsonNode);
		return ResponseEntity.ok().build();
	}
}
