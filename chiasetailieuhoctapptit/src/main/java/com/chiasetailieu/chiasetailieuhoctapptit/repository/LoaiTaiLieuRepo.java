package com.chiasetailieu.chiasetailieuhoctapptit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.LoaiTaiLieu;

@Repository
public interface LoaiTaiLieuRepo extends JpaRepository<LoaiTaiLieu, String> {
    public List<LoaiTaiLieu> findByTenLoai(String tenLoai);
}
