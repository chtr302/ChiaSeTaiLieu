package com.chiasetailieu.chiasetailieuhoctapptit.repository.BinhLuanRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BinhLuan;

@Repository
public interface BinhLuanRepo extends JpaRepository<BinhLuan, Integer> {
    public List<BinhLuan> findByTaiLieu(long id);

    @Query("SELECT " +
           "(SELECT COUNT(bl) FROM BinhLuan bl WHERE bl.sinhVien = :maSinhVien) + " +
           "(SELECT COUNT(tr) FROM TraLoiBinhLuan tr WHERE tr.maSinhVien = :maSinhVien)")
    long countCommentsOfSinhVien(@Param("maSinhVien") String maSV);
}
