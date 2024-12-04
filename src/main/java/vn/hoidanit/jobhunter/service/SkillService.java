package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.skill.ResCreateSkillDTO;
import vn.hoidanit.jobhunter.domain.response.skill.ResFetchSkillDTO;
import vn.hoidanit.jobhunter.domain.response.skill.ResUpdateSkillDTO;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class SkillService {
    
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    // CREATE
    public Skill createSkill(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public ResCreateSkillDTO convertToCreateSkillDTO(Skill skill) {
        return ResCreateSkillDTO.builder()
                .name(skill.getName())
                .build();
    }

    // GET
    public ResPaginationDTO getAllSkill(Pageable pageable) {
        Page<Skill> skillPage = this.skillRepository.findAll(pageable);
        List<ResFetchSkillDTO> listSkill = skillPage.getContent()
                .stream().map(item -> new ResFetchSkillDTO(
                        item.getId(),
                        item.getName()
                )).collect(Collectors.toList());
        ResPaginationDTO resultPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(skillPage.getTotalPages());
        meta.setTotal(skillPage.getTotalElements());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(listSkill);
        return resultPaginationDTO;
    }

    // UPDATE
    public Skill updateSkill(Skill skill) throws IdInvalidException {
        Optional<Skill> skillOptional = this.skillRepository.findById(skill.getId());
        if(skillOptional.isPresent()) {
            Skill currentSkill = skillOptional.get();
            currentSkill.setId(skill.getId());
            currentSkill.setName(skill.getName());
            return this.skillRepository.save(currentSkill);
        }else {
            throw new IdInvalidException("Kỹ năng không tồn tại!");
        }
    }

    public ResUpdateSkillDTO convertToUpdateSkillDTO(Skill skill) {
        return ResUpdateSkillDTO.builder()
                .id(skill.getId())
                .name(skill.getName())
                .build();
    }

    // DELETE

    // CHECK EXIST
    public boolean existsByName(String name) {
        return this.skillRepository.existsByName(name);
    }
}
