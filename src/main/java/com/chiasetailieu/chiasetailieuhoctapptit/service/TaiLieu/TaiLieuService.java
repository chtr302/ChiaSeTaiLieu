package com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository.TaiLieuRepo;
// import java.util.Optional;

@Service
public class TaiLieuService {

    @Autowired
    private TaiLieuRepo taiLieuRepo;
    
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
    

    public List<TaiLieu> getAllTaiLieu() {
        return taiLieuRepo.findAll();
    }

    public TaiLieu getTaiLieuById(long id) {
        return taiLieuRepo.findById(id).orElse(null);
    }

    @Transactional
    public void incrementViewCount(long id){
        TaiLieu taiLieu = taiLieuRepo.findById(id).orElse(null);
        if (taiLieu != null){
            Integer currentViews = taiLieu.getLuotXem();
            if (currentViews == null){currentViews=0;}
            taiLieu.setLuotXem(currentViews + 1);
            taiLieuRepo.save(taiLieu);
        }
    }
}