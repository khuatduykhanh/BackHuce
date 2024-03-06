package com.example.HuceBack.service;

import com.example.HuceBack.dto.Post.PostRequest;
import com.example.HuceBack.dto.Post.PostResponsive;
import com.example.HuceBack.dto.common.BaseResponse;
import com.example.HuceBack.dto.common.MessageBaseResponse;
import com.example.HuceBack.entity.Message;
import com.example.HuceBack.entity.Post;
import com.example.HuceBack.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface PostService {
    BaseResponse<PostResponsive> createPost(PostRequest postRequest, HttpServletRequest request) throws IOException;
    BaseResponse<PostResponsive> getPostById(Long id);
    BaseResponse<List<PostResponsive>> getAllPost();
    BaseResponse<List<PostResponsive>> getAllPostByUserId(Long userId);
    BaseResponse<PostResponsive> updatePost(Long postId, PostRequest postRequest, HttpServletRequest request) throws IOException;
    MessageBaseResponse deletePost(Long postId, HttpServletRequest request);
    MessageBaseResponse addLike(Long postId, HttpServletRequest request);
}
