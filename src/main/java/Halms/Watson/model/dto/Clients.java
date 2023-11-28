package Halms.Watson.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Clients {
    String clientName;
    String description;
    String service;
    Long price;
    Long serviceId;
}