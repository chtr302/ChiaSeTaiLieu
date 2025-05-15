package com.chiasetailieu.chiasetailieuhoctapptit.model.TheoDoi;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "vw_ThongKeFollow")
public class Follow_VW {
    @Id
    @Column(name = "MaSinhVien")
    private String maSV;

    @Column(name = "HoVaTen")
    private String hoVaTen;

    @Column(name = "HinhAnh")
    private String hinhAnh;

    @Column(name = "Follow_ing")
    private int following;

    @Column(name = "Follower")
    private int follower;

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

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }
}
