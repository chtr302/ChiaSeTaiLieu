package com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="TraLoiBinhLuan")
public class TraLoiBinhLuan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaTraLoi")
    private Long maTraLoi;

    @Column(name = "MaBinhLuan")
    private Integer maBinhLuan;

    @Column(name = "MaSinhVien")
    private String maSinhVien;

    @Column(name = "NoiDung")
    private String noiDung;

    @Column(name = "NgayTraLoi")
    private LocalDateTime ngayTraLoi;

    public Long getMaTraLoi() {
        return maTraLoi;
    }

    public Integer getMaBinhLuan() {
        return maBinhLuan;
    }

    public String getMaSinhVien() {
        return maSinhVien;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public LocalDateTime getNgayTraLoi() {
        return ngayTraLoi;
    }


}
