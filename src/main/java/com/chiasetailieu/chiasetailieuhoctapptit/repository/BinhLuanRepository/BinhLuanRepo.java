package com.chiasetailieu.chiasetailieuhoctapptit.repository.BinhLuanRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BinhLuan;

@Repository
public interface BinhLuanRepo extends JpaRepository<BinhLuan, Integer> {
    public List<BinhLuan> findByTaiLieu(long id);
}
