package com.ak.drainage.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "category_tbl")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 255)
    private String name;
    @Column(nullable = false, length = 255)
    private String description;
    @Column(nullable = false, length = 255)
    private String thumbnail;
    @Column(nullable = false, length = 255)
    private Date createdAt;
    private Date updatedAt;
    @Column(nullable = false, length = 255)
    private String tag;
}
