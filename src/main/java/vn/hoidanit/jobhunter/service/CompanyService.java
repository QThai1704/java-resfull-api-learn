package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.PreUpdate;
import vn.hoidanit.jobhunter.domain.Company;
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
    public List<Company> getAllCompany() {
        return this.companyRepository.findAll();
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
        }
        return currentCompany;
    }

    // DELETE
    public void deleteCompany(long id) {
        this.companyRepository.deleteById(id);
    }
}
