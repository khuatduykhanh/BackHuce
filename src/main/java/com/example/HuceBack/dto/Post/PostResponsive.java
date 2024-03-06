package com.example.HuceBack.dto.Post;

import com.example.HuceBack.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostResponsive {
    private Long id;
    private String cap;
    private List<Long> liked;
    private Integer countComment;
    private Long user;
    private List<String> images;
    private List<CommentResponsive> comments;
}
