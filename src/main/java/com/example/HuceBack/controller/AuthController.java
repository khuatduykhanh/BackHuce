package com.example.HuceBack.controller;

import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.dto.user.*;
import com.example.HuceBack.entity.PasswordResetToken;
import com.example.HuceBack.entity.VerifyEmail;
import com.example.HuceBack.event.RegistrationCompleteEvent;
import com.example.HuceBack.exception.APIException;
import com.example.HuceBack.exception.ResourceNotFoundException;
import com.example.HuceBack.repository.ForgotPasswordRepository;
import com.example.HuceBack.repository.VerifyEmailRepository;
import com.example.HuceBack.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private VerifyEmailRepository emailRepository;
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginReq loginReq){
        return ResponseEntity.ok(authService.signin(loginReq));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody Signup signup){
        String regex = ".*@huce\\.edu\\.vn$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(signup.getEmail());
        if (matcher.matches()) {
            return ResponseEntity.ok(authService.register(signup));
        } else {
            throw new APIException(HttpStatus.BAD_REQUEST,"The email is not in the correct school format");
        }
    }
    @PutMapping("/verifyEmail/{token}")
    public ResponseEntity<MessageBaseResponse> verifyEmail(@PathVariable("token") String token){
        VerifyEmail theToken = emailRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token","Token",token));
        String verificationResult = authService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
            return  ResponseEntity.ok(new MessageBaseResponse(1,"Email verified successfully. Now you can login to your account"));
        }
        return ResponseEntity.ok(new MessageBaseResponse(0,"Invalid verification token"));
    }

    @GetMapping("/sendVerifyEmail")
    public ResponseEntity<MessageBaseResponse> sendVerifyEmail(@RequestParam(name = "email") String email){
        return  ResponseEntity.ok(authService.sendVerifyEmail(email));
    }



    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenReq refreshTokenRequest) {
        return  ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/forgotPassword/{email}")
    public ResponseEntity<MessageBaseResponse> emailForgotPassword(@PathVariable("email") String email) {
        return  ResponseEntity.ok(authService.forgotPassword(email));
    }
    @GetMapping("/sendVerifyTokenPassword")
    public ResponseEntity<MessageBaseResponse> sendVerifyTokenPassword(@RequestParam(name = "email") String email){
        return  ResponseEntity.ok(authService.sendVerifyTokenPassword(email));
    }
    @GetMapping("/verifyForgotPassword/{token}")
    public boolean verifyForgotPassword(@PathVariable("token") String token){
        return authService.validateForgotPassword(token);
    }
    @PutMapping("/verifyForgotPassword/{token}")
    public ResponseEntity<MessageBaseResponse> forgotPassword(@PathVariable("token") String token,@Valid @RequestBody ForgotPassword forgotPassword ){
        PasswordResetToken theToken = forgotPasswordRepository.findByToken(token).orElseThrow(() -> new ResourceNotFoundException("Token","Token",token));
        String verificationResult = authService.verifyForgotPassword(token,forgotPassword);
        if (verificationResult.equalsIgnoreCase("valid")){
            return  ResponseEntity.ok(new MessageBaseResponse(1,"Reset password successfully Now you can login to your account"));
        }
        return  ResponseEntity.ok(new MessageBaseResponse(0,"Invalid verification token"));
    }
}
