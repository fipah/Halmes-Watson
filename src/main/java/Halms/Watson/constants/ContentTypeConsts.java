package Halms.Watson.constants;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ContentTypeConsts {

 public static final List<String> allowedContentTypes = List.of("image/jpeg", "image/png", "image/gif", "image/svg+xml", "image/*");
}
