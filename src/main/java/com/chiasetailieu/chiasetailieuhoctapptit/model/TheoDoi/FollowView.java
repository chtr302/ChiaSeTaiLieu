package com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vw_Follow")
public class FollowView {
    
    @Id
    @Column(name = "MaTD")
    private Long maTD;
    
    @Column(name = "NguoiDung")
    private String nguoiDung;
    
    @Column(name = "DangTheoDoi")
    private String dangTheoDoi;
    
    @Column(name = "HoTenND")
    private String hoTenND;
    
    @Column(name = "HinhAnhND")
    private String hinhAnhND;
    
    @Column(name = "HoTenDTD")
    private String hoTenDTD;
    
    @Column(name = "HinhAnhDTD")
    private String hinhAnhDTD;

    // Getters và Setters
    public Long getMaTD() {
        return maTD;
    }

    public void setMaTD(Long maTD) {
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

    public String getHoTenND() {
        return hoTenND;
    }

    public void setHoTenND(String hoTenND) {
        this.hoTenND = hoTenND;
    }

    public String getHinhAnhND() {
        return hinhAnhND;
    }

    public void setHinhAnhND(String hinhAnhND) {
        this.hinhAnhND = hinhAnhND;
    }

    public String getHoTenDTD() {
        return hoTenDTD;
    }

    public void setHoTenDTD(String hoTenDTD) {
        this.hoTenDTD = hoTenDTD;
    }

    public String getHinhAnhDTD() {
        return hinhAnhDTD;
    }

    public void setHinhAnhDTD(String hinhAnhDTD) {
        this.hinhAnhDTD = hinhAnhDTD;
    }
}