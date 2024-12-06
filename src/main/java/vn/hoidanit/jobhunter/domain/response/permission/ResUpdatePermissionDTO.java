package vn.hoidanit.jobhunter.domain.response.permission;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResUpdatePermissionDTO {
    long id;
    String name;
    String apiPath;
    String method;
    String module;
    Instant updatedAt;
    String updatedBy;
}
