package com.chiasetailieu.chiasetailieuhoctapptit.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="MonHoc")
public class MonHoc {
    @Id
    @Column(name="MaMon")
    private String maMon;

    @Column(name="TenMon")
    private String tenMon;

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

}
