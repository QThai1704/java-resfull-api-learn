package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.hoidanit.jobhunter.util.constant.StatusEnum;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResCreateResumeDTO {
    String email;
    String url;
    StatusEnum status;
    Instant createdAt;
    String createdBy;
    UserResume user;
    JobResume job;

    @Getter
    @Setter
    public static class UserResume {
        String fullName;
        String email;
    }

    @Getter
    @Setter
    public static class JobResume {
        String name;
        String description;
    }
}
