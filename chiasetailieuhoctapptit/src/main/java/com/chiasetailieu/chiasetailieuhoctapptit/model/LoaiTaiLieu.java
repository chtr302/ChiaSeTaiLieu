package com.chiasetailieu.chiasetailieuhoctapptit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="LoaiTaiLieu")
public class LoaiTaiLieu {
    @Id
    @Column(name = "MaLoai")
    private String maLoai;

    @Column(name = "TenLoai")
    private String tenLoai;

    public LoaiTaiLieu(){}
    public LoaiTaiLieu (String maLoai, String tenLoai){
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
    }
    public String getMaLoai() {
        return maLoai;
    }
    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }
    public String getTenLoai() {
        return tenLoai;
    }
    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }
    
}
