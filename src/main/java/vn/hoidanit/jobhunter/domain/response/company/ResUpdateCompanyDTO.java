package vn.hoidanit.jobhunter.domain.response.company;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResUpdateCompanyDTO {
    private long id;
    private String name;
    private String description;
    private String address;
    private String logo;
    private Instant updatedAt;
}
