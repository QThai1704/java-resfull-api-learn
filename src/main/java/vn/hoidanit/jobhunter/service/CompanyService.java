package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.company.ResCreateCompanyDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResFetchCompanyDTO;
import vn.hoidanit.jobhunter.domain.response.company.ResUpdateCompanyDTO;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class CompanyService {

    public final CompanyRepository companyRepository;

    public final UserRepository userRepository;

    public CompanyService(CompanyRepository companyRepository, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    // POST / CREATE
    public Company createCompany(Company newCompany) {
        return this.companyRepository.save(newCompany);
    }

    public ResCreateCompanyDTO convertToCreateCompanyDTO(Company company) {
        return ResCreateCompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .address(company.getAddress())
                .logo(company.getLogo())
                .createdAt(company.getCreatedAt())
                .build();
    }

    // GET / FETCH
    public ResPaginationDTO getAllCompany(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompany = this.companyRepository.findAll(spec, pageable);
        List<ResFetchCompanyDTO> listCompany = pageCompany.getContent()
                .stream().map(item -> new ResFetchCompanyDTO(
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getAddress(),
                        item.getLogo(),
                        item.getCreatedAt(),
                        item.getUpdatedAt()
                )).collect(Collectors.toList());
        
        ResPaginationDTO resultPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageCompany.getTotalPages());
        meta.setTotal(pageCompany.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(listCompany);
        return resultPaginationDTO;
    }

    public Company getCompanyById(long id) {
        Optional<Company> companyList = this.companyRepository.findById(id);
        if (!companyList.isPresent()) {
            return null;
        }
        return companyList.get();
    }

    public ResFetchCompanyDTO convertToFetchCompanyDTO(Company company) {
        return ResFetchCompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .address(company.getAddress())
                .logo(company.getLogo())
                .createdAt(company.getCreatedAt())
                .updatedAt(company.getUpdatedAt())
                .build();
    }
    // PUT / UPDATE
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

    public ResUpdateCompanyDTO convertToUpdateCompanyDTO(Company company) {
        return ResUpdateCompanyDTO.builder()
                .id(company.getId())
                .name(company.getName())
                .description(company.getDescription())
                .address(company.getAddress())
                .logo(company.getLogo())
                .updatedAt(company.getCreatedAt())
                .build();
    }

    // DELETE
    public void deleteCompany(long id) {
        List<User> getAllUserByCompanyId = this.userRepository.findAllUserByCompanyId(id);
        getAllUserByCompanyId.stream().forEach(
            user -> {
                this.userRepository.delete(user);
            }
        );
        this.companyRepository.deleteById(id);
    }
}
