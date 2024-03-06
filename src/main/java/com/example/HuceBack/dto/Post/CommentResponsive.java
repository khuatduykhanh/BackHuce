package com.example.HuceBack.dto.Post;

import com.example.HuceBack.entity.Post;
import com.example.HuceBack.entity.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponsive {
    private Long id;
    private String content;
}
