package io.softwarestrategies;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.scheduler.SchedulerClient;
import software.amazon.awssdk.services.scheduler.model.*;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class EventSchedulerApp {

    public static void main(String[] args) {
        String region = "us-east-2";
        String lambdaFunctionArn = "arn:aws:lambda:us-east-2:853094644116:function:caps-processor";
        String executionRoleArn = "arn:aws:iam::853094644116:role/EventBridgeSchedulerExecutionRole";

        String eventPayload = createSampleEventPayload();

        Instant executionTime = Instant.now().plus(5, ChronoUnit.MINUTES);
        String executionTimeStr= DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneOffset.UTC).format(executionTime);

        try {
            String scheduleArn = scheduleEvent(
                    Region.of(region),
                    lambdaFunctionArn,
                    executionRoleArn,
                    eventPayload,
                    executionTimeStr
            );

            System.out.println("Successfully created schedule: " + scheduleArn);
            System.out.println("Event will execute at: " + executionTimeStr);

        } catch (Exception e) {
            System.err.println("Error creating schedule: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Creates a schedule in EventBridge Scheduler
     *
     * @param region AWS region
     * @param targetFunctionArn ARN of the Lambda function to invoke
     * @param executionRoleArn ARN of the role that EventBridge Scheduler will use
     * @param eventPayload JSON payload to pass to the target
     * @param executionTime ISO8601 timestamp for when the event should execute
     * @return ARN of the created schedule
     */
    private static String scheduleEvent(
            Region region,
            String targetFunctionArn,
            String executionRoleArn,
            String eventPayload,
            String executionTime) {

        // Initialize the Scheduler client
        SchedulerClient schedulerClient = SchedulerClient.builder()
                .region(region)
                .build();

        // Create a unique schedule name
        String scheduleName = "schedule-" + System.currentTimeMillis();

        // Set up the target (Lambda function)
        Target target = Target.builder()
                .arn(targetFunctionArn)
                .roleArn(executionRoleArn)
                .input(eventPayload)
                .build();

        // Configure the schedule to run exactly at the specified time
        FlexibleTimeWindow timeWindow = FlexibleTimeWindow.builder()
                .mode(FlexibleTimeWindowMode.OFF)
                .build();

        // Create the schedule request
        CreateScheduleRequest request = CreateScheduleRequest.builder()
                .name(scheduleName)
                .scheduleExpression("at(" + executionTime + ")") // ISO8601 format
                .target(target)
                .flexibleTimeWindow(timeWindow)
                .build();

        // Create the schedule and return its ARN
        CreateScheduleResponse response = schedulerClient.createSchedule(request);
        return response.scheduleArn();
    }

    private static String createSampleEventPayload() {
        return "{"
                + "\"customerID\": \"1\","
                + "\"orderId\": \"ORD-" + System.currentTimeMillis() + "\","
                + "\"amount\": 99.95,"
                + "\"processType\": \"SHIPMENT\""
                + "}";
    }
}