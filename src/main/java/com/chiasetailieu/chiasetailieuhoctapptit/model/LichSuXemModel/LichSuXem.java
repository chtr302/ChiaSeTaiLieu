package com.chiasetailieu.chiasetailieuhoctapptit.model.LichSuXemModel;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "LichSuXem")
public class LichSuXem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaLichSu")
    private Long maLichSu;
    
    @Column(name = "MaSinhVien", nullable = false, length = 10)
    private String maSinhVien;
    
    @Column(name = "MaTaiLieu", nullable = false)
    private Integer maTaiLieu;
    
    @Column(name = "LoaiHanhDong", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private LoaiHanhDong loaiHanhDong;
    
    @Column(name = "ThoiGian", nullable = false)
    private LocalDateTime thoiGian;
    
    // Enum for action types
    public enum LoaiHanhDong {
        VIEW, SAVE
    }
    
    // Default constructor
    public LichSuXem() {
        this.thoiGian = LocalDateTime.now();
    }
    
    // Constructor with parameters
    public LichSuXem(String maSinhVien, Integer maTaiLieu, LoaiHanhDong loaiHanhDong) {
        this.maSinhVien = maSinhVien;
        this.maTaiLieu = maTaiLieu;
        this.loaiHanhDong = loaiHanhDong;
        this.thoiGian = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getMaLichSu() {
        return maLichSu;
    }
    
    public void setMaLichSu(Long maLichSu) {
        this.maLichSu = maLichSu;
    }
    
    public String getMaSinhVien() {
        return maSinhVien;
    }
    
    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }
    
    public Integer getMaTaiLieu() {
        return maTaiLieu;
    }
    
    public void setMaTaiLieu(Integer maTaiLieu) {
        this.maTaiLieu = maTaiLieu;
    }
    
    public LoaiHanhDong getLoaiHanhDong() {
        return loaiHanhDong;
    }
    
    public void setLoaiHanhDong(LoaiHanhDong loaiHanhDong) {
        this.loaiHanhDong = loaiHanhDong;
    }
    
    public LocalDateTime getThoiGian() {
        return thoiGian;
    }
    
    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }
} 