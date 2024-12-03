package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.response.ResLoginDTO;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.anotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
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

        @PostMapping("/auth/login")
        public ResponseEntity<ResLoginDTO> login(@Valid @RequestBody ReqLoginDTO loginDto) {
                // B1: Đưa cho Security thông tin của người dùng nhập vào form login
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                loginDto.getUsername(), loginDto.getPassword());

                // B2: Xác thực thông tin người dùng
                Authentication authentication = authenticationManagerBuilder.getObject()
                                .authenticate(authenticationToken);

                // B3: Lưu các thông tin và SecurityContextHolder
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Khởi tạo đối tượng ResLoginDTO
                ResLoginDTO res = new ResLoginDTO();
                User user = this.userService.findUserByEmail(loginDto.getUsername());
                if (user != null) {
                        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                        user.getId(),
                                        user.getName(),
                                        user.getEmail());
                        res.setUser(userLogin);
                }
                // Tạo access token
                String accessToken = securityUtil.createAccessToken(authentication.getName(), res.getUser());
                res.setAccessToken(accessToken);

                // Tạo refresh token
                String refreshToken = securityUtil.createRefreshToken(loginDto.getUsername(), res);
                this.userService.updateRefreshToken(loginDto.getUsername(), refreshToken);

                // Tạo cookie để lưu lại token
                ResponseCookie responseCookie = ResponseCookie.from("refreshToken", refreshToken)
                                .httpOnly(true)
                                .secure(true) // chỉ có tác dụng cho http hoặc https, trong trường hợp này không có tác
                                              // dụng
                                .maxAge(jwtRefreshExpiration)
                                .path("/")
                                .build();

                return ResponseEntity
                                .ok()
                                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                                .body(res);
        }

        @GetMapping("/auth/account")
        @ApiMessage("Fetch account")
        public ResponseEntity<ResLoginDTO.UserGetAccount> getAccount() {
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
                ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount();
                User user = this.userService.findUserByEmail(email);
                if (user != null) {
                        userLogin.setId(user.getId());
                        userLogin.setName(user.getName());
                        userLogin.setEmail(user.getEmail());
                        userGetAccount.setUser(userLogin);
                }
                return ResponseEntity.ok().body(userGetAccount);
        }

        @GetMapping("/auth/refresh")
        @ApiMessage("Get user by refresh token")
        public ResponseEntity<ResLoginDTO> getRefreshToken(
                @CookieValue(name = "refreshToken", defaultValue = "abc") String refreshToken) throws IdInvalidException {
                if(refreshToken.equals("abc")) {
                        throw new IdInvalidException("Bạn không có token");
                }
                // Kiem tra token  
                Jwt decodedToken = this.securityUtil.checkValidRefreshToken(refreshToken);
                String email = decodedToken.getSubject();

                // Kiểm tra thông tin người dùng với refresh token + email
                User currentUser = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
                if (currentUser == null) {
                        throw new IdInvalidException("Token không hợp lệ");     
                }

                // Khởi tạo đối tượng ResLoginDTO
                ResLoginDTO res = new ResLoginDTO();
                User user = this.userService.findUserByEmail(email);
                if (user != null) {
                        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(
                                        user.getId(),
                                        user.getName(),
                                        user.getEmail());
                        res.setUser(userLogin);
                }

                // Tạo access token
                String accessToken = securityUtil.createAccessToken(email, res.getUser());
                res.setAccessToken(accessToken);

                // Tạo refresh token
                String new_refresh_token = securityUtil.createRefreshToken(email, res);
                this.userService.updateRefreshToken(email, refreshToken);

                // Tạo cookie để lưu lại token
                ResponseCookie responseCookie = ResponseCookie
                                .from("refreshToken", new_refresh_token)
                                .httpOnly(true)
                                .secure(true) // chỉ có tác dụng cho http hoặc https, trong trường hợp này không có tác
                                              // dụng
                                .maxAge(jwtRefreshExpiration)
                                .path("/")
                                .build();

                return ResponseEntity
                                .ok()
                                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                                .body(res);
        }
        
        @PostMapping("/auth/logout")
        public ResponseEntity<Void> postLogout() throws IdInvalidException {
                String email = SecurityUtil.getCurrentUserLogin().isPresent()
                                ? SecurityUtil.getCurrentUserLogin().get()
                                : "";
                if(email.equals("")) {
                        throw new IdInvalidException("Email không hợp lệ");
                }

                this.userService.updateRefreshToken(email, null);

                // Xóa cookie
                ResponseCookie deleteCookie = ResponseCookie
                                .from("refreshToken", null)
                                .httpOnly(true)
                                .secure(true) // chỉ có tác dụng cho http hoặc https, trong trường hợp này không có tác
                                              // dụng
                                .maxAge(0)
                                .path("/")
                                .build();
                return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                        .body(null);
        }
        
}
