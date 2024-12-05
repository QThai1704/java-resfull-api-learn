package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.skill.ResCreateSkillDTO;
import vn.hoidanit.jobhunter.domain.response.skill.ResUpdateSkillDTO;
import vn.hoidanit.jobhunter.service.SkillService;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    // CREATE
    @PostMapping("/skill")
    public ResponseEntity<ResCreateSkillDTO> createSkill(@RequestBody Skill skill) {
        if(this.skillService.existsByName(skill.getName())) {
            throw new RuntimeException("Kỹ năng " + skill.getName() + " đã tồn tại");
        }
        Skill newSkill = this.skillService.createSkill(skill);
        return ResponseEntity.ok().body(this.skillService.convertToCreateSkillDTO(newSkill));
    }

    // FETCH
    @GetMapping("/skills")
    public ResponseEntity<ResPaginationDTO> fetchAllSkill(Pageable pageable) {
        return ResponseEntity.ok().body(this.skillService.getAllSkill(pageable));
    }
    

    // UPDATE
    @PutMapping("/skill/{id}")
    public ResponseEntity<ResUpdateSkillDTO> updateSkill(@PathVariable String id, @RequestBody Skill skillPostman) throws IdInvalidException {
        Skill currentSkill = this.skillService.updateSkill(skillPostman);
        return ResponseEntity.ok().body(this.skillService.convertToUpdateSkillDTO(currentSkill));
    }
    
    // DELETE
}
