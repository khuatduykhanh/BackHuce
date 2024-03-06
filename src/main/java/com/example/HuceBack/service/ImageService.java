package com.example.HuceBack.service;

import com.example.HuceBack.dto.Image.ImageRequest;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    MessageBaseResponse uploadImage(ImageRequest imageRequest, HttpServletRequest request) throws IOException;
    MessageBaseResponse uploadImagePost(MultipartFile file, HttpServletRequest request,Long postId) throws IOException;
//    List<byte[]> downloadImages(ImageRequest imageRequest);
    byte[] downloadAvatar(Long id);
    List<Long> getListImage(Long postId);
    byte[] downloadPost(Long imageId);
}
