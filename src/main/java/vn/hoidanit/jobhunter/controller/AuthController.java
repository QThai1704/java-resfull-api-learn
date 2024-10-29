package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {

    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long jwtRefreshExpiration;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final SecurityUtil securityUtil;

    private final UserService userService;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil, UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody LoginDTO loginDto) {
        // B1: Đưa cho Security thông tin của người dùng nhập vào form login
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword());

        // B2: Xác thực thông tin người dùng
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Tạo Access token
        String accessToken = securityUtil.createAccessToken(authentication);
        //
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO res = new ResLoginDTO();
        User user = this.userService.findUserByEmail(loginDto.getUsername());
        if (user != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                    user.getId(),
                    user.getName(),
                    user.getEmail());
            res.setUser(userLogin);
        }
        res.setAccessToken(accessToken);

        // Tạo refresh token
        String refreshToken = securityUtil.createRefreshToken(loginDto.getUsername(), res);
        this.userService.updateRefreshToken(loginDto.getUsername(), refreshToken);

        // Tạo cookie
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true) // chỉ có tác dụng cho http hoặc https, trong trường hợp này không có tác dụng
                .maxAge(jwtRefreshExpiration)
                .path("/")
                .build();
        return ResponseEntity
                .ok()
                .header("Set-Cookie", responseCookie.toString())
                .body(res);
    }
}
