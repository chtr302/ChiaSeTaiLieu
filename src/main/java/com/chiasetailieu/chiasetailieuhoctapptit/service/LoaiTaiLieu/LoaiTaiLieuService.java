package com.chiasetailieu.chiasetailieuhoctapptit.service.LoaiTaiLieu;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.LoaiTaiLieuModel.LoaiTaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.LoaiTaiLieuRepository.LoaiTaiLieuRepo;

@Service
public class LoaiTaiLieuService {
    
    @Autowired
    private LoaiTaiLieuRepo loaiTaiLieuRepo;

    public List<LoaiTaiLieu> getLoaiTaiLieu(){
        return loaiTaiLieuRepo.findAll();
    }
    public Optional<LoaiTaiLieu> searchTaiLieuBy(String maLoai){
        return loaiTaiLieuRepo.findById(maLoai);
    }
    
    public Optional<LoaiTaiLieu> getLoaiTaiLieuByMa(String maLoai) {
        return loaiTaiLieuRepo.findById(maLoai);
    }
}
