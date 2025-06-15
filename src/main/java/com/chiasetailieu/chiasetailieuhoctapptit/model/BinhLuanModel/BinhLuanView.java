package com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="vw_BinhLuan")
public class BinhLuanView {
    @Id
    @Column(name="MaBinhLuan")
    private long maBinhLuan;

    @Column(name="NoiDung")
    private String noiDung;

    @Column(name="NgayBinhLuan")
    private LocalDate ngayBinhLuan;

    @Column(name="HoVaTen")
    private String hoVaTen;

    @Column(name="HinhAnh")
    private  String hinhAnh;

    @Column(name="MaTaiLieu")
    private long maTaiLieu;

    @Column(name="MaSinhVien")
    private String maSV;

    public long getMaBinhLuan() {
        return maBinhLuan;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public LocalDate getNgayBinhLuan() {
        return ngayBinhLuan;
    }

    public String getHoVaTen() {
        return hoVaTen;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public long getMaTaiLieu() {
        return maTaiLieu;
    }

    public String getMaSV() {
        return maSV;
    }
}
