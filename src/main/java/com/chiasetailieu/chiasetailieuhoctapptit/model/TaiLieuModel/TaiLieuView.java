package com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vw_tailieu")
public class TaiLieuView {

    @Id
    @Column(name = "MaTaiLieu")
    private long maTaiLieu;

    @Column(name = "TieuDe")
    private String tieuDe;

    @Column(name = "LuotXem")
    private long luotXem;

    @Column(name = "TenMon")
    private String tenMon;

    @Column(name = "TenLoai")
    private String tenLoai;

    @Column(name = "DanhGia")
    private long danhGia;

    @Column(name = "SoLuongBL")
    private long soLuongBL;

    @Column(name = "Thumbnail")
    private String thumbnail;

    @Column(name = "DuongDanFile")
    private String duongDanFile;

    @Column(name = "MoTa")
    private String moTa;

    @Column(name = "Tags")
    private String tags;

    @Column(name = "MaSinhVien")
    private String maSinhVien;
    
    @Column(name = "HinhAnhSV")
    private String hinhAnhSV;
    
    @Column(name = "TenSinhVien")
    private String tenSinhVien;

    public long getMaTaiLieu() {
        return maTaiLieu;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public long getLuotXem() {
        return luotXem;
    }

    public String getTenMon() {
        return tenMon;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public long getDanhGia() {
        return danhGia;
    }

    public long getSoLuongBL() {
        return soLuongBL;
    }

    public String getThumbnail() {
        return thumbnail;
    }
    public String getDuongDanFile(){
        return duongDanFile;
    }

    public String getMoTa() {
        return moTa;
    }

    public String getTags() {
        return tags;
    }
    public String getMaSinhVien() {
        return maSinhVien;
    }
    
    public String getHinhAnhSV() {
        return hinhAnhSV;
    }
    
    public String getTenSinhVien() {
        return tenSinhVien;
    }

    public TaiLieuView orElse(Object object) {
        throw new UnsupportedOperationException("Unimplemented method 'orElse'");
    }
    
}
