package io.softwarestrategies.eventscheduler.scheduler;

import java.time.temporal.ChronoUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.softwarestrategies.eventscheduler.common.data.dto.ProcessOrderEvent;
import io.softwarestrategies.eventscheduler.scheduler.service.ScheduledEventService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Main Spring Boot Application class for the Event Scheduler.
 * Implements CommandLineRunner to execute logic right after the application context loads.
 */
@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class EventSchedulerApplication {

	private static final String TOPIC_PROCESS_ORDER = "topic-process-order";

	private final ScheduledEventService scheduledEventService;

	public static void main(String[] args) {
		// Starts the Spring Boot application, runs the CommandLineRunner, and then shuts down.
		SpringApplication.run(EventSchedulerApplication.class, args);
	}

	/**
	 * Defines a CommandLineRunner bean which will execute when the application starts.
	 * This is the entry point for your command-line logic.
	 *
	 * @return A CommandLineRunner instance.
	 */
	@Bean
	public CommandLineRunner runner() {
		log.info("Application starting...");
		log.info("Java Version: " + System.getProperty("java.version"));

		scheduleProcessOrderEvent();

		return args -> System.exit(0);
	}

	/**
	 * Example of scheduling an event to be executed in the future.  This could be some call in some service, where
	 * we want to post a scheduled event.
	 */
	private void scheduleProcessOrderEvent() {
		final long orderId = 111L;
		final String orderDescription = "Deferred order to be processed";

		// Create the event data.
		ProcessOrderEvent processOrderEvent = new ProcessOrderEvent(orderId, orderDescription);

		// Configure the delay
		long eventDelayInMsecs = 3 * (60 * 1000);
		ChronoUnit eventDelayUnits = ChronoUnit.MILLIS;

		// Schedule the event based on the above
		scheduledEventService.scheduleEvent(TOPIC_PROCESS_ORDER, processOrderEvent, eventDelayInMsecs, eventDelayUnits);
	}
}