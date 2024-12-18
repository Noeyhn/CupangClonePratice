package com.github.cupangclone.web.controller;

import com.github.cupangclone.config.security.JwtTokenProvider;
import com.github.cupangclone.service.AuthService;
import com.github.cupangclone.web.dto.userInfo.UserInfoResponse;
import com.github.cupangclone.web.exceptions.NotAcceptException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping("/check")
    public UserInfoResponse checkInfoUser(HttpServletRequest request, HttpServletResponse response) {

        String email = authService.blockAccessWithOnlyToken(request);

        if ( email != null ) {
            return authService.checkInfoUser(email);
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            throw new NotAcceptException("잘못된 접근입니다.");
        }

    }

}
