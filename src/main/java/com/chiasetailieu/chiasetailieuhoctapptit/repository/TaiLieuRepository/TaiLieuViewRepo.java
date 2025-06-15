package com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;

@Repository
public interface TaiLieuViewRepo extends JpaRepository<TaiLieuView, Long> {
    TaiLieuView findByMaTaiLieu(long maTaiLieu);

    List<TaiLieuView> findByMaTaiLieuIn(List<Integer> maTaiLieuList);
    
    // Tìm kiếm và sắp xếp theo lượt xem giảm dần
    List<TaiLieuView> findByMaTaiLieuInOrderByLuotXemDesc(List<Integer> maTaiLieuList);
    
    // Tìm kiếm và sắp xếp theo số lượng bình luận giảm dần
    List<TaiLieuView> findByMaTaiLieuInOrderBySoLuongBLDesc(List<Integer> maTaiLieuList);
    
    // Tìm kiếm tài liệu theo MaSinhVien
    List<TaiLieuView> findByMaSinhVien(String maSinhVien);
    
    // Tìm kiếm tài liệu theo MaSinhVien và sắp xếp theo lượt xem giảm dần
    List<TaiLieuView> findByMaSinhVienOrderByLuotXemDesc(String maSinhVien);
    
    // Tìm kiếm tài liệu theo MaSinhVien và sắp xếp theo số lượng bình luận giảm dần
    List<TaiLieuView> findByMaSinhVienOrderBySoLuongBLDesc(String maSinhVien);
    
    // Tìm kiếm tài liệu theo MaSinhVien và sắp xếp theo ngày đăng tăng dần (cũ nhất)
    @Query("SELECT t FROM TaiLieuView t JOIN TaiLieu tl ON t.maTaiLieu = tl.maTaiLieu " +
           "WHERE t.maSinhVien = :maSinhVien ORDER BY tl.ngayDang ASC")
    List<TaiLieuView> findByMaSinhVienOrderByNgayDangAsc(@Param("maSinhVien") String maSinhVien);
    
    // Tìm kiếm tài liệu theo MaSinhVien và sắp xếp theo ngày đăng giảm dần (mới nhất)
    @Query("SELECT t FROM TaiLieuView t JOIN TaiLieu tl ON t.maTaiLieu = tl.maTaiLieu " +
           "WHERE t.maSinhVien = :maSinhVien ORDER BY tl.ngayDang DESC")
    List<TaiLieuView> findByMaSinhVienOrderByNgayDangDesc(@Param("maSinhVien") String maSinhVien);
}
