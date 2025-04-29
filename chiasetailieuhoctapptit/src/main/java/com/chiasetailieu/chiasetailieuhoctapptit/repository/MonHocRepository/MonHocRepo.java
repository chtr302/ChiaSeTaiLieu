package com.chiasetailieu.chiasetailieuhoctapptit.repository.MonHocRepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.MonHocModel.MonHoc;


@Repository
public interface MonHocRepo extends JpaRepository<MonHoc, String>{
    List<MonHoc> findByTenMon(String tenMon);
}
