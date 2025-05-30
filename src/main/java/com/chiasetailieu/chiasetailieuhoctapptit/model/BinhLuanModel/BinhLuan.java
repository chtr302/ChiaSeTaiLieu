package com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BinhLuan")
public class BinhLuan {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "MaBinhLuan")
    private long maBinhLuan;

    @Column(name = "NoiDung")
    private String noiDung;

    @Column(name = "NgayBinhLuan")
    private LocalDate ngayBinhLuan;

    @Column(name = "MaSinhVien")
    private String sinhVien;

    @Column(name = "MaTaiLieu")
    private long taiLieu;

    public BinhLuan(){
        this.ngayBinhLuan = LocalDate.now();
    }

    public long getMaBinhLuan() {
        return maBinhLuan;
    }

    public void setMaBinhLuan(long maBinhLuan) {
        this.maBinhLuan = maBinhLuan;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public LocalDate getNgayBinhLuan() {
        return ngayBinhLuan;
    }

    public void setNgayBinhLuan(LocalDate ngayBinhLuan) {
        this.ngayBinhLuan = ngayBinhLuan;
    }

    public String getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(String sinhVien) {
        this.sinhVien = sinhVien;
    }

    public long getTaiLieu() {
        return taiLieu;
    }

    public void setTaiLieu(long taiLieu) {
        this.taiLieu = taiLieu;
    }
    
}
