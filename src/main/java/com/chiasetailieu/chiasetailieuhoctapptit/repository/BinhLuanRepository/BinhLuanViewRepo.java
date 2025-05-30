package com.chiasetailieu.chiasetailieuhoctapptit.repository.BinhLuanRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.BinhLuanModel.BinhLuanView;

@Repository
public interface BinhLuanViewRepo extends JpaRepository<BinhLuanView, Long> {
    List<BinhLuanView> findByMaTaiLieu(long maTaiLieu);
}
