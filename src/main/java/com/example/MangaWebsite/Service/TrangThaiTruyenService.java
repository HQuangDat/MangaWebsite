package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.TrangThaiTruyen;
import com.example.MangaWebsite.Repository.IChuongRepository;
import com.example.MangaWebsite.Repository.ITrangThaiTruyenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrangThaiTruyenService {

    @Autowired
    private ITrangThaiTruyenRepository trangThaiTruyenRepository;
    public List<TrangThaiTruyen> getAllTrangThais() {
        return trangThaiTruyenRepository.findAll();
    }



}
