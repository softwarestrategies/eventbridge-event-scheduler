package io.softwarestrategies.eventscheduler.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProcessOrderEvent implements EventData {

    private long orderId;
    private String orderDescription;
}
