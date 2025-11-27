package io.softwarestrategies.eventscheduler.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ScheduledEventWrapper {

    String source;
    String env;
    String topic;
    EventData eventData;
    String dateCreated;
    int retryCount;
}
