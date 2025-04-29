package com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;

@Repository
public interface TaiLieuViewRepo extends JpaRepository<TaiLieuView, Long> {
    TaiLieuView findByMaTaiLieu(long maTaiLieu);
}
