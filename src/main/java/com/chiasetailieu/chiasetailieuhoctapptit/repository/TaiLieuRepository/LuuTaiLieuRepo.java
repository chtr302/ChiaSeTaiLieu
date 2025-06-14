package com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.LuuTaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.LuuTaiLieuView;

@Repository
public interface LuuTaiLieuRepo extends JpaRepository<LuuTaiLieu, LuuTaiLieuView> {
    
    // Tìm kiếm tài liệu đã được lưu bởi người dùng
    boolean existsByMaTaiLieuAndMaSinhVien(Integer maTaiLieu, String maSinhVien);
    
    // Lấy danh sách tài liệu đã lưu của người dùng
    List<LuuTaiLieu> findByMaSinhVien(String maSinhVien);
    
    // Lấy danh sách MaTaiLieu mà người dùng đã lưu
    @Query("SELECT l.maTaiLieu FROM LuuTaiLieu l WHERE l.maSinhVien = :maSinhVien")
    List<Integer> findMaTaiLieuByMaSinhVien(@Param("maSinhVien") String maSinhVien);
    
    // Lấy danh sách theo ngày lưu mới nhất
    @Query("SELECT l FROM LuuTaiLieu l WHERE l.maSinhVien = :maSinhVien ORDER BY l.ngayLuu DESC")
    List<LuuTaiLieu> findByMaSinhVienOrderByNgayLuuDesc(@Param("maSinhVien") String maSinhVien);
    
    // Xóa tất cả bản ghi theo MaTaiLieu và MaSinhVien
    void deleteByMaTaiLieuAndMaSinhVien(Integer maTaiLieu, String maSinhVien);
    
    
} 