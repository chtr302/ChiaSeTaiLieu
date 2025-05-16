package com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "TheoDoi")
public class TheoDoi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTD")
    private int maTD;

    @Column(name = "NguoiDung")
    private String nguoiDung;

    @Column(name = "DangTheoDoi")
    private String dangTheoDoi;

    @Column(name = "TrangThai")
    private int trangThai;

    public TheoDoi() {
        this.trangThai = 0;
    }

    public int getMaTD() {
        return maTD;
    }

    public void setMaTD(int maTD) {
        this.maTD = maTD;
    }

    public String getNguoiDung() {
        return nguoiDung;
    }

    public void setNguoiDung(String nguoiDung) {
        this.nguoiDung = nguoiDung;
    }

    public String getDangTheoDoi() {
        return dangTheoDoi;
    }

    public void setDangTheoDoi(String dangTheoDoi) {
        this.dangTheoDoi = dangTheoDoi;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    
}
