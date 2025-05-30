package com.chiasetailieu.chiasetailieuhoctapptit.model.MonHocModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="vw_MonHoc")
public class MonHoc {
    @Id
    @Column(name="MaMon")
    private String maMon;

    @Column(name="TenMon")
    private String tenMon;

    @Column(name="TaiLieu")
    private long taiLieu;

    public MonHoc(){}
    public MonHoc(String maMon, String tenMon){
        this.maMon = maMon;
        this.tenMon = tenMon;
    }

    public String getMaMon() {
        return maMon;
    }

    public void setMaMon(String maMon) {
        this.maMon = maMon;
    }

    public String getTenMon() {
        return tenMon;
    }

    public void setTenMon(String tenMon) {
        this.tenMon = tenMon;
    }

    public long getTaiLieu() {
        return taiLieu;
    }

    public void setTaiLieu(long taiLieu) {
        this.taiLieu = taiLieu;
    }

}
