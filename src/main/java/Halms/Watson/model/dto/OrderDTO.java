package Halms.Watson.model.dto;


import Halms.Watson.model.enums.OrderStatusEnum;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderDTO {

    Long id;
    String clientName;
    String description;
    Long price;

    OrderStatusEnum status;

    OffsetDateTime createdDate;

    OffsetDateTime completedDate;

    EmployeeDTO assignedEmployee;
}