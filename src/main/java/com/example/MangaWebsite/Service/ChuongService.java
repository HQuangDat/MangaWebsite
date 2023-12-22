package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.*;
import com.example.MangaWebsite.Repository.IChuongRepository; // Change the import
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChuongService {
    @Autowired
    private IChuongRepository chuongRepository; // Change the variable name
    @Autowired
    private ChuongUserService chuongUserService;

    public List<Chuong> getAllChuongs() {
        return chuongRepository.findAll();
    }

    public Chuong getChuongById(Long id) {
        return chuongRepository.findById(id).orElse(null);
    }

    public List<Chuong> getChuongsByUserId(Long userId) {
        return chuongRepository.findAllByUserId(userId);
    }
    public List<Chuong> getChuongsByTruyenId(Long truyenId) {
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

    public List<ChartData> getChartDataForLastWeekChuong() {
        List<Chuong> chaptersInWeek = getChuongInWeek();

        Map<String, Long> chaptersByDay = chaptersInWeek.stream()
                .collect(Collectors.groupingBy(chuong ->
                        chuong.getNgayDang().getDayOfWeek().toString(), Collectors.counting()));

        List<ChartData> chartData = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            String day = dayOfWeek.toString();
            Long numberOfChapters = chaptersByDay.getOrDefault(day, 0L);
            chartData.add(new ChartData(day, numberOfChapters));
        }

        return chartData;
    }

    public Chuong getFirstChuongOfTruyen( Long truyenid) {
        List<Chuong> ChuongDau =  getChuongsByTruyenId(truyenid);
        if (!ChuongDau.isEmpty()) {
            return ChuongDau.get(0);
        } else {
            return null;
        }
    }

    public List<Chuong> getChuongInWeek() {
        LocalDateTime startOfWeek = LocalDateTime.now().minusWeeks(1);
        return chuongRepository.findChuongByNgayDangInWeek(startOfWeek);
    }

    public List<Chuong> getLockedChuongsByTruyenId(Long truyenId) {

        List<Chuong> allChuongs = chuongRepository.getChuongsByTruyenId(truyenId);

        return allChuongs.stream()
                .filter(Chuong::isLocked)
                .collect(Collectors.toList());
    }

    public List<Chuong> getChuongsWithUserStatus(Long truyenId, User userId) {
        List<Chuong> allChuongs = getChuongsByTruyenId(truyenId);
        List<Chuong> updatedChuongs = new ArrayList<>();

        for (Chuong chuong : allChuongs) {
            if (chuong.isLocked()) {
                chuong.setLocked(!chuongUserService.userBoughtChuong(chuong, userId));
            }
            updatedChuongs.add(chuong);
        }

        return updatedChuongs;
    }


    public List<Chuong> getAllChuongsOfUser(Long userId) {
        return chuongRepository.findAllByUserId(userId);
    }
}

