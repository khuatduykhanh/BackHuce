package com.example.HuceBack.controller;

import com.example.HuceBack.dto.Image.ImageRequest;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.dto.user.ChangePassword;
import com.example.HuceBack.service.AuthService;
import com.example.HuceBack.service.ImageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/account")
public class AccountController {
    @Autowired
    private AuthService authService;
    @Autowired
    private ImageService imageService;
    @GetMapping("/checkToken")
    public ResponseEntity<Boolean> checkToken(HttpServletRequest request){
        return ResponseEntity.ok(authService.checkToken(request));
    }
    @PutMapping("/changePassword")
    public ResponseEntity<MessageBaseResponse> changePassword(@Valid @RequestBody ChangePassword changePassword, HttpServletRequest request){
        return ResponseEntity.ok(authService.changePassword(changePassword,request));
    }
    @PostMapping("/uploadImage")
    public ResponseEntity<MessageBaseResponse> uploadImage(@Valid @ModelAttribute ImageRequest imageRequest,HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(imageService.uploadImage(imageRequest,request));
    }
    @GetMapping("/getImage/{userId}")
    public ResponseEntity<?> downloadImage(@PathVariable Long userId){
        byte[] imageData= imageService.downloadAvatar(userId);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }


}
