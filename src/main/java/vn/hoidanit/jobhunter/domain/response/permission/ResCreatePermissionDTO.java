package vn.hoidanit.jobhunter.domain.response.permission;

import java.time.Instant;

import jakarta.persistence.criteria.CriteriaBuilder.In;
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
public class ResCreatePermissionDTO {
    String name;
    String apiPath;
    String method;
    String module;
    Instant createdAt;
    String createdBy;
}
