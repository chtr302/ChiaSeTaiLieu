package com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "SinhVien")
public class SinhVien {
    @Id
    @Column(name="MaSinhVien")
    private String maSV;

    @Column(name="HoVaTen")
    private String hoVaTen;

    @Column(name="Email")
    private String email;

    @Column(name="HinhAnh")
    private String hinhAnh;

    @Column(name="NgayTao")
    private LocalDate ngayTao;

    @Column(name="LastLogin")
    private LocalDate lastLogin;

    public SinhVien() {
        this.ngayTao = LocalDate.now();
        this.lastLogin = LocalDate.now();
    }
    public SinhVien(String maSV, String hoVaTen, String email, String hinhAnh){
        this.maSV = maSV.toUpperCase();
        this.hoVaTen = hoVaTen;
        this.email = email;
        this.hinhAnh = hinhAnh;
        this.ngayTao = LocalDate.now();
        this.lastLogin = LocalDate.now();
    }

    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public void setHoVaTen(String hoVaTen) {
        this.hoVaTen = hoVaTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public LocalDate getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDate ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDate getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDate lastLogin) {
        this.lastLogin = lastLogin;
    }

}