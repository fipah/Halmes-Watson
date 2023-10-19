package Halms.Watson.model.entity;

import Halms.Watson.model.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@Entity
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String username;

    String password;

//    @ElementCollection
//    @JoinTable(name = "authorities", joinColumns = {@JoinColumn(name = "username")})// use default table (PERSON_NICKNAMES)
//            @Column(name = "authority")
@Enumerated(EnumType.STRING)
@Column(name = "role")
private Role role;
}
