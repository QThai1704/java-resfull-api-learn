package vn.hoidanit.jobhunter.domain.response.job;

import java.time.Instant;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResFetchJobDTO {
    long id;
    String name;
    String location;
    double salary;
    int quantity;
    LevelEnum level;
    String description;
    Instant startDate;
    Instant endDate;
    boolean active;
    Instant createdAt;
    Instant updatedAt;
    String createdBy;
    String updatedBy;
    List<SkillJob> skill;

    @Getter
    @Setter
    public static class SkillJob{
        String name;
    }
}
