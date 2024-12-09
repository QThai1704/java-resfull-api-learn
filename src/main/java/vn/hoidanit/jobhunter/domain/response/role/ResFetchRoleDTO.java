package vn.hoidanit.jobhunter.domain.response.role;

import java.time.Instant;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResFetchRoleDTO {
     String name;
    String description;
    boolean active;
    Instant createdAt;
    String createdBy;
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
