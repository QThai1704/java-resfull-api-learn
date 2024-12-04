package vn.hoidanit.jobhunter.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResFetchUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // GET
    public User fetchUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public User findUserByEmail(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent()) {
            return user.get();
        }
        return null;
    }

    public ResPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        // Find all record, return type Page
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);

        // Convert every record to ResFetchUserDTO (use stream)
        List<ResFetchUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResFetchUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getCreateAt(),
                        item.getUpdateAt(),
                        new ResFetchUserDTO.CompanyUser(
                                item.getCompany().getId(),
                                item.getCompany().getName()
                        )))
                .collect(Collectors.toList());

        ResPaginationDTO resultPaginationDTO = new ResPaginationDTO();
        ResPaginationDTO.Meta meta = new ResPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(listUser);

        return resultPaginationDTO;
    }

    public ResFetchUserDTO convertToGetUserDTO(User getUser) {
        ResFetchUserDTO userDTO = new ResFetchUserDTO();
        ResFetchUserDTO.CompanyUser companyUser = new ResFetchUserDTO.CompanyUser();
        if(companyUser != null) {
            companyUser.setId(getUser.getCompany().getId());
            companyUser.setName(getUser.getCompany().getName());
            userDTO.setCompany(companyUser);
        }
        userDTO.setId(getUser.getId());
        userDTO.setName(getUser.getName());
        userDTO.setEmail(getUser.getEmail());
        userDTO.setGender(getUser.getGender());
        userDTO.setAddress(getUser.getAddress());
        userDTO.setAge(getUser.getAge());
        userDTO.setUpdateAt(getUser.getCreateAt());
        userDTO.setUpdateAt(getUser.getUpdateAt());
        return userDTO;
    }

    // Fetch user by token and email
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    // POST
    public User save(User user) {
        return this.userRepository.save(user);
    }

    public ResCreateUserDTO convertToCreateUserDTO(User newUser) {
        ResCreateUserDTO userDTO = new ResCreateUserDTO();
        ResCreateUserDTO.CompanyUser companyUser = new ResCreateUserDTO.CompanyUser();
        if(companyUser != null) {
            companyUser.setId(newUser.getCompany().getId());
            companyUser.setName(newUser.getCompany().getName());
            userDTO.setCompany(companyUser);
        }
        userDTO.setId(newUser.getId());
        userDTO.setName(newUser.getName());
        userDTO.setEmail(newUser.getEmail());
        userDTO.setGender(newUser.getGender());
        userDTO.setAddress(newUser.getAddress());
        userDTO.setAge(newUser.getAge());
        userDTO.setCreateAt(Instant.now());
        return userDTO;
    }

    // PUT
    public User update(User reqUser) throws IdInvalidException {
        User currentUser = this.fetchUserById(reqUser.getId());
        if (this.existsByEmail(currentUser.getEmail()) != true) {
            throw new IdInvalidException("Email đã tồn tại");
        } else {
            if (currentUser != null) {
                currentUser.setName(reqUser.getName());
                currentUser.setEmail(reqUser.getEmail());
                currentUser.setPassword(reqUser.getPassword());
                this.userRepository.save(currentUser);
            }
        }
        return currentUser;
    }

    public ResUpdateUserDTO convertToUpdateUserDTO(User userUpdate) {
        ResUpdateUserDTO userDTO = new ResUpdateUserDTO();
        ResUpdateUserDTO.CompanyUser companyUser = new ResUpdateUserDTO.CompanyUser();
        if(companyUser != null) {
            companyUser.setId(userUpdate.getCompany().getId());
            companyUser.setName(userUpdate.getCompany().getName());
            userDTO.setCompany(companyUser);
        }
        userDTO.setId(userUpdate.getId());
        userDTO.setName(userUpdate.getName());
        userDTO.setEmail(userUpdate.getEmail());
        userDTO.setGender(userUpdate.getGender());
        userDTO.setAddress(userUpdate.getAddress());
        userDTO.setAge(userUpdate.getAge());
        userDTO.setUpdateAt(Instant.now());
        return userDTO;
    }

    // DELETE
    public void delete(long id) throws IdInvalidException {
        this.userRepository.deleteById(id);
    }

    // Check exists
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public boolean existsById(long id) {
        return this.userRepository.existsById(id);
    }
    
    // Update refresh token
    public User updateRefreshToken(String email, String refreshToken) {
        User user = this.findUserByEmail(email);
        if (user == null) {
            return null;
        }
        user.setRefreshToken(refreshToken);
        return this.userRepository.save(user);
    }
}
