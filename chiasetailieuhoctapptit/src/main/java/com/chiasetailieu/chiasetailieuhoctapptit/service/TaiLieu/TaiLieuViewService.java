package com.chiasetailieu.chiasetailieuhoctapptit.service.TaiLieu;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuModel.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuRepository.TaiLieuViewRepo;

@Service
public class TaiLieuViewService {
    @Autowired
    private TaiLieuViewRepo taiLieuViewRepo;

    public List<TaiLieuView> getTaiLieu(){
        return taiLieuViewRepo.findAll();
    }

    public TaiLieuView findByMaTaiLieu(Long id){
        return  taiLieuViewRepo.findByMaTaiLieu(id);
    }
}
