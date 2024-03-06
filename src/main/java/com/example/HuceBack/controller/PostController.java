package com.example.HuceBack.controller;

import com.example.HuceBack.dto.Image.ImageRequest;
import com.example.HuceBack.dto.Post.PostRequest;
import com.example.HuceBack.service.ImageService;
import com.example.HuceBack.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private ImageService imageService;
    @PostMapping()
    public ResponseEntity<?> createPost(@ModelAttribute PostRequest postRequest, HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(postService.createPost(postRequest,request));
    }
//    @PostMapping(consumes =  MediaType.MULTIPART_FORM_DATA_VALUE,
//            produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Object> saveNewTeacher( @RequestParam(value = "cap") String cap,
//                                                  @RequestParam(value = "files") List<MultipartFile> files, HttpServletRequest request) throws IOException {
//        PostRequest postRequest = new PostRequest(cap,files);
//        return ResponseEntity.ok(postService.createPost(postRequest,request));
//    }

//    @PostMapping("/uploadImagePost")
//    public ResponseEntity<?> uploadImagePost(@Valid @ModelAttribute ImageRequest imageRequest, HttpServletRequest request) throws IOException {
//        return ResponseEntity.ok(imageService.uploadImagePost(imageRequest.getFile(),request,imageRequest.getPostId()));
//    }
    @PutMapping("/{postId}")
    public ResponseEntity<?> putPost(@PathVariable("postId") Long postId, @ModelAttribute PostRequest postRequest, HttpServletRequest request) throws IOException {
        return ResponseEntity.ok(postService.updatePost(postId,postRequest,request));
    }
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        return ResponseEntity.ok(postService.deletePost(postId,request));
    }
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPost(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }
    @GetMapping()
    public ResponseEntity<?> getAllPost() {
        return ResponseEntity.ok(postService.getAllPost());
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getAllPostByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok(postService.getAllPostByUserId(userId));
    }
    @PutMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable("postId") Long postId, HttpServletRequest request) {
        return ResponseEntity.ok(postService.addLike(postId,request));
    }
}
