package com.chiasetailieu.chiasetailieuhoctapptit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.TaiLieuView;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.TaiLieuViewRepo;

@Service
public class TaiLieuViewService {
    @Autowired
    private TaiLieuViewRepo taiLieuViewRepo;

    public List<TaiLieuView> getTaiLieu(){
        return taiLieuViewRepo.findAll();
    }

}
