package com.example.HuceBack.service.Impl;

import com.example.HuceBack.dto.common.BaseResponse;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.dto.common.MessageResponse;
import com.example.HuceBack.dto.user.*;
import com.example.HuceBack.entity.*;
import com.example.HuceBack.event.ForgotPasswordCompleteEvent;
import com.example.HuceBack.event.RegistrationCompleteEvent;
import com.example.HuceBack.exception.APIException;
import com.example.HuceBack.exception.ResourceAlreadyExistException;
import com.example.HuceBack.exception.ResourceNotFoundException;
import com.example.HuceBack.repository.ForgotPasswordRepository;
import com.example.HuceBack.repository.RoleRepository;
import com.example.HuceBack.repository.UserRepository;
import com.example.HuceBack.repository.VerifyEmailRepository;
import com.example.HuceBack.security.JwtProvider;
import com.example.HuceBack.security.RefreshTokenService;
import com.example.HuceBack.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private VerifyEmailRepository verificationToken;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtProvider tokenProvider;
    @Autowired
    private RefreshTokenService refreshTokenService;
    @Autowired
    private ForgotPasswordRepository forgotPasswordRepository;
    @Override
    public BaseResponse<JWTAuthRes> signin(LoginReq loginReq) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginReq.getEmail(),
                        loginReq.getPassword()
                ));

        if (authentication.isAuthenticated()) {
            String token = tokenProvider.generateToken(loginReq.getEmail());
            String refreshToken = refreshTokenService.createRefreshToken(loginReq.getEmail(),authentication);
            User user = userRepository.findByEmailAndIsEnabled(loginReq.getEmail(),true).orElseThrow(() -> new ResourceNotFoundException("email","email",loginReq.getEmail()));
            return new BaseResponse<JWTAuthRes>(new JWTAuthRes(user.getName(),user.getId(),user.getDepartment(),user.getLop(),token,refreshToken));
        } else {
            throw new APIException(HttpStatus.BAD_REQUEST, "invalid user request !");
        }
    }
    @Override
    public BaseResponse<UserRes> register(Signup signup) {
        if(userRepository.existsByEmail(signup.getEmail())){
            throw new ResourceAlreadyExistException("email", "email", signup.getEmail());
        }
        String regex = "(\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(signup.getEmail());
        User user = new User();
        if (matcher.find()) {
            String numberPart = matcher.group(1);
            user.setId(Long.parseLong(numberPart));
        } else {
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid email");
        }
        user.setName(signup.getName());
        user.setEmail(signup.getEmail());
        user.setPassword(passwordEncoder.encode(signup.getPassword()));
        Role role = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singleton(role));
        User newUser =  userRepository.save(user);
        publisher.publishEvent(new RegistrationCompleteEvent(user));
        return new BaseResponse<>(convertUser(newUser));
    }
    @Override
    public BaseResponse<JWTAuthRes> refreshToken(RefreshTokenReq refreshTokenRequest) {
        return refreshTokenService.findByToken(refreshTokenRequest.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String token = tokenProvider.generateToken(userInfo.getEmail());
                    User user = userRepository.findByEmailAndIsEnabled(userInfo.getEmail(),true).orElseThrow(() -> new ResourceNotFoundException("email","email",userInfo.getEmail()));
                    return new BaseResponse<JWTAuthRes>(new JWTAuthRes(user.getName(),user.getId(),user.getDepartment(),user.getLop(),token,refreshTokenRequest.getToken()));
                }).orElseThrow(() -> new RuntimeException(
                        "Refresh token is not in database!"));

    }

    @Override
    public MessageBaseResponse forgotPassword(String email) {
        User user = userRepository.findByEmailAndIsEnabled(email,true).orElseThrow(() -> new ResourceNotFoundException("email","email",email));
        PasswordResetToken password = forgotPasswordRepository.findByUser(user);
        if(password != null){
            forgotPasswordRepository.delete(password);
        }
        publisher.publishEvent(new ForgotPasswordCompleteEvent(user));
        return  new MessageBaseResponse(1,"Successfully sent authentication code");
    }

    @Override
    public boolean validateForgotPassword(String theToken) {
        PasswordResetToken token = forgotPasswordRepository.findByToken(theToken).orElseThrow(() -> new ResourceNotFoundException("Token","Token",theToken));
        if(token == null){
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid verification token");
        }
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            forgotPasswordRepository.delete(token);
            throw new APIException(HttpStatus.BAD_REQUEST,"Token already expired");
        }
        return true;
    }

    @Override
    public String verifyForgotPassword(String theToken, ForgotPassword forgotPassword) {
        PasswordResetToken token = forgotPasswordRepository.findByToken(theToken).orElseThrow(() -> new ResourceNotFoundException("Token","Token",theToken));
        if(token == null){
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid verification token");
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            forgotPasswordRepository.delete(token);
            throw new APIException(HttpStatus.BAD_REQUEST,"Token already expired");
        }
        user.setPassword(passwordEncoder.encode(forgotPassword.getPasswordNew()));
        userRepository.save(user);
        forgotPasswordRepository.delete(token);
        return "valid";
    }

    @Override
    public void saveUserVerificationToken(User theUser, String token) {
            VerifyEmail verify = new VerifyEmail(token, theUser);
            verificationToken.save(verify);
    }
    @Override
    public void saveForgotPassword(User theUser, String token) {
        PasswordResetToken verify = new PasswordResetToken(token, theUser);
        forgotPasswordRepository.save(verify);
    }

    @Override
    public MessageBaseResponse changePassword(ChangePassword changePassword,HttpServletRequest request) {
        String token = tokenProvider.getTokenFromRequest(request);
        String email;
        if(StringUtils.hasText(token) && tokenProvider.validateToken(token)){
             email = tokenProvider.getEmail(token);
        } else {
            email = null;
        }
        if(email == null){
            throw new APIException(HttpStatus.BAD_REQUEST,"no right to change password");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        email,
                        changePassword.getPasswordOld()
                ));
        if (authentication.isAuthenticated()) {
            User user = userRepository.findByEmailAndIsEnabled(email,true).orElseThrow(() -> new ResourceNotFoundException("email","email",email));
            user.setPassword(passwordEncoder.encode(changePassword.getPasswordNew()));
            userRepository.save(user);
            return new MessageBaseResponse(1,"changed password successfully") ;
        } else {
            return new MessageBaseResponse(0,"Old password is incorrect, please re-enter");
        }

    }

    @Override
    public MessageBaseResponse sendVerifyEmail(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("email","email",email));
        VerifyEmail verify =  verificationToken.findByUser(user);
        if(verify != null ){
            verificationToken.delete(verify);
        }
        publisher.publishEvent(new RegistrationCompleteEvent(user));
        return new MessageBaseResponse(1,"resend successfully");
    }

    @Override
    public MessageBaseResponse sendVerifyTokenPassword(String email) {
        User user = userRepository.findByEmailAndIsEnabled(email,true).orElseThrow(() -> new ResourceNotFoundException("email","email",email));
        PasswordResetToken password = forgotPasswordRepository.findByUser(user);
        if(password != null){
            forgotPasswordRepository.delete(password);
        }
        publisher.publishEvent(new ForgotPasswordCompleteEvent(user));
        return new MessageBaseResponse(1,"resend successfully");
    }

    @Override
    public boolean checkToken(HttpServletRequest request) {
        String token = tokenProvider.getTokenFromRequest(request);
        return tokenProvider.validateToken(token);
    }
    @Override
    public String validateToken(String theToken) {
        VerifyEmail token = verificationToken.findByToken(theToken).orElseThrow(() -> new ResourceNotFoundException("Token","Token",theToken));
        if(token == null){
            throw new APIException(HttpStatus.BAD_REQUEST,"Invalid verification token");
        }
        User user = token.getUser();
        if(user.isEnabled()){
            throw new APIException(HttpStatus.BAD_REQUEST,"Account has not been activated");
        }
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) <= 0){
            verificationToken.delete(token);
            throw new APIException(HttpStatus.BAD_REQUEST,"Token already expired");
        }
        user.setEnabled(true);
        userRepository.save(user);
        verificationToken.delete(token);
        return "valid";
    }
    private UserRes convertUser(User user){
        return modelMapper.map(user, UserRes.class);
    }
}
