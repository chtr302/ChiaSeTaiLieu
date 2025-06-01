package com.chiasetailieu.chiasetailieuhoctapptit.repository.LichSuXemRepository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.LichSuXemModel.LichSuXem;
import com.chiasetailieu.chiasetailieuhoctapptit.model.LichSuXemModel.LichSuXemDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LichSuXemRepo extends JpaRepository<LichSuXem, Long> {
    
    // Tìm lịch sử theo sinh viên và tài liệu và loại hành động
    Optional<LichSuXem> findByMaSinhVienAndMaTaiLieuAndLoaiHanhDong(
        String maSinhVien, Integer maTaiLieu, LichSuXem.LoaiHanhDong loaiHanhDong);
    
    // Lấy 4 hoạt động gần nhất của sinh viên trong 7 ngày qua
    @Query("SELECT ls FROM LichSuXem ls WHERE ls.maSinhVien = :maSinhVien " +
           "AND ls.thoiGian >= :fromTime " +
           "ORDER BY ls.thoiGian DESC")
    List<LichSuXem> findTop4RecentActivities(@Param("maSinhVien") String maSinhVien, 
                                           @Param("fromTime") LocalDateTime fromTime);
    
    // Lấy lịch sử với thông tin tài liệu (4 hoạt động gần nhất trong 7 ngày)
    @Query("SELECT new com.chiasetailieu.chiasetailieuhoctapptit.model.LichSuXemModel.LichSuXemDTO(" +
           "ls.maLichSu, ls.maSinhVien, ls.maTaiLieu, CAST(ls.loaiHanhDong AS string), ls.thoiGian, " +
           "tv.tieuDe, tv.thumbnail, tv.tenMon, tv.tenLoai, tv.duongDanFile) " +
           "FROM LichSuXem ls " +
           "JOIN TaiLieuView tv ON ls.maTaiLieu = tv.maTaiLieu " +
           "WHERE ls.maSinhVien = :maSinhVien " +
           "AND ls.thoiGian >= :fromTime " +
           "ORDER BY ls.thoiGian DESC")
    List<LichSuXemDTO> findRecentActivitiesWithDocumentInfo(
        @Param("maSinhVien") String maSinhVien, 
        @Param("fromTime") LocalDateTime fromTime);
    
    // Xóa lịch sử cũ hơn 7 ngày
    @Modifying
    @Transactional
    @Query("DELETE FROM LichSuXem ls WHERE ls.thoiGian < :beforeTime")
    int deleteOldHistory(@Param("beforeTime") LocalDateTime beforeTime);
    
    // Xóa lịch sử cũ của một sinh viên cụ thể
    @Modifying
    @Transactional
    @Query("DELETE FROM LichSuXem ls WHERE ls.maSinhVien = :maSinhVien AND ls.thoiGian < :beforeTime")
    int deleteOldHistoryByUser(@Param("maSinhVien") String maSinhVien, 
                              @Param("beforeTime") LocalDateTime beforeTime);
    
    // Đếm số lượng lịch sử của sinh viên trong 7 ngày qua
    @Query("SELECT COUNT(ls) FROM LichSuXem ls WHERE ls.maSinhVien = :maSinhVien " +
           "AND ls.thoiGian >= :fromTime")
    long countRecentActivities(@Param("maSinhVien") String maSinhVien, 
                              @Param("fromTime") LocalDateTime fromTime);
    
    // Kiểm tra xem đã có lịch sử xem tài liệu này chưa (trong 1 giờ qua để tránh spam)
    @Query("SELECT ls FROM LichSuXem ls WHERE ls.maSinhVien = :maSinhVien " +
           "AND ls.maTaiLieu = :maTaiLieu " +
           "AND ls.loaiHanhDong = :loaiHanhDong " +
           "AND ls.thoiGian >= :fromTime")
    List<LichSuXem> findRecentSameActivity(@Param("maSinhVien") String maSinhVien,
                                         @Param("maTaiLieu") Integer maTaiLieu,
                                         @Param("loaiHanhDong") LichSuXem.LoaiHanhDong loaiHanhDong,
                                         @Param("fromTime") LocalDateTime fromTime);
} 