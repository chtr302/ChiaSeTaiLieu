package com.chiasetailieu.chiasetailieuhoctapptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuView;

@Repository
public interface TaiLieuViewRepo extends JpaRepository<TaiLieuView, Long> {
    
}
