package com.example.HuceBack.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users",uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {
    @Id
    private Long id;
    @Column(name = "name",nullable = false)
    private String name;
    @Column(name = "email",nullable = false)
    private String email;
    @Column(name = "department",nullable = true)
    private String department;
    @Column(name = "lop",nullable = true)
    private String lop;
    @Column(name = "password",nullable = false)
    private String password;
    @Column(name = "isEnabled",nullable = false)
    private boolean isEnabled = false;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL) // Loại lấy dữ liệu này có nghĩa là dữ liệu liên quan sẽ được tải ngay lập tức, hoặc ngay cùng với thực thể chính.
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id",referencedColumnName = "id"), // đánh dấu id là khoá ngoại của bảng User và có tên là user_roles
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") // đánh dấu id của bảng role là khoá ngoại và có tên là role_id
    )
    private Set<Role> roles;
}
