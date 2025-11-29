package io.softwarestrategies.eventscheduler.scheduler.data;

import io.softwarestrategies.eventscheduler.common.data.dto.EventData;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduledEventWrapper {

    String id;
    String source;
    String env;
    String topic;
    EventData data;
    String dateCreated;
    int retryCount;
}
