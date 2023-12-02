package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Repository.IChuongRepository; // Change the import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChuongService {
    @Autowired
    private IChuongRepository chuongRepository; // Change the variable name

    public List<Chuong> getAllChuongs() {
        return chuongRepository.findAll();
    }

    public Chuong getChuongById(Long id) {
        return chuongRepository.findById(id).orElse(null);
    }

    public List<Chuong> getChuongsByUserId(Long userId) {
        return chuongRepository.findAllByUserId(userId);
    }  public List<Chuong> getChuongsByTruyenId(Long truyenId) {
        return chuongRepository.findAllByTruyenId(truyenId);
    }


    public void updateChuong(Chuong chuong) {
        chuongRepository.save(chuong);
    }

    public void deleteChuong(Long id) {
        chuongRepository.deleteById(id);
    }

    public void addChuongs(Chuong chuong) { // Kiểm tra xem user đã được thiết lập hay chưa
        chuongRepository.save(chuong);
    }
    public boolean isChuongBelongsToTruyen(Long truyenId, Long chuongId) {
        Optional<Chuong> chuong = chuongRepository.getChuongById(chuongId);
        return chuong.isPresent() && chuong.get().getTruyen().getId().equals(truyenId);
    }

}
