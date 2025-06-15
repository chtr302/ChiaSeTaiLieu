package com.chiasetailieu.chiasetailieuhoctapptit.service.LichSuXem;

import com.chiasetailieu.chiasetailieuhoctapptit.model.LichSuXemModel.LichSuXem;
import com.chiasetailieu.chiasetailieuhoctapptit.model.LichSuXemModel.LichSuXemDTO;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.LichSuXemRepository.LichSuXemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LichSuXemService {
    
    @Autowired
    private LichSuXemRepo lichSuXemRepo;
    
    /**
     * Ghi lại hoạt động xem/lưu tài liệu
     * @param maSinhVien Mã sinh viên
     * @param maTaiLieu Mã tài liệu
     * @param loaiHanhDong Loại hành động (VIEW hoặc SAVE)
     */
    @Async
    @Transactional
    public void recordActivity(String maSinhVien, Integer maTaiLieu, LichSuXem.LoaiHanhDong loaiHanhDong) {
        try {
            // Kiểm tra xem có hoạt động tương tự trong 1 giờ qua không (để tránh spam)
            LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
            List<LichSuXem> recentSameActivities = lichSuXemRepo.findRecentSameActivity(
                maSinhVien, maTaiLieu, loaiHanhDong, oneHourAgo
            );
            
            // Nếu đã có hoạt động tương tự trong 1 giờ qua, không ghi lại nữa
            if (!recentSameActivities.isEmpty()) {
                return;
            }
            
            // Tạo bản ghi lịch sử mới
            LichSuXem lichSuXem = new LichSuXem(maSinhVien, maTaiLieu, loaiHanhDong);
            lichSuXemRepo.save(lichSuXem);
            
        } catch (Exception e) {
            // Log error nhưng không throw exception để không ảnh hưởng đến chức năng chính
            System.err.println("Lỗi khi ghi lại lịch sử xem: " + e.getMessage());
        }
    }
    
    /**
     * Lấy 4 hoạt động gần nhất của sinh viên trong 7 ngày qua
     * @param maSinhVien Mã sinh viên
     * @return Danh sách lịch sử với thông tin tài liệu
     */
    public List<LichSuXemDTO> getRecentActivities(String maSinhVien) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        List<LichSuXemDTO> activities = lichSuXemRepo.findRecentActivitiesWithDocumentInfo(maSinhVien, sevenDaysAgo);
        
        // Giới hạn 4 hoạt động gần nhất
        return activities.size() > 4 ? activities.subList(0, 4) : activities;
    }
    
    /**
     * Đếm số lượng hoạt động gần đây của sinh viên
     * @param maSinhVien Mã sinh viên
     * @return Số lượng hoạt động trong 7 ngày qua
     */
    public long countRecentActivities(String maSinhVien) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return lichSuXemRepo.countRecentActivities(maSinhVien, sevenDaysAgo);
    }
    
    /**
     * Xóa lịch sử cũ hơn 7 ngày (chạy tự động hàng ngày)
     * Chạy vào 2h sáng mỗi ngày
     */
    @Scheduled(cron = "0 0 2 * * ?")
    @Transactional
    public void cleanupOldHistory() {
        try {
            LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
            int deletedCount = lichSuXemRepo.deleteOldHistory(sevenDaysAgo);
            System.out.println("Đã xóa " + deletedCount + " bản ghi lịch sử cũ hơn 7 ngày");
        } catch (Exception e) {
            System.err.println("Lỗi khi xóa lịch sử cũ: " + e.getMessage());
        }
    }
    
    /**
     * Xóa lịch sử cũ của một sinh viên cụ thể
     * @param maSinhVien Mã sinh viên
     * @return Số lượng bản ghi đã xóa
     */
    @Transactional
    public int cleanupOldHistoryByUser(String maSinhVien) {
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        return lichSuXemRepo.deleteOldHistoryByUser(maSinhVien, sevenDaysAgo);
    }
    
    /**
     * Ghi lại hoạt động xem tài liệu
     * @param maSinhVien Mã sinh viên
     * @param maTaiLieu Mã tài liệu
     */
    public void recordViewActivity(String maSinhVien, Integer maTaiLieu) {
        recordActivity(maSinhVien, maTaiLieu, LichSuXem.LoaiHanhDong.VIEW);
    }
    
    /**
     * Ghi lại hoạt động lưu tài liệu
     * @param maSinhVien Mã sinh viên
     * @param maTaiLieu Mã tài liệu
     */
    public void recordSaveActivity(String maSinhVien, Integer maTaiLieu) {
        recordActivity(maSinhVien, maTaiLieu, LichSuXem.LoaiHanhDong.SAVE);
    }
} 