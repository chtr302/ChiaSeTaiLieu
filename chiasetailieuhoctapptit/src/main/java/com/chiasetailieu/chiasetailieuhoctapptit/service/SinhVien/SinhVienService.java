package com.chiasetailieu.chiasetailieuhoctapptit.service.SinhVien;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.chiasetailieu.chiasetailieuhoctapptit.model.SinhVienModel.SinhVien;
import com.chiasetailieu.chiasetailieuhoctapptit.repository.SinhVienRepository.SinhVienRepo;

@Service
public class SinhVienService {
    @Autowired
    private SinhVienRepo sinhVienRepo;

    @Transactional
    public SinhVien saveOrUpdateSinhVien(OidcUser oidcUser){
        String hoVaTenGoc = (String) oidcUser.getClaims().get("name");
        String email = oidcUser.getEmail();
        String maSV = email.substring(0, email.indexOf('@'));
        String hoVaTen = hoVaTenGoc.substring(hoVaTenGoc.indexOf(' '));
        String hinhAnhURL = (String) oidcUser.getClaims().get("picture");

        Optional <SinhVien> existingSinhVien = sinhVienRepo.findByEmail(email);
        if (existingSinhVien.isPresent()){
            SinhVien sinhVien = existingSinhVien.get();
            sinhVien.setLastLogin(LocalDate.now());
            return sinhVienRepo.save(sinhVien);
        }
        else {
            SinhVien sinhVien = new SinhVien(maSV, hoVaTen, email, hinhAnhURL);
            return sinhVienRepo.save(sinhVien);
        }
    }
    public Optional<SinhVien> findByEmail (String email){
        return sinhVienRepo.findByEmail(email);
    }
    public SinhVien getSinhVienbyMaSinhVien(String id){
        return sinhVienRepo.findByMaSV(id);
    }
}
