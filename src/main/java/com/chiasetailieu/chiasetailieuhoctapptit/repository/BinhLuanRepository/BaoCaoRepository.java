package com.chiasetailieu.chiasetailieuhoctapptit.repository.BinhLuanRepository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BaoCao;

@Repository
public interface BaoCaoRepository extends JpaRepository<BaoCao, Integer> {
    // Additional query methods can be added here if needed
} 