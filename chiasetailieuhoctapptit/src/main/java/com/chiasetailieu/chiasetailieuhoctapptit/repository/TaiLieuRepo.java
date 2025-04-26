package com.chiasetailieu.chiasetailieuhoctapptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieu;

@Repository
public interface TaiLieuRepo extends JpaRepository<TaiLieu, Integer> {
    
}
