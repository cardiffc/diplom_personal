package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import request.AuthRequestBody;
import response.AuthResponseBody;
import services.AuthService;

@Controller()
public class ApiAuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/api/auth/login")
    public ResponseEntity<AuthResponseBody> logon (@RequestBody AuthRequestBody loginBody) {
        AuthResponseBody authResponseBody = authService.logonUser(loginBody.getE_mail(), loginBody.getPassword());
        return new ResponseEntity<>(authResponseBody, HttpStatus.OK);
    }

    @GetMapping("/api/auth/check")
    public ResponseEntity<AuthResponseBody> authCheck() {
       AuthResponseBody authResponseBody = authService.checkAuth();
        return new ResponseEntity<>(authResponseBody, HttpStatus.OK);
    }

    @GetMapping("/api/auth/logout")
    public ResponseEntity<AuthResponseBody> logout() {
        AuthResponseBody authResponseBody = authService.logout();
        return new ResponseEntity<>(authResponseBody, HttpStatus.OK);
    }


}
