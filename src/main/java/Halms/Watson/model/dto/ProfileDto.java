package Halms.Watson.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ProfileDto {

    Long id;

    String username;

    String fullName;

    String address;

    String city;

    String phoneNumber;

    MultipartFile avatar;
}
