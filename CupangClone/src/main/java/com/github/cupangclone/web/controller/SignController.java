package com.github.cupangclone.web.controller;

import com.github.cupangclone.service.AuthService;
import com.github.cupangclone.web.dto.login.LoginRequest;
import com.github.cupangclone.web.dto.signUp.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
    TODO LIST : 기기별 유저 로그인 관리 로직 구현
                여러기기 동시 접속을 허용할 것인지 말것인지 로직 구현 및 그에 따른 문제 해결 로직 구현
                (판매 물품 등록 및 물품 구매 등 중복되면 안되는 로직들)
                리프레시 토큰을 이용한 자동 로그인 및 로그인 유지 기능 구현
                회원 탈퇴 로직 완성하기

                심화 : 유저 가입시 유저 개인정보 인증 로직 구현
                      네이버 및 카카오 연동 로그인 로직 구현
 */

@RestController
@RequestMapping("/api/signup")
@RequiredArgsConstructor
public class SignController {

    private final AuthService authService;

    @PostMapping("/register")
    public String registerUser(@RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        // 일반 유저페이지에서 가입시 유저 권한 설정
        signUpRequest.setSeller(false);
        boolean isSuccess = authService.createUser(response, signUpRequest);
        return isSuccess ? "회원가입에 성공하였습니다." : "회원가입에 실패하였습니다.";
    }

    @PostMapping("/seller/register")
    public String registerSeller(@RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        // seller 페이지에서 가입시 seller 권한 설정
        signUpRequest.setSeller(true);
        boolean isSuccess = authService.createUser(response, signUpRequest);
        return isSuccess ? "판매자 회원가입에 성공하였습니다." : "판매자 회원가입에 실패하였습니다.";
    }


    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        String token = authService.loginUser(response, loginRequest);
        response.setHeader("Authorization", "Bearer " + token);

        return "로그인에 성공하였습니다.";

    }

    @DeleteMapping("/resign")
    public ResponseEntity<String> resignUser(@RequestParam("password") String password, HttpServletRequest request) {

        if ( authService.resignUser(request, password) ) {
            return ResponseEntity.ok("성공적으로 탈퇴되었습니다.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 맞지 않습니다.");
        }

    }
}
