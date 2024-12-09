package vn.hoidanit.jobhunter.domain.response.role;

import java.time.Instant;
import java.util.List;

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
public class ResUpdateRoleDTO {
    long id;
    String name;
    String description;
    boolean active;
    Instant updatedAt;
    String updatedBy;
    List<PermissionRole> permission;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PermissionRole {
        private String name;
        private String apiPath;
        private String method;
        private String module;
    }
}
