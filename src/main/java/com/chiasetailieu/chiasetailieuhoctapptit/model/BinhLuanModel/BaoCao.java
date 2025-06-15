package com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "baocao")
public class BaoCao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaBaoCao")
    private Integer maBaoCao;

    @Column(name = "TieuDeBaoCao", length = 100)
    private String tieuDeBaoCao;

    @Column(name = "NoiDungBaoCao", length = 45)
    private String noiDungBaoCao;

    @Column(name = "NgayBaoCao")
    private LocalDate ngayBaoCao;

    @Column(name = "TrangThai")
    private Boolean trangThai;

    @Column(name = "MaTaiLieu")
    private Integer maTaiLieu;

    @Column(name = "MaSinhVien", length = 10)
    private String maSinhVien;

    // Constructors
    public BaoCao() {}

    public BaoCao(String tieuDeBaoCao, String noiDungBaoCao, LocalDate ngayBaoCao, 
                  Boolean trangThai, Integer maTaiLieu, String maSinhVien) {
        this.tieuDeBaoCao = tieuDeBaoCao;
        this.noiDungBaoCao = noiDungBaoCao;
        this.ngayBaoCao = ngayBaoCao;
        this.trangThai = trangThai;
        this.maTaiLieu = maTaiLieu;
        this.maSinhVien = maSinhVien;
    }

    // Getters and Setters
    public Integer getMaBaoCao() {
        return maBaoCao;
    }

    public void setMaBaoCao(Integer maBaoCao) {
        this.maBaoCao = maBaoCao;
    }

    public String getTieuDeBaoCao() {
        return tieuDeBaoCao;
    }

    public void setTieuDeBaoCao(String tieuDeBaoCao) {
        this.tieuDeBaoCao = tieuDeBaoCao;
    }

    public String getNoiDungBaoCao() {
        return noiDungBaoCao;
    }

    public void setNoiDungBaoCao(String noiDungBaoCao) {
        this.noiDungBaoCao = noiDungBaoCao;
    }

    public LocalDate getNgayBaoCao() {
        return ngayBaoCao;
    }

    public void setNgayBaoCao(LocalDate ngayBaoCao) {
        this.ngayBaoCao = ngayBaoCao;
    }

    public Boolean getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Boolean trangThai) {
        this.trangThai = trangThai;
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
} 