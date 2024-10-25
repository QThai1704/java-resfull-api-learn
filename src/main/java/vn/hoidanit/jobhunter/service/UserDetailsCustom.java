package vn.hoidanit.jobhunter.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import vn.hoidanit.jobhunter.repository.UserRepository;

// Chú ý @Component("userDetailsService") xử lý việc tạo ra một bean có tên là userDetailsService xử lý việc xác thực người dùng
@Component("userDetailsService")
public class UserDetailsCustom implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailsCustom(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.hoidanit.jobhunter.domain.User userByEmail = null;
        Optional<vn.hoidanit.jobhunter.domain.User> user = this.userRepository.findByEmail(username);
        if (!user.isPresent()) {
            throw new UsernameNotFoundException("Tài khoản/ mật khẩu không hợp lệ!");
        }
        userByEmail = user.get();
        return new User(
                userByEmail.getEmail(),
                userByEmail.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }

}
