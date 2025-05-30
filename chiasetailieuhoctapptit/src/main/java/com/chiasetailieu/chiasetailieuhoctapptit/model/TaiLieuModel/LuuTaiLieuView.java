package com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel;

import java.io.Serializable;
import java.util.Objects;

public class LuuTaiLieuView implements Serializable {
    
    private Integer maTaiLieu;
    private String maSinhVien;
    
    public LuuTaiLieuView() {
        super();
    }
    
    public LuuTaiLieuView(Integer maTaiLieu, String maSinhVien) {
        super();
        this.maTaiLieu = maTaiLieu;
        this.maSinhVien = maSinhVien;
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
    
    @Override
    public int hashCode() {
        return Objects.hash(maTaiLieu, maSinhVien);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        LuuTaiLieuView other = (LuuTaiLieuView) obj;
        return Objects.equals(maTaiLieu, other.maTaiLieu) && 
               Objects.equals(maSinhVien, other.maSinhVien);
    }
} 