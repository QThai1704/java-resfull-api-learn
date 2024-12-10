package vn.hoidanit.jobhunter.domain.response.subscriber;

import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import vn.hoidanit.jobhunter.domain.Skill;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResCreateSubscriberDTO {
    String name;
    String email;
    List<SkillSubscriber> skills;


    @Getter
    @Setter
    public static class SkillSubscriber {
        String name;
    }
}
