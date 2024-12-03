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
import vn.hoidanit.jobhunter.domain.dto.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.dto.ResUpdateUserDTO;
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

    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> new ResUserDTO(
                        item.getId(),
                        item.getEmail(),
                        item.getName(),
                        item.getGender(),
                        item.getAddress(),
                        item.getAge(),
                        item.getCreateAt(),
                        item.getUpdateAt()))
                .collect(Collectors.toList());

        ResultPaginationDTO resultPaginationDTO = new ResultPaginationDTO();
        Meta meta = new Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageUser.getTotalPages());
        meta.setTotal(pageUser.getTotalElements());

        resultPaginationDTO.setMeta(meta);
        resultPaginationDTO.setResult(listUser);

        return resultPaginationDTO;
    }

    // POST
    public User save(User user) {
        return this.userRepository.save(user);
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

    public User updateRefreshToken(String email, String refreshToken) {
        User user = this.findUserByEmail(email);
        if (user == null) {
            return null;
        }
        user.setRefreshToken(refreshToken);
        return this.userRepository.save(user);
    }

    // DELETE
    public void delete(long id) throws IdInvalidException {
        this.userRepository.deleteById(id);
    }

    // Process Function
    public boolean existsByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public boolean existsById(long id) {
        return this.userRepository.existsById(id);
    }

    public ResCreateUserDTO convertToCreateUserDTO(User newUser) {
        ResCreateUserDTO userDTO = new ResCreateUserDTO();
        userDTO.setId(newUser.getId());
        userDTO.setName(newUser.getName());
        userDTO.setEmail(newUser.getEmail());
        userDTO.setGender(newUser.getGender());
        userDTO.setAddress(newUser.getAddress());
        userDTO.setAge(newUser.getAge());
        userDTO.setCreateAt(Instant.now());
        return userDTO;
    }

    public ResUpdateUserDTO convertToUpdateUserDTO(User userUpdate) {
        ResUpdateUserDTO userDTO = new ResUpdateUserDTO();
        userDTO.setId(userUpdate.getId());
        userDTO.setName(userUpdate.getName());
        userDTO.setEmail(userUpdate.getEmail());
        userDTO.setGender(userUpdate.getGender());
        userDTO.setAddress(userUpdate.getAddress());
        userDTO.setAge(userUpdate.getAge());
        userDTO.setUpdateAt(Instant.now());
        return userDTO;
    }

    public ResUserDTO convertToGetUserDTO(User getUser) {
        ResUserDTO userDTO = new ResUserDTO();
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

    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }
}
