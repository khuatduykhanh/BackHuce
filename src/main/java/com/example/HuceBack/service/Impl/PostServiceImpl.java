package com.example.HuceBack.service.Impl;

import com.example.HuceBack.dto.Post.PostRequest;
import com.example.HuceBack.dto.Post.PostResponsive;
import com.example.HuceBack.dto.common.BaseResponse;
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
import com.example.HuceBack.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ImageService imageService;
    @Autowired
    private ImageDataRepository imageDataRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    @Transactional
    public BaseResponse<PostResponsive> createPost(PostRequest postRequest, HttpServletRequest request) throws IOException {
        User reqUser = jwtProvider.getUserFromRequest(request);
        Post post = new Post();
        post.setCap(postRequest.getCap());
        post.setUser(reqUser);
        post.setCountComment(0);
        post.setCreatedAt(java.time.LocalDateTime.now());
        Post newPost = postRepository.save(post);
        for(MultipartFile file:postRequest.getFiles()){
            imageService.uploadImagePost(file,request,newPost.getId());
        }
        List<Long> imageId = imageService.getListImage(post.getId());
        List<String> images = new ArrayList<>();
        for(Long image: imageId){
            images.add("http://localhost:8080/api/image/"+ image);
        }
        PostResponsive postResponsive = PostResponsive.builder()
                .id(post.getId())
                .cap(post.getCap())
                .countComment(post.getCountComment())
                .liked(post.getLiked().stream().map(User::getId).collect(Collectors.toList()))
                .user(post.getUser().getId())
                .images(images)
                .build();
        return new BaseResponse<>(postResponsive);
    }


    @Override
    public BaseResponse<PostResponsive> getPostById(Long id) {
        Post post = postRepository.findById(id).orElse(null);
        if(post == null){
            return new BaseResponse<>("không tìm thấy postid");
        }
        List<Long> imageId = imageService.getListImage(post.getId());
        List<String> images = new ArrayList<>();
        for(Long image: imageId){
            images.add("http://localhost:8080/api/image/"+ image);
        }
        PostResponsive postResponsive = PostResponsive.builder()
                .id(post.getId())
                .cap(post.getCap())
                .countComment(post.getCountComment())
                .liked(post.getLiked().stream().map(User::getId).collect(Collectors.toList()))
                .user(post.getUser().getId())
                .images(images)
                .build();
        return  new BaseResponse<>(postResponsive);
    }

    @Override
    public BaseResponse<List<PostResponsive>> getAllPost() {
        List<Post> post = postRepository.findAll();
        List<PostResponsive> postResponsive = post.stream().map(this::convertPostResponsive).toList();
        return new BaseResponse<>(postResponsive);
    }

    @Override
    public BaseResponse<List<PostResponsive>> getAllPostByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("userId","userId",String.valueOf(userId)));
        List<Post> post = postRepository.findAllByUser(user);
        List<PostResponsive> postResponsive = post.stream().map(this::convertPostResponsive).toList();
        return new BaseResponse<>(postResponsive);
    }

    @Override
    public BaseResponse<PostResponsive> updatePost(Long postId, PostRequest postRequest,HttpServletRequest request) throws IOException {
        User user = jwtProvider.getUserFromRequest(request);
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null || post.getUser() != user){
            return  new BaseResponse<>("khong tim thay postId");
        }
        post.setCap(postRequest.getCap());
        postRepository.save(post);
        List<ImageData> images = imageDataRepository.findImageDataByPostAndPostId(true,postId).orElseThrow(()-> new ResourceNotFoundException("image","image",String.valueOf(postId)));
        imageDataRepository.deleteAll(images);
        for(MultipartFile file:postRequest.getFiles()){
            imageService.uploadImagePost(file,request,post.getId());
        }
        List<Long> imageId = imageService.getListImage(post.getId());
        List<String> newImages = new ArrayList<>();
        for(Long image: imageId){
            newImages.add("http://localhost:8080/api/image/"+ image);
        }
        PostResponsive postResponsive = PostResponsive.builder()
                .id(post.getId())
                .cap(post.getCap())
                .countComment(post.getCountComment())
                .liked(post.getLiked().stream().map(User::getId).collect(Collectors.toList()))
                .user(post.getUser().getId())
                .images(newImages)
                .build();
        return new BaseResponse<>(postResponsive);
    }

    @Override
    public MessageBaseResponse deletePost(Long postId, HttpServletRequest request) {
        User user = jwtProvider.getUserFromRequest(request);
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null || post.getUser() != user){
            return  new MessageBaseResponse(0,"khong tim thay postId");
        }
        postRepository.delete(post);
        List<ImageData> images = imageDataRepository.findImageDataByPostAndPostId(true,postId).orElseThrow(()-> new ResourceNotFoundException("image","image",String.valueOf(postId)));
        imageDataRepository.deleteAll(images);
        return new MessageBaseResponse(1,"xoa thanh cong");
    }

    @Override
    public MessageBaseResponse addLike(Long postId, HttpServletRequest request) {
        User user = jwtProvider.getUserFromRequest(request);
        Post post = postRepository.findById(postId).orElse(null);
        if(post == null){
            return new MessageBaseResponse(0,"khong tim thay postId");
        }
        if(post.getLiked().contains(user)){
            post.getLiked().remove(user);
        }else{
            post.getLiked().add(user);
        }
        postRepository.save(post);
        return new MessageBaseResponse(1,"Like thanh cong");
    }

    private PostResponsive convertPostResponsive(Post post){
        List<Long> imageId = imageService.getListImage(post.getId());
        List<String> images = new ArrayList<>();
        for(Long image: imageId){
            images.add("http://localhost:8080/api/image/"+ image);
        }
        return PostResponsive.builder()
                .id(post.getId())
                .cap(post.getCap())
                .countComment(post.getCountComment())
                .liked(post.getLiked().stream().map(User::getId).collect(Collectors.toList()))
                .user(post.getUser().getId())
                .images(images)
                .build();
    }
}
