package com.example.HuceBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name= "chat_name")
    private  String chat_name;
    @Column(name= "chat_image")
    private String chat_image;
    @ManyToMany
    private Set<User> admins = new HashSet<>();
    @Column(name="is_group")
    private boolean isGroup;
    @ManyToOne
    private User createBy;
    @ManyToMany
    private Set<User> users = new HashSet<>();
    @OneToMany
    private List<Message> message = new ArrayList<>();

}
