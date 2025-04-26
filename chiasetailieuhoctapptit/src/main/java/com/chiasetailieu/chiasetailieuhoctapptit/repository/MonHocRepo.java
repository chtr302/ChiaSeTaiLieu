package com.chiasetailieu.chiasetailieuhoctapptit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.MonHoc;


@Repository
public interface MonHocRepo extends JpaRepository<MonHoc, String>{
    List<MonHoc> findByTenMon(String tenMon);
}
