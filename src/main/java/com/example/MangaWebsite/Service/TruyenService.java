package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.*;
import com.example.MangaWebsite.Repository.IChuongRepository;
import com.example.MangaWebsite.Repository.IMangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TruyenService {
    @Autowired
    private IMangaRepository truyenRepository;
    @Autowired
    private IChuongRepository chuongRepository;

    public List<Truyen> getAllTruyens() {
        return truyenRepository.findAll();
    }

    public Truyen getTruyenById(Long id) {
        return truyenRepository.findById(id).orElse(null);
    }

    public List<Truyen> getTruyensByUserId(Long userId) {
        return truyenRepository.findAllByUser_Id(userId);
    }

    ;

    public Truyen addTruyen(Truyen truyen) {
        return truyenRepository.save(truyen);
    }

    public Truyen updateTruyen(Truyen truyen) {
        return truyenRepository.save(truyen);
    }

    public void deleteTruyen(Long id) {
        truyenRepository.deleteById(id);
    }


    public List<Truyen> getTopTruyenMoiDangList() {
        List<Truyen> truyenList = truyenRepository.findAll();
        truyenList.sort(Comparator.comparing(Truyen::getNgayDang).reversed());
        return truyenList.stream().limit(6).collect(Collectors.toList());
    }

    public List<Truyen> getTopTruyenNhieuViewList() {
        List<Truyen> topViewsList = truyenRepository.findAll();
        topViewsList.sort(Comparator.comparingInt(Truyen::getSoView).reversed());
        return topViewsList.stream().limit(5).collect(Collectors.toList());
    }

    public List<Truyen> getTopTruyenNhieuLikeList() {
        List<Truyen> topLikeList = truyenRepository.findAll();
        topLikeList.sort(Comparator.comparingInt(Truyen::getSoView).reversed());
        return topLikeList.stream().limit(6).collect(Collectors.toList());
    }

    public List<ChartData> getChartDataForLastWeek() {
        List<Truyen> storiesInWeek = getStoriesInWeek();

        Map<String, Long> storiesByDay = storiesInWeek.stream()
                .collect(Collectors.groupingBy(truyen ->
                        truyen.getNgayDang().getDayOfWeek().toString(), Collectors.counting()));

        List<ChartData> chartData = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            String day = dayOfWeek.toString();
            Long numberOfStories = storiesByDay.getOrDefault(day, 0L);
            chartData.add(new ChartData(day, numberOfStories));
        }

        return chartData;
    }

    public List<Truyen> getStoriesInWeek() {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusWeeks(1);
        return truyenRepository.findByNgayDangGreaterThanEqual(oneWeekAgo);
    }
    public List<Truyen> getAllTruyenMoiDangList(List<Truyen> truyenList) {
        truyenList.sort(Comparator.comparing(Truyen::getNgayDang).reversed());
        return new ArrayList<>(truyenList);
    }
    public List<Truyen> getAllTruyenMoiCapNhat(List<Truyen> truyenList){
        List<Chuong> chuongList = chuongRepository.findAll();
        chuongList.sort(Comparator.comparing(Chuong::getNgayDang).reversed());
        List<Chuong>chuongMoiCapNhatList = chuongList.stream().toList();
        List<Truyen> truyenMoiCapNhatList = truyenList.stream()
                .filter(truyen -> chuongMoiCapNhatList.stream()
                        .anyMatch(chuong -> Objects.equals(chuong.getTruyen().getId(), truyen.getId())))
                .toList();
        return new ArrayList<>(truyenMoiCapNhatList);
    }

    public List<Truyen> searchTruyen(String TenTruyen, Long trangThaiId, Long theLoaiId) {
                // Trường hợp mặc định hoặc khi sapxepId không phải 1 hoặc 2
                if (TenTruyen.isEmpty() && trangThaiId == null && theLoaiId == null) {
                        return truyenRepository.findAll();
                } else if (TenTruyen.isEmpty() && trangThaiId == null) {
                    return truyenRepository.findAllByCategory_Id(theLoaiId);
                } else if (TenTruyen.isEmpty() && theLoaiId == null) {
                    return truyenRepository.findAllByTrangThaiTruyen_Id(trangThaiId);
                } else if (trangThaiId == null && theLoaiId == null) {
                    return truyenRepository.findAllByTenTruyenContaining(TenTruyen);
                } else if (trangThaiId == null) {
                    return truyenRepository.findAllByTenTruyenContainingAndCategory_Id(TenTruyen, theLoaiId);
                } else if (TenTruyen.isEmpty()) {
                    return truyenRepository.findAllByTrangThaiTruyen_IdAndCategory_Id(trangThaiId, theLoaiId);
                } else if (theLoaiId == null) {
                    return truyenRepository.findAllByTenTruyenContainingAndTrangThaiTruyen_Id(TenTruyen, trangThaiId);
                } else {
                    return truyenRepository.findByTenTruyenContainingAndTrangThaiTruyen_IdAndCategory_Id(
                            TenTruyen,
                            trangThaiId,
                            theLoaiId
                    );
                }

    }


    public Optional<Truyen> findById(Long truyenId) {
        return truyenRepository.findById(truyenId);
    }

    public void ChangeCategoryAllTruyensToNewCategory(Long idcategory, Category newCategoryId) {
        List<Truyen> truyenList = truyenRepository.findAllByCategory_Id(idcategory);
        if (!truyenList.isEmpty()) {
            for (Truyen truyen : truyenList) {
                truyen.setCategory(newCategoryId);
            }
            truyenRepository.saveAll(truyenList);
        }

    }

    public void ChangeUserAllTruyensToNewUser(Long userId, User l) {
        List<Truyen> truyenList = truyenRepository.findAllByUser_Id(userId);
        if (!truyenList.isEmpty()) {
            for (Truyen truyen : truyenList) {
                truyen.setUser(l);
            }
            truyenRepository.saveAll(truyenList);
        }
    }


    @Transactional
    public void togglePremiumStatus(Long truyenId) {
        Truyen truyen = truyenRepository.findById(truyenId).orElse(null);
        if (truyen != null) {
            truyen.setPremium(!truyen.isPremium());
            truyenRepository.save(truyen);
        }
    }

    public void updateTruyenLuotXem(Long truyenId) {
        Truyen truyen = truyenRepository.findById(truyenId).orElse(null);
        if (truyen != null) {
            truyen.setSoView(truyen.getSoView() + 1);
            truyenRepository.save(truyen);
        }
    }
}
