package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.other.ResPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResCreateUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResFetchUserDTO;
import vn.hoidanit.jobhunter.domain.response.user.ResUpdateUserDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    // Create
    @PostMapping("/users")
    @ApiMessage("Create new user")
    public ResponseEntity<ResCreateUserDTO> createNewUser(@Valid @RequestBody User postManUser) {
        if (this.userService.existsByEmail(postManUser.getEmail())) {
            throw new RuntimeException("Email" + postManUser.getEmail() + "đã tồn tại");
        }
        postManUser.setPassword(this.passwordEncoder.encode(postManUser.getPassword()));
        User newUser = this.userService.save(postManUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToCreateUserDTO(newUser));
    }

    // GET
    @GetMapping("/users")
    @ApiMessage("Fetch all user")
    public ResponseEntity<ResPaginationDTO> fetchAllUser(@Filter Specification<User> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.userService.fetchAllUser(spec, pageable));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("Get user by id")
    public ResponseEntity<ResFetchUserDTO> fetchUserById(@PathVariable("id") long id) throws IdInvalidException {
        if (this.userService.existsById(id) != true) {
            throw new IdInvalidException("Id không tồn tại.");
        }
        User user = this.userService.fetchUserById(id);
        return ResponseEntity.ok(this.userService.convertToGetUserDTO(user));
    }

    // PUT
    @PutMapping("/users")
    @ApiMessage("Update user by id")
    public ResponseEntity<ResUpdateUserDTO> putMethodName(@Valid @RequestBody User user) throws IdInvalidException {
        if (this.userService.existsById(user.getId()) != true) {
            throw new IdInvalidException("Id không tồn tại.");
        }
        User userUpdate = this.userService.update(user);
        return ResponseEntity.ok(this.userService.convertToUpdateUserDTO(userUpdate));
    }

    // DELETE
    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete user by id")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id) throws IdInvalidException {
        if (this.userService.existsById(id) != true) {
            throw new IdInvalidException("Id không tồn tại.");
        }
        this.userService.delete(id);
        return ResponseEntity.ok(null);
    }
}
