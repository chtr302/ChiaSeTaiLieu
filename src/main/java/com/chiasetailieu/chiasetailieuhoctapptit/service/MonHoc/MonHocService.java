package com.chiasetailieu.chiasetailieuhoctapptit.service.MonHoc;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chiasetailieu.chiasetailieuhoctapptit.model.MonHocModel.MonHoc;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.MonHocRepository.MonHocRepo;

@Service
public class MonHocService {
    
    @Autowired
    private MonHocRepo monHocRepo;

    public List<MonHoc> getAllMonHoc(){
        return monHocRepo.findAll();
    }
    public Optional<MonHoc> getMonHocByMa(String maMon){
        return monHocRepo.findById(maMon);
    }
    public List<MonHoc> searchMonHoc(String tenMon){
        return monHocRepo.findByTenMon(tenMon);
    }
    
    public List<MonHoc> getTopMonHocByTaiLieu(int limit) {
        return monHocRepo.findAll().stream()
                .sorted((m1, m2) -> Long.compare(m2.getTaiLieu(), m1.getTaiLieu()))
                .limit(limit)
                .toList();
    }
}
