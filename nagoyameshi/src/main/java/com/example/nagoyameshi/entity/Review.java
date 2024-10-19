package com.example.nagoyameshi.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "reviews")
@Data
public class Review {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     @Column(name = "id")
     private Integer id;
     
     @ManyToOne
     @JoinColumn(name = "store_id", nullable = false)
     private Storeinfo storeinfo; 
     
     @ManyToOne
     @JoinColumn(name = "memberinfo_id")
     private Memberinfo memberinfo;     

    private String comment;
    
    private int star;

    // No-argument constructor (required by JPA)
    public Review() {}

    // Constructor with all arguments
    public Review(Integer id, Storeinfo storeInfo, String comment, int star) {
        this.id = id;
        this.storeinfo = storeInfo;
        this.comment = comment;
        this.star = star;
    }
        @Column(name = "created_at")
        private LocalDateTime createdAt;

        @PrePersist
        protected void onCreate() {
            createdAt = LocalDateTime.now();
        }


    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Storeinfo getStoreInfo() {
        return storeinfo;
    }

    public void setStoreInfo(Storeinfo storeInfo) {
        this.storeinfo = storeInfo;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }
}
