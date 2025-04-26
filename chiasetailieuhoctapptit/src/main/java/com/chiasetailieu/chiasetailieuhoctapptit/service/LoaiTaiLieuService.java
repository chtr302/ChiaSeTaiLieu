package com.chiasetailieu.chiasetailieuhoctapptit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.LoaiTaiLieu;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.LoaiTaiLieuRepo;

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
}
