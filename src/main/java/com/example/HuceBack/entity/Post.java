package com.example.HuceBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String cap;
    @OneToMany
    private List<User> liked = new ArrayList<>();
    private Integer countComment;
    private LocalDateTime createdAt;
    @ManyToOne
    private User user;
}
