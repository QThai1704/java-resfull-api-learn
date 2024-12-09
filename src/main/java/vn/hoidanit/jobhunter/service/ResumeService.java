package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import com.turkraft.springfilter.parser.FilterParser;
import com.turkraft.springfilter.parser.node.FilterNode;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Service
public class ResumeService {
    @Autowired
    FilterBuilder fb;

    @Autowired
    private FilterParser filterParser;

    @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    // CREATE
    public Resume createResume(Resume resume) {
        return resumeRepository.save(resume);
    }

    public ResCreateResumeDTO convertToCreateResumeDTO(Resume resume) {
        ResCreateResumeDTO resCreateResumeDTO = new ResCreateResumeDTO();
        ResCreateResumeDTO.UserResume userResume = new ResCreateResumeDTO.UserResume();
        User user = this.userRepository.findById(resume.getUser().getId()).get();
        userResume.setFullName(user.getName());
        userResume.setEmail(user.getEmail());
        ResCreateResumeDTO.JobResume jobResume = new ResCreateResumeDTO.JobResume();
        Job job = this.jobRepository.findById(resume.getJob().getId()).get();
        jobResume.setName(job.getName());
        jobResume.setDescription(job.getDescription());
        resCreateResumeDTO.setEmail(resume.getEmail());
        resCreateResumeDTO.setUrl(resume.getUrl());
        resCreateResumeDTO.setStatus(resume.getStatus());
        resCreateResumeDTO.setUser(userResume);
        resCreateResumeDTO.setJob(jobResume);
        resCreateResumeDTO.setCreatedAt(resume.getCreatedAt());
        resCreateResumeDTO.setCreateBy(resume.getCreateBy());
        return resCreateResumeDTO;
    }

    // FETCH
    public Resume getResumeById(Long id) {
        Optional<Resume> resume = resumeRepository.findById(id);
        if (resume.isPresent()) {
            return resume.get();
        }
        return null;
    }

    public ResFetchResumeDTO convertToFetchResumeDTO(Resume resume) {
        ResFetchResumeDTO resFetchResumeDTO = new ResFetchResumeDTO();
        ResFetchResumeDTO.UserResume userResume = new ResFetchResumeDTO.UserResume();
        User user = this.userRepository.findById(resume.getUser().getId()).get();
        userResume.setFullName(user.getName());
        userResume.setEmail(user.getEmail());
        ResFetchResumeDTO.JobResume jobResume = new ResFetchResumeDTO.JobResume();
        Job job = this.jobRepository.findById(resume.getJob().getId()).get();
        jobResume.setName(job.getName());
        jobResume.setDescription(job.getDescription());
        resFetchResumeDTO.setEmail(resume.getEmail());
        resFetchResumeDTO.setUrl(resume.getUrl());
        resFetchResumeDTO.setStatus(resume.getStatus());
        resFetchResumeDTO.setUser(userResume);
        resFetchResumeDTO.setJob(jobResume);
        resFetchResumeDTO.setCreatedAt(resume.getCreatedAt());
        resFetchResumeDTO.setCreatedBy(resume.getCreateBy());
        resFetchResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resFetchResumeDTO.setUpdatedBy(resume.getCreateBy());
        return resFetchResumeDTO;
    }

    public ResPaginationDTO getAllResume(Pageable pageable) {
        Page<Resume> resumePage = this.resumeRepository.findAll(pageable);
        List<Resume> resumeList = resumePage.getContent();
        List<ResFetchResumeDTO> listResume = resumeList.stream()
                .map(item -> new ResFetchResumeDTO(
                        item.getEmail(),
                        item.getUrl(),
                        item.getStatus(),
                        item.getCreatedAt(),
                        item.getCreateBy(),
                        item.getUpdatedAt(),
                        item.getCreateBy(),
                        new ResFetchResumeDTO.UserResume(
                                item.getUser().getName(),
                                item.getUser().getEmail()),
                        new ResFetchResumeDTO.JobResume(
                                item.getJob().getName(),
                                item.getJob().getDescription())))
                .collect(Collectors.toList());
        ResPaginationDTO resPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(resumePage.getTotalPages());
        meta.setTotal(resumePage.getTotalElements());
        resPaginationDTO.setMeta(meta);
        resPaginationDTO.setResult(listResume);
        return resPaginationDTO;
    }

    // UPDATE
    public Resume updateResume(Resume resume) {
        Resume currentResume = resumeRepository.findById(resume.getId()).get();
        currentResume.setStatus(resume.getStatus());
        return this.resumeRepository.save(currentResume);
    }

    public ResUpdateResumeDTO convertToUpdateResumeDTO(Resume resume) {
        ResUpdateResumeDTO resUpdateResumeDTO = new ResUpdateResumeDTO();
        ResUpdateResumeDTO.UserResume userResume = new ResUpdateResumeDTO.UserResume();
        User user = this.userRepository.findById(resume.getUser().getId()).get();
        userResume.setFullName(user.getName());
        userResume.setEmail(user.getEmail());
        ResUpdateResumeDTO.JobResume jobResume = new ResUpdateResumeDTO.JobResume();
        Job job = this.jobRepository.findById(resume.getJob().getId()).get();
        jobResume.setName(job.getName());
        jobResume.setDescription(job.getDescription());
        resUpdateResumeDTO.setId(resume.getId());
        resUpdateResumeDTO.setEmail(resume.getEmail());
        resUpdateResumeDTO.setUrl(resume.getUrl());
        resUpdateResumeDTO.setStatus(resume.getStatus());
        resUpdateResumeDTO.setUser(userResume);
        resUpdateResumeDTO.setJob(jobResume);
        resUpdateResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resUpdateResumeDTO.setUpdatedBy(resume.getCreateBy());
        return resUpdateResumeDTO;
    }

    // DELETE
    public void deleteResume(Long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResPaginationDTO fetchResumeByUser(Pageable pageable) {
        // query builder
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        // ***
        FilterNode node = filterParser.parse("email='" + email + "'");
        FilterSpecification<Resume> spec = filterSpecificationConverter.convert(node);
        
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);

        ResPaginationDTO rs = new ResPaginationDTO();
        ResPaginationDTO.Meta mt = new ResPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResFetchResumeDTO> listResume = pageResume.getContent()
                .stream().map(item -> this.convertToFetchResumeDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listResume);

        return rs;
    }
}
