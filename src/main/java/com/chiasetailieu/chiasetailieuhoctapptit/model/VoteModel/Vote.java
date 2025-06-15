package com.chiasetailieu.chiasetailieuhoctapptit.model.VoteModel;

import java.time.LocalDateTime;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieu;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "Vote", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"MaTaiLieu", "MaSinhVien"})
})
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaVote")
    private Long maVote;

    @Column(name = "MaTaiLieu")
    private Long maTaiLieu;

    @Column(name = "MaSinhVien")
    private String maSinhVien;

    @Column(name = "LoaiVote") // 1 cho upvote, -1 cho downvote
    private Integer loaiVote;

    @Column(name = "NgayVote")
    private LocalDateTime ngayVote;

    @ManyToOne
    @JoinColumn(name = "MaTaiLieu", referencedColumnName = "MaTaiLieu", insertable = false, updatable = false)
    private TaiLieu taiLieu;

    @ManyToOne
    @JoinColumn(name = "MaSinhVien", referencedColumnName = "MaSinhVien", insertable = false, updatable = false)
    private SinhVien sinhVien;

    public Vote() {
        this.ngayVote = LocalDateTime.now();
    }

    public Vote(Long maTaiLieu, String maSinhVien, Integer loaiVote) {
        this.maTaiLieu = maTaiLieu;
        this.maSinhVien = maSinhVien;
        this.loaiVote = loaiVote;
        this.ngayVote = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getMaVote() {
        return maVote;
    }

    public void setMaVote(Long maVote) {
        this.maVote = maVote;
    }

    public Long getMaTaiLieu() {
        return maTaiLieu;
    }

    public void setMaTaiLieu(Long maTaiLieu) {
        this.maTaiLieu = maTaiLieu;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public void setMaSinhVien(String maSinhVien) {
        this.maSinhVien = maSinhVien;
    }

    public Integer getLoaiVote() {
        return loaiVote;
    }

    public void setLoaiVote(Integer loaiVote) {
        this.loaiVote = loaiVote;
    }

    public LocalDateTime getNgayVote() {
        return ngayVote;
    }

    public void setNgayVote(LocalDateTime ngayVote) {
        this.ngayVote = ngayVote;
    }

    public TaiLieu getTaiLieu() {
        return taiLieu;
    }

    public void setTaiLieu(TaiLieu taiLieu) {
        this.taiLieu = taiLieu;
    }

    public SinhVien getSinhVien() {
        return sinhVien;
    }

    public void setSinhVien(SinhVien sinhVien) {
        this.sinhVien = sinhVien;
    }
} 