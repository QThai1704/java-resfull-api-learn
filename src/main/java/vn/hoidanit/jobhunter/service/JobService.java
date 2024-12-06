package vn.hoidanit.jobhunter.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResFetchJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;

@Service
public class JobService {
    private final JobRepository jobRepository;

    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    // CREATE
    public Job createJob(Job job) {
        List<Skill> skillList = job.getSkills();
        List<Skill> newSkillList = new ArrayList<Skill>();
        skillList.forEach(item -> {
            if(this.skillRepository.existsById(item.getId())){
                newSkillList.add(item);
            }
        });
        job.setSkills(newSkillList);
        return this.jobRepository.save(job);
    }

    public ResCreateJobDTO convertToCreateJobDTO(Job job){
        ResCreateJobDTO resCreateJobDTO = new ResCreateJobDTO();
        List<ResCreateJobDTO.SkillJob> skillJob = new ArrayList<ResCreateJobDTO.SkillJob>();
        List<Skill> skillList = job.getSkills();
        skillList.forEach(item -> {
            Skill findSkill = this.skillRepository.findById(item.getId()).get();
            ResCreateJobDTO.SkillJob skill = new ResCreateJobDTO.SkillJob();
            skill.setName(findSkill.getName());
            skillJob.add(skill);
        });
        resCreateJobDTO.setName(job.getName());
        resCreateJobDTO.setLocation(job.getLocation());
        resCreateJobDTO.setSalary(job.getSalary());
        resCreateJobDTO.setQuantity(job.getQuantity());
        resCreateJobDTO.setLevel(job.getLevel());
        resCreateJobDTO.setDescription(job.getDescription());
        resCreateJobDTO.setStartDate(job.getStartDate());
        resCreateJobDTO.setEndDate(job.getEndDate());
        resCreateJobDTO.setActive(job.isActive());
        resCreateJobDTO.setSkill(skillJob);
        return resCreateJobDTO;
    }

    // FETCH
    public Job fetchJob(long id) {
        Optional<Job> job = this.jobRepository.findById(id);
        if(job.isPresent()){
            return job.get();
        }
        return null;
    }

    public ResFetchJobDTO convertToFetchJobDTO(Job job){
        ResFetchJobDTO resFetchJobDTO = new ResFetchJobDTO();
        List<ResFetchJobDTO.SkillJob> skillJob = new ArrayList<ResFetchJobDTO.SkillJob>();
        List<Skill> skillList = job.getSkills();
        skillList.forEach(item -> {
            Skill findSkill = this.skillRepository.findById(item.getId()).get();
            ResFetchJobDTO.SkillJob skill = new ResFetchJobDTO.SkillJob();
            skill.setName(findSkill.getName());
            skillJob.add(skill);
        });
        resFetchJobDTO.setId(job.getId());
        resFetchJobDTO.setName(job.getName());
        resFetchJobDTO.setLocation(job.getLocation());
        resFetchJobDTO.setSalary(job.getSalary());
        resFetchJobDTO.setQuantity(job.getQuantity());
        resFetchJobDTO.setLevel(job.getLevel());
        resFetchJobDTO.setDescription(job.getDescription());
        resFetchJobDTO.setStartDate(job.getStartDate());
        resFetchJobDTO.setEndDate(job.getEndDate());
        resFetchJobDTO.setActive(job.isActive());
        resFetchJobDTO.setCreatedAt(job.getCreatedAt());
        resFetchJobDTO.setUpdatedAt(job.getUpdatedAt());
        resFetchJobDTO.setCreatedBy(job.getCreatedBy());
        resFetchJobDTO.setUpdatedBy(job.getUpdatedBy());
        resFetchJobDTO.setSkill(skillJob);
        return resFetchJobDTO;
    }

    public ResPaginationDTO convertToFetchAllJobDTO(Pageable pageable){
        Page<Job> jobPage = this.jobRepository.findAll(pageable);
        List<ResFetchJobDTO> jobList = jobPage.getContent()
                .stream()
                .map(item -> new ResFetchJobDTO(
                        item.getId(),
                        item.getName(),
                        item.getLocation(),
                        item.getSalary(),
                        item.getQuantity(),
                        item.getLevel(),
                        item.getDescription(),
                        item.getStartDate(),
                        item.getEndDate(),
                        item.isActive(),
                        item.getCreatedAt(),
                        item.getUpdatedAt(),
                        item.getCreatedBy(),
                        item.getUpdatedBy(),
                        item.getSkills().stream().map(skill -> {
                            ResFetchJobDTO.SkillJob skillJob = new ResFetchJobDTO.SkillJob();
                            skillJob.setName(skill.getName());
                            return skillJob;
                        }).collect(Collectors.toList())
                ))
                .collect(Collectors.toList());
        ResPaginationDTO resultPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(jobPage.getTotalPages());
        meta.setTotal(jobPage.getTotalElements());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(jobList);
        return resultPaginationDTO;
    }
    // UPDATE
    public Job updateJob(Job job) {
        Job currentJob = this.jobRepository.findById(job.getId()).get();
        currentJob.setId(job.getId());
        currentJob.setName(job.getName());
        currentJob.setLocation(job.getLocation());
        currentJob.setSalary(job.getSalary());
        currentJob.setQuantity(job.getQuantity());
        currentJob.setLevel(job.getLevel());
        currentJob.setDescription(job.getDescription());
        currentJob.setStartDate(job.getStartDate());
        currentJob.setEndDate(job.getEndDate());
        currentJob.setActive(job.isActive());
        currentJob.setSkills(job.getSkills());
        return this.jobRepository.save(currentJob);
    }

    public ResUpdateJobDTO convertToUpdateJobDTO(Job job){
        ResUpdateJobDTO resUpdateJobDTO = new ResUpdateJobDTO();
        List<ResUpdateJobDTO.SkillJob> skillJob = new ArrayList<ResUpdateJobDTO.SkillJob>();
        List<Skill> skillList = job.getSkills();
        skillList.forEach(item -> {
            Skill findSkill = this.skillRepository.findById(item.getId()).get();
            ResUpdateJobDTO.SkillJob skill = new ResUpdateJobDTO.SkillJob();
            skill.setName(findSkill.getName());
            skillJob.add(skill);
        });
        resUpdateJobDTO.setId(job.getId());
        resUpdateJobDTO.setName(job.getName());
        resUpdateJobDTO.setLocation(job.getLocation());
        resUpdateJobDTO.setSalary(job.getSalary());
        resUpdateJobDTO.setQuantity(job.getQuantity());
        resUpdateJobDTO.setLevel(job.getLevel());
        resUpdateJobDTO.setDescription(job.getDescription());
        resUpdateJobDTO.setStartDate(job.getStartDate());
        resUpdateJobDTO.setEndDate(job.getEndDate());
        resUpdateJobDTO.setActive(job.isActive());
        resUpdateJobDTO.setSkill(skillJob);
        return resUpdateJobDTO;
    }

    // DELETE
    public ResponseEntity<Void> deleteJob(long id) {
        this.jobRepository.deleteById(id);
        return ResponseEntity.ok().body(null);
    }
}
