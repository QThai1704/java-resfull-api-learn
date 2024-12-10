package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    // Create
    @PostMapping("/resumes")
    @ApiMessage("Create resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@RequestBody Resume resume) {
        Resume newResume = this.resumeService.createResume(resume);
        ResCreateResumeDTO resCreateResumeDTO = this.resumeService.convertToCreateResumeDTO(newResume);
        return ResponseEntity.status(HttpStatus.CREATED).body(resCreateResumeDTO);
    }

    // Get
    @GetMapping("/resumes/{id}")
    public ResponseEntity<ResFetchResumeDTO> getResumById(@RequestBody Resume resume) {
        Resume fetchResume = this.resumeService.getResumeById(resume.getId());
        ResFetchResumeDTO resFetchResumeDTO = this.resumeService.convertToFetchResumeDTO(fetchResume);
        return ResponseEntity.ok().body(resFetchResumeDTO);
    }

    // Get all ***
    @GetMapping("/resumes")
    public ResponseEntity<ResPaginationDTO> getAllResumById(Pageable pageable) {
        ResPaginationDTO resPaginationDTO = this.resumeService.getAllResume(pageable);
        return ResponseEntity.ok().body(resPaginationDTO);
    }

    // Update
    @PutMapping("/resumes")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) {
        Resume currentResume = this.resumeService.updateResume(resume);
        ResUpdateResumeDTO resUpdateResumeDTO = this.resumeService.convertToUpdateResumeDTO(currentResume);
        return ResponseEntity.ok().body(resUpdateResumeDTO);
    }

    // Delete
    @DeleteMapping("/resumes/{id}")
    public ResponseEntity<Void> deleteResume(Long id) {
        this.resumeService.deleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResPaginationDTO> fetchResumeByUser(Pageable pageable) {
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
}
