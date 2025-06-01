package com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

@Entity
@Table(name = "LuuTaiLieu")
@IdClass(LuuTaiLieuView.class)
public class LuuTaiLieu implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "MaTaiLieu")
    private Integer maTaiLieu;
    
    @Id
    @Column(name = "MaSinhVien")
    private String maSinhVien;
    
    @Column(name = "TrangThaiLuu")
    private Boolean trangThaiLuu;
    
    @Column(name = "NgayLuu")
    private LocalDate ngayLuu;
    
    public LuuTaiLieu() {
        super();
        this.ngayLuu = LocalDate.now();
        this.trangThaiLuu = true;
    }
    
    public LuuTaiLieu(Integer maTaiLieu, String maSinhVien) {
        super();
        this.maTaiLieu = maTaiLieu;
        this.maSinhVien = maSinhVien;
        this.ngayLuu = LocalDate.now();
        this.trangThaiLuu = true;
    }
    
    public Integer getMaTaiLieu() {
        return maTaiLieu;
    }
    
    public void setMaTaiLieu(Integer maTaiLieu) {
        this.maTaiLieu = maTaiLieu;
    }
    
    public String getMaSinhVien() {
        return maSinhVien;
    }
    
    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }
    
    public Boolean getTrangThaiLuu() {
        return trangThaiLuu;
    }
    
    public void setTrangThaiLuu(Boolean trangThaiLuu) {
        this.trangThaiLuu = trangThaiLuu;
    }
    
    public LocalDate getNgayLuu() {
        return ngayLuu;
    }
    
    public void setNgayLuu(LocalDate ngayLuu) {
        this.ngayLuu = ngayLuu;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(maTaiLieu, maSinhVien);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LuuTaiLieu other = (LuuTaiLieu) obj;
        return Objects.equals(maTaiLieu, other.maTaiLieu) && 
               Objects.equals(maSinhVien, other.maSinhVien);
    }
} 