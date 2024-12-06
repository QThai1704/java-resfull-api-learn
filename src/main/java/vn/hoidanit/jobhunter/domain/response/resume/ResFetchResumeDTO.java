package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.hoidanit.jobhunter.util.constant.StatusEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResFetchResumeDTO {
    String email;
    String url;
    StatusEnum status;
    Instant createdAt;
    String createdBy;
    Instant updatedAt;
    String updatedBy;
    UserResume user;
    JobResume job;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserResume {
        String fullName;
        String email;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JobResume {
        String name;
        String description;
    }
}
