package io.softwarestrategies.eventscheduler.scheduler.service;

import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.softwarestrategies.eventscheduler.common.data.dto.EventData;
import io.softwarestrategies.eventscheduler.scheduler.data.ScheduledEventWrapper;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleRequest;
import software.amazon.awssdk.services.scheduler.model.CreateScheduleResponse;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindow;
import software.amazon.awssdk.services.scheduler.model.FlexibleTimeWindowMode;
import software.amazon.awssdk.services.scheduler.model.Target;

import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Slf4j
public class ScheduledEventService {

	@Value("${application.event.source}")
	private String EVENT_SOURCE;

	@Value("${application.env}")
	private String ENV;

	@Value("${AWS_REGION}")
	private String AWS_REGION;

	@Value("${AWS_LAMBDA_TARGET_FUNCTION_ARN}")
	private String LAMBDA_TARGET_FUNCTION_ARN;

	@Value("${AWS_EVENTBRIDGE_SCHEDULER_EXECUTION_ROLE_ARN}")
	private String EVENTBRIDGE_SCHEDULER_EXECUTION_ROLE_ARN;

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public void scheduleEvent(String topic, EventData eventData, long eventDelayAmount, ChronoUnit eventDelayUnits) {

        String dateTimeCreated = dateTimeFormatter.format(LocalDateTime.now().atZone(ZoneId.of("UTC")));

        String eventScheduledTime = dateTimeFormatter.withZone(ZoneOffset.UTC)
                .format(Instant.now().plus(eventDelayAmount, eventDelayUnits));

		String id = UUID.randomUUID().toString();

        ScheduledEventWrapper scheduledEventWrapper
				= new ScheduledEventWrapper(id, EVENT_SOURCE, ENV, topic, eventData, dateTimeCreated, 0);

        scheduleEvent(scheduledEventWrapper, eventScheduledTime);
    }

	/**
	 * WARNING: Only use this for local development/testing!  This bypasses SSL certificate validation.
	 */
	private TrustManager[] getTrustAllManager() {
		return new TrustManager[]{
				new X509TrustManager() {
					public X509Certificate[] getAcceptedIssuers() {
						return new X509Certificate[0];
					}
					public void checkClientTrusted(X509Certificate[] certs, String authType) {}
					public void checkServerTrusted(X509Certificate[] certs, String authType) {}
				}
		};
	}

	/**
     * Creates a schedule in EventBridge Scheduler
     *
     * @param scheduledEventWrapper JSON payload to pass to the target
     */
    private void scheduleEvent(ScheduledEventWrapper scheduledEventWrapper, String eventScheduledTime) {
        String scheduledEventRequestAsString;

        try {
            scheduledEventRequestAsString = objectMapper.writeValueAsString(scheduledEventWrapper);
			log.info("ScheduledEventWrapper JSON: {}", scheduledEventRequestAsString);

        } catch (JsonProcessingException e) {
            // There will be better logging here and throw a different exception, but good enough for now
            log.error("Unable to serialize ScheduledEventWrapper to JSON", e);
            throw new RuntimeException("Unable to serialize ScheduledEventWrapper to JSON");
        }

        // Initialize the Scheduler client
        SchedulerClient schedulerClient = SchedulerClient.builder()
                .region(Region.of(AWS_REGION))
				.httpClientBuilder(ApacheHttpClient.builder()
						.tlsTrustManagersProvider(this::getTrustAllManager))

                .build();

        // Create a unique schedule name
        String scheduleName = "schedule-" + System.currentTimeMillis();

		// Set up the target (Lambda function)
        Target target = Target.builder()
                .arn(LAMBDA_TARGET_FUNCTION_ARN)
                .roleArn(EVENTBRIDGE_SCHEDULER_EXECUTION_ROLE_ARN)
                .input(scheduledEventRequestAsString)
                .build();

        // Configure the schedule to run exactly at the specified time

        FlexibleTimeWindow timeWindow = FlexibleTimeWindow.builder()
                .mode(FlexibleTimeWindowMode.OFF)
                .build();

        // Create the schedule request
        CreateScheduleRequest request = CreateScheduleRequest.builder()
                .name(scheduleName)
                .scheduleExpression("at(" + eventScheduledTime + ")") // ISO8601 format
                .target(target)
                .flexibleTimeWindow(timeWindow)
                .build();

        // Create the schedule and return its ARN
        CreateScheduleResponse response = schedulerClient.createSchedule(request);

        // There will be better logging here, but good enough for now
        log.info("Successfully created schedule: " + response.scheduleArn());
        log.info("Event will execute at: " + eventScheduledTime);
    }
}
