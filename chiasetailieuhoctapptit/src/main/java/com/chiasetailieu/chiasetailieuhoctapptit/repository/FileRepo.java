package com.chiasetailieu.chiasetailieuhoctapptit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.chiasetailieu.chiasetailieuhoctapptit.model.File;

@Repository
public interface FileRepo extends JpaRepository<File, Integer> {
    
}
