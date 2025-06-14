package com.chiasetailieu.chiasetailieuhoctapptit.model.LichSuXemModel;

import java.time.LocalDateTime;

public class LichSuXemDTO {
    private Long maLichSu;
    private String maSinhVien;
    private Integer maTaiLieu;
    private String loaiHanhDong;
    private LocalDateTime thoiGian;
    
    // Thông tin tài liệu
    private String tieuDe;
    private String thumbnail;
    private String tenMon;
    private String tenLoai;
    private String duongDanFile;
    
    // Constructors
    public LichSuXemDTO() {}
    
    public LichSuXemDTO(Long maLichSu, String maSinhVien, Integer maTaiLieu, 
                       String loaiHanhDong, LocalDateTime thoiGian,
                       String tieuDe, String thumbnail, String tenMon, 
                       String tenLoai, String duongDanFile) {
        this.maLichSu = maLichSu;
        this.maSinhVien = maSinhVien;
        this.maTaiLieu = maTaiLieu;
        this.loaiHanhDong = loaiHanhDong;
        this.thoiGian = thoiGian;
        this.tieuDe = tieuDe;
        this.thumbnail = thumbnail;
        this.tenMon = tenMon;
        this.tenLoai = tenLoai;
        this.duongDanFile = duongDanFile;
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
    
    public String getLoaiHanhDong() {
        return loaiHanhDong;
    }
    
    public void setLoaiHanhDong(String loaiHanhDong) {
        this.loaiHanhDong = loaiHanhDong;
    }
    
    public LocalDateTime getThoiGian() {
        return thoiGian;
    }
    
    public void setThoiGian(LocalDateTime thoiGian) {
        this.thoiGian = thoiGian;
    }
    
    public String getTieuDe() {
        return tieuDe;
    }
    
    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }
    
    public String getThumbnail() {
        return thumbnail;
    }
    
    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
    
    public String getTenMon() {
        return tenMon;
    }
    
    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }
    
    public String getTenLoai() {
        return tenLoai;
    }
    
    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
    
    public String getDuongDanFile() {
        return duongDanFile;
    }
    
    public void setDuongDanFile(String duongDanFile) {
        this.duongDanFile = duongDanFile;
    }
} 