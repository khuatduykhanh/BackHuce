package com.example.HuceBack.service;

import com.example.HuceBack.dto.common.BaseResponse;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.dto.common.MessageResponse;
import com.example.HuceBack.dto.user.*;
import com.example.HuceBack.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    BaseResponse<UserRes> register(Signup signup);
    void saveUserVerificationToken(User theUser, String token);
    String validateToken(String theToken);
    BaseResponse<JWTAuthRes> signin(LoginReq loginReq);
    BaseResponse<JWTAuthRes> refreshToken(RefreshTokenReq refreshTokenRequest);
    MessageBaseResponse forgotPassword(String email);
    boolean validateForgotPassword(String theToken);
    String verifyForgotPassword(String theToken,ForgotPassword forgotPassword);
    void saveForgotPassword(User theUser, String token);
    MessageBaseResponse changePassword(ChangePassword changePassword, HttpServletRequest request);
    MessageBaseResponse sendVerifyEmail(String email);
    MessageBaseResponse sendVerifyTokenPassword(String email);
    boolean checkToken(HttpServletRequest request);
}
