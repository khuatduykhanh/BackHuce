package com.example.HuceBack.service.Impl;

import com.example.HuceBack.dto.Image.ImageRequest;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.entity.ImageData;
import com.example.HuceBack.entity.Post;
import com.example.HuceBack.entity.User;
import com.example.HuceBack.exception.ResourceNotFoundException;
import com.example.HuceBack.repository.ImageDataRepository;
import com.example.HuceBack.repository.PostRepository;
import com.example.HuceBack.repository.UserRepository;
import com.example.HuceBack.security.JwtProvider;
import com.example.HuceBack.service.ImageService;
import com.example.HuceBack.utils.ImageUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final UserRepository userRepository;
    private final ImageDataRepository imageDataRepository;
    private final PostRepository postRepository;
    private final JwtProvider jwtProvider;
    @Override
    public MessageBaseResponse uploadImage(ImageRequest imageRequest, HttpServletRequest request) throws IOException {
        User user =  jwtProvider.getUserFromRequest(request);
        if(user == null ){
            return new MessageBaseResponse(0,"User not exits");
        }
        if(Objects.equals(imageRequest.getType(), "avatar")) {
            imageDataRepository.save(ImageData.builder()
                    .name(imageRequest.getFile().getOriginalFilename())
                    .type(imageRequest.getFile().getContentType())
                    .imageData(ImageUtils.compressImage(imageRequest.getFile().getBytes()))
                    .avatar(true)
                    .user(user).build());
            return new MessageBaseResponse(1, "file uploaded successfully : " + imageRequest.getFile().getOriginalFilename());
        }
        return new MessageBaseResponse(0,"Đã có lỗi xảy ra");
    }

    @Override
    public MessageBaseResponse uploadImagePost(MultipartFile file, HttpServletRequest request,Long postId) throws IOException {
        User user =  jwtProvider.getUserFromRequest(request);
        if(user == null ){
            return new MessageBaseResponse(0,"User not exits");
        }
        imageDataRepository.save(ImageData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .imageData(ImageUtils.compressImage(file.getBytes()))
                .post(true)
                .postId(postId)
                .user(user).build());
        return new MessageBaseResponse(1, "file uploaded successfully : " + file.getOriginalFilename());
    }


    @Override
    public byte[] downloadAvatar(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id","id",String.valueOf(id)));
        ImageData image = imageDataRepository.findImageDataByAvatarAndUser(true,user).orElseThrow(()-> new ResourceNotFoundException("image","image",String.valueOf(id)));
        byte[] images=ImageUtils.decompressImage(image.getImageData());
        return images;
    }
    @Override
    public List<Long> getListImage(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFoundException("id","id",String.valueOf(postId)));
        List<ImageData> image = imageDataRepository.findImageDataByPostAndPostId(true,postId).orElseThrow(()-> new ResourceNotFoundException("image","image",String.valueOf(postId)));
        List<Long> listImageId = new ArrayList<>();
        for(ImageData data : image){
            listImageId.add(data.getId());
        }
        return listImageId;
    }

    @Override
    public byte[] downloadPost(Long imageId) {
        ImageData image = imageDataRepository.findImageDataById(imageId).orElseThrow(()-> new ResourceNotFoundException("image","image",String.valueOf(imageId)));
        byte[] images=ImageUtils.decompressImage(image.getImageData());
        return images;
    }
}
