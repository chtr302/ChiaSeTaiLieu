package com.chiasetailieu.chiasetailieuhoctapptit.repository.SinhVienRepository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;

@Repository
public interface SinhVienRepo extends JpaRepository<SinhVien, String> {
    Optional<SinhVien> findByEmail(String email);
    SinhVien findByMaSV(String maSV);
}
