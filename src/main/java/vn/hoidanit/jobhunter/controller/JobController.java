package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResFetchJobDTO;
import vn.hoidanit.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.service.JobService;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;




@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // CREATE
    @PostMapping("/job")
    public ResponseEntity<ResCreateJobDTO> createJob(@RequestBody Job jobPostman) {
        Job newJob = this.jobService.createJob(jobPostman);
        return ResponseEntity
                .ok()
                .body(this.jobService.convertToCreateJobDTO(newJob));
    }

    // FETCH
    @GetMapping("/job/{id}")
    public ResponseEntity<ResFetchJobDTO> getJob(@PathVariable long id) {
        Job currentJob = this.jobService.fetchJob(id);
        return ResponseEntity
                .ok()
                .body(this.jobService.convertToFetchJobDTO(currentJob));
    }

    @GetMapping("jobs")
    public ResponseEntity<ResPaginationDTO> getMethodName(Pageable pageable) {
        return ResponseEntity.ok().body(this.jobService.convertToFetchAllJobDTO(pageable));
    }

    // UPDATE
    @PutMapping("/job/{id}")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@RequestBody Job jobPostman) {
        Job currentJob = this.jobService.updateJob(jobPostman);
        return ResponseEntity
                .ok()
                .body(this.jobService.convertToUpdateJobDTO(currentJob));
    }

    // DELETE
    @DeleteMapping("/job/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable long id) {
        this.jobService.deleteJob(id);
        return ResponseEntity
                .ok()
                .body("Job has been deleted successfully.");
    }
}
