package com.ak.drainage.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "subCategory_tbl")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false, length = 255)
    private String name;
    @Column(nullable = false, length = 255)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL , optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(nullable = false, length = 255)
    private String thumbnail;
    private Date createdAt;
    private Date updatedAt;
}
