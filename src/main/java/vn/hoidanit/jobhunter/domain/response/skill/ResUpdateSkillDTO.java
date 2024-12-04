package vn.hoidanit.jobhunter.domain.response.skill;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResUpdateSkillDTO {
    private long id;
    private String name;
}
