package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public List<User> fetchAllUser() {
        List<User> userList = this.userService.fetchAllUser();
        return userList;
    }

    @GetMapping("/user/{id}")
    public User fetchUserById(@PathVariable("id") long id) {
        User user = this.userService.fetchUserById(id);
        return user;
    }

    @PostMapping("/user")
    public User createNewUser(@RequestBody User postManUser) {
        User newUser = this.userService.save(postManUser);
        return newUser;
    }

    @PutMapping("/user")
    public User putMethodName(@RequestBody User user) {
        User userUpdate = this.userService.update(user);
        return userUpdate;
    }

    @DeleteMapping("/user/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        this.userService.delete(id);
        return "Xóa thành công";
    }
}
