package vn.hoidanit.jobhunter.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;

@Service
public class CompanyService {

    public final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    // POST
    public Company createCompany(Company newCompany) {
        return this.companyRepository.save(newCompany);
    }

    // GET
    public ResultPaginationDTO getAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();
        meta.setPage(pageCompany.getNumber() + 1);
        meta.setPageSize(pageCompany.getSize());
        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());
        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(pageCompany.getContent());
        return resultPaginationDTO;
    }

    public Company getCompanyById(long id) {
        Optional<Company> companyList = this.companyRepository.findById(id);
        if (!companyList.isPresent()) {
            return null;
        }
        return companyList.get();
    }

    // PUT
    public Company updateCompany(Company updateCompany) {
        Company currentCompany = this.getCompanyById(updateCompany.getId());
        if (currentCompany != null) {
            currentCompany.setName(updateCompany.getName());
            currentCompany.setAddress(updateCompany.getAddress());
            currentCompany.setLogo(updateCompany.getLogo());
            currentCompany.setDescription(updateCompany.getDescription());
            currentCompany.setCreatedAt(updateCompany.getCreatedAt());
            currentCompany.setUpdatedAt(updateCompany.getUpdatedAt());
            currentCompany.setCreatedBy(updateCompany.getCreatedBy());
            currentCompany.setUpdatedBy(updateCompany.getUpdatedBy());
            this.companyRepository.save(currentCompany);
            return currentCompany;
        }
        return null;
    }

    // DELETE
    public void deleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
