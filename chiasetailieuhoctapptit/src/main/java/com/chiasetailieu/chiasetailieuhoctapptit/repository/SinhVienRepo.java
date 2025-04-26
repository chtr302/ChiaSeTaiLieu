package com.chiasetailieu.chiasetailieuhoctapptit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVien;

@Repository
public interface SinhVienRepo extends JpaRepository<SinhVien, String> {
    Optional<SinhVien> findByEmail(String email);
}
