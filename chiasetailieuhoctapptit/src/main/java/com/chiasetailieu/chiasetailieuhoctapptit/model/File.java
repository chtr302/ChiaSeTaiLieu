package com.chiasetailieu.chiasetailieuhoctapptit.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="File")
public class File {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="MaFile")
    private int maFile;

    @Column(name="TenFile")
    private String tenFile;

    @Column(name="DuongDanFile")
    private String duongDanFile;

    @Column(name="DinhDang")
    private String dinhDang;

    @Column(name="KichThuoc")
    private long kichThuoc;

    @Column(name="NgayTaiLen")
    private LocalDate ngayTaiLen;

    @Column(name = "Thumbnail")
    private String thumbnail;

    public File(){
        this.ngayTaiLen = LocalDate.now();
    }

    public int getMaFile() {
        return maFile;
    }

    public void setMaFile(int maFile) {
        this.maFile = maFile;
    }

    public String getTenFile() {
        return tenFile;
    }

    public void setTenFile(String tenFile) {
        this.tenFile = tenFile;
    }

    public String getDuongDanFile() {
        return duongDanFile;
    }

    public void setDuongDanFile(String duongDanFile) {
        this.duongDanFile = duongDanFile;
    }

    public String getDinhDang() {
        return dinhDang;
    }

    public void setDinhDang(String dinhDang) {
        this.dinhDang = dinhDang;
    }

    public long getKichThuoc() {
        return kichThuoc;
    }

    public void setKichThuoc(long kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public LocalDate getNgayTaiLen() {
        return ngayTaiLen;
    }

    public void setNgayTaiLen(LocalDate ngayTaiLen) {
        this.ngayTaiLen = ngayTaiLen;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
