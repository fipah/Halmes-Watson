package Halms.Watson.model.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String description;

    Long price;

    String clientName;

    @OneToOne
    OrderStatus orderStatus;

    @OneToOne
    Users employee;

    OffsetDateTime createdDate = OffsetDateTime.now();

    OffsetDateTime completedDate;

    @ManyToOne
    Users user;


    @ManyToOne
    Service service;

    byte[] confirmationPhoto;

    String confirmationPhotoContentType;
}
