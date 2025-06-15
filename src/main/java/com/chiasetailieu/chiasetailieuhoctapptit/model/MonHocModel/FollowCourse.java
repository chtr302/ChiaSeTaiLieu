package com.chiasetailieu.chiasetailieuhoctapptit.model.MonHocModel;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "TheoDoiMonHoc", uniqueConstraints = @UniqueConstraint(columnNames = {"MaSinhVien", "MaMon"}))
public class FollowCourse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "MaSinhVien", nullable = false)
    private String maSinhVien;

    @Column(name = "MaMon", nullable = false)
    private String maMon;

    @Column(name = "FollowedAt")
    private LocalDateTime followedAt = LocalDateTime.now();

    public FollowCourse() {}

    public FollowCourse(String maSinhVien, String maMon) {
        this.maSinhVien = maSinhVien;
        this.maMon = maMon;
        this.followedAt = LocalDateTime.now();
    }

    // Getters and setters...
    public Long getId() { return id; }
    public String getMaSinhVien() { return maSinhVien; }
    public void setMaSinhVien(String maSinhVien) { this.maSinhVien = maSinhVien; }
    public String getMaMon() { return maMon; }
    public void setMaMon(String maMon) { this.maMon = maMon; }
    public LocalDateTime getFollowedAt() { return followedAt; }
    public void setFollowedAt(LocalDateTime followedAt) { this.followedAt = followedAt; }
}
