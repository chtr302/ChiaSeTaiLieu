package com.chiasetailieu.chiasetailieuhoctapptit.model;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="TaiLieu")
public class TaiLieu {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="MaTaiLieu")
    private int maTaiLieu;

    @Column(name="TieuDe")
    private String tieuDe;

    @Column(name="MoTa", columnDefinition = "TEXT")
    private String moTa;

    @Column(name = "LuotXem")
    private Integer luotXem;

    @Column(name = "NgayDang")
    private LocalDate ngayDang;

    @Column(name = "UpVote")
    private Integer upVote;

    @Column(name = "DownVote")
    private Integer downVote;

    @Column(name = "MonHoc")
    private String maMonHoc;

    @Column(name = "MaLoai")
    private Integer maLoai;

    @Column(name = "MaSinhVien")
    private String maSinhVien;

    @Column(name = "MaFile")
    private Long maFile;

    @ManyToOne
    @JoinColumn(name = "MaSinhVien", referencedColumnName = "MaSinhVien", insertable = false, updatable = false)
    private SinhVien sinhVien;

    @ManyToOne
    @JoinColumn(name = "MaFile", referencedColumnName = "MaFile", insertable = false, updatable = false)
    private File file;

    public TaiLieu() {
        this.luotXem = 0;
        this.upVote = 0;
        this.downVote = 0;
        this.ngayDang = LocalDate.now();
    }

    public int getMaTaiLieu() {
        return maTaiLieu;
    }

    public void setMaTaiLieu(int maTaiLieu) {
        this.maTaiLieu = maTaiLieu;
    }

    public String getTieuDe() {
        return tieuDe;
    }

    public void setTieuDe(String tieuDe) {
        this.tieuDe = tieuDe;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Integer getLuotXem() {
        return luotXem;
    }

    public void setLuotXem(Integer luotXem) {
        this.luotXem = luotXem;
    }

    public LocalDate getNgayDang() {
        return ngayDang;
    }

    public void setNgayDang(LocalDate ngayDang) {
        this.ngayDang = ngayDang;
    }

    public Integer getUpVote() {
        return upVote;
    }

    public void setUpVote(Integer upVote) {
        this.upVote = upVote;
    }

    public Integer getDownVote() {
        return downVote;
    }

    public void setDownVote(Integer downVote) {
        this.downVote = downVote;
    }

    public String getMaMonHoc() {
        return maMonHoc;
    }

    public void setMaMonHoc(String maMonHoc) {
        this.maMonHoc = maMonHoc;
    }

    public Integer getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(Integer maLoai) {
        this.maLoai = maLoai;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public Long getMaFile() {
        return maFile;
    }

    public void setMaFile(Long maFile) {
        this.maFile = maFile;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

}
