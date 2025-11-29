package io.softwarestrategies.eventscheduler.common.data.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProcessOrderEvent implements EventData {

    private long orderId;
    private String orderDescription;
}
