package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    // POST
    @PostMapping("/companies")
    public ResponseEntity<Company> createCompany(@Valid @RequestBody Company postManCompany) {
        Company newCompany = this.companyService.createCompany(postManCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    // GET
    @GetMapping("/companies")
    @ApiMessage("Fetch all company")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
            @Filter Specification<Company> spec, Pageable pageable) {
        return ResponseEntity.ok(this.companyService.getAllCompany(spec, pageable));
    }

    @GetMapping("/company/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id) {
        return ResponseEntity.ok(this.companyService.getCompanyById(id));
    }

    // PUT
    @PutMapping("/company/{id}")
    public ResponseEntity<Company> updateCompany(@RequestBody Company company) {
        return ResponseEntity.ok(this.companyService.updateCompany(company));
    }

    // DELETE
    @DeleteMapping("/company/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        this.companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
}
