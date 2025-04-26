package com.chiasetailieu.chiasetailieuhoctapptit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// import com.chiasetailieu.chiasetailieuhoctapptit.model.File;
import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepo;

import java.time.LocalDate;
import java.util.List;
// import java.util.Optional;

@Service
public class TaiLieuService {

    @Autowired
    private TaiLieuRepo taiLieuRepo;
    
    /**
     * Lưu thông tin tài liệu vào database
     */
    @Transactional
    public TaiLieu saveTaiLieu(TaiLieu taiLieu) {
        // Thiết lập các giá trị mặc định nếu chưa có
        if (taiLieu.getLuotXem() == null) {
            taiLieu.setLuotXem(0);
        }
        if (taiLieu.getUpVote() == null) {
            taiLieu.setUpVote(0);
        }
        if (taiLieu.getDownVote() == null) {
            taiLieu.setDownVote(0);
        }
        if (taiLieu.getNgayDang() == null) {
            taiLieu.setNgayDang(LocalDate.now());
        }
        
        return taiLieuRepo.save(taiLieu);
    }
    
    /**
     * Lấy tất cả tài liệu
     */
    public List<TaiLieu> getAllTaiLieu() {
        return taiLieuRepo.findAll();
    }
}