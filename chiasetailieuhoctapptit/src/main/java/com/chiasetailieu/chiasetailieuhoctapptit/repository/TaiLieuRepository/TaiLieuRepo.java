package com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieu;

@Repository
public interface TaiLieuRepo extends JpaRepository<TaiLieu, Long> {
}
