package com.example.HuceBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "imageData")
@Builder
public class ImageData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "type",nullable = false)
    private String type;
    @Column(name = "imageData",length = 1000)
    private byte[] imageData;
    @Column(name = "avatar")
    private Boolean avatar;
    @Column(name = "post")
    private Boolean post;
    @Column(name = "postid")
    private Long postId;
    @Column(name = "comment")
    private Boolean comment;
    @Column(name = "commentid")
    private Long commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private User user;
}
