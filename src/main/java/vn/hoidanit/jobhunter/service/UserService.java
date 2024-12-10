package vn.hoidanit.jobhunter.service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResFetchUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

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
                .stream().map(item -> convertToGetUserDTO(item))
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
        ResFetchUserDTO.RoleUser roleUser = new ResFetchUserDTO.RoleUser();

        if (userDTO.getRole() != null) {
            roleUser.setId(getUser.getRole().getId());
            roleUser.setName(getUser.getRole().getName());
            userDTO.setRole(roleUser);
        }

        if(userDTO.getCompany() != null) {
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
        userDTO.setCreatedAt(getUser.getCreatedAt());
        userDTO.setUpdatedAt(getUser.getUpdatedAt());
        return userDTO;
    }

    // Fetch user by token and email
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    // POST
    public User save(User user) {
        if(user.getRole() != null) {
            Role role = this.roleService.getRoleById(user.getRole().getId());
            user.setRole(role);
        }
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
        userDTO.setCreatedAt(Instant.now());
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
        ResUpdateUserDTO.RoleUser roleUser = new ResUpdateUserDTO.RoleUser();
        if(roleUser != null) {
            roleUser.setId(userUpdate.getRole().getId());
            roleUser.setName(userUpdate.getRole().getName());
            userDTO.setRole(roleUser);
        }
        userDTO.setId(userUpdate.getId());
        userDTO.setName(userUpdate.getName());
        userDTO.setEmail(userUpdate.getEmail());
        userDTO.setGender(userUpdate.getGender());
        userDTO.setAddress(userUpdate.getAddress());
        userDTO.setAge(userUpdate.getAge());
        userDTO.setUpdatedAt(Instant.now());
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
