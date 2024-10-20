package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/user/create")
    public String createNewUser() {
        User user = new User();
        user.setName("Nguyen");
        user.setEmail("hoidanit@gmail.com");
        user.setPassword("123456");
        this.userService.save(user);
        return "User created";
    }
}
