package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Repository.IMangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruyenService {
    @Autowired
    private IMangaRepository truyenService;
    public List<Truyen> getAllTruyens(){
        return truyenService.findAll();
    }
    public Truyen getTruyenById(Long id){
        return truyenService.findById(id).orElse(null);
    }
    public List<Truyen> getTruyensByUserId(Long userId){return truyenService.findAllById(userId);};

    public void addTruyen(Truyen truyen){
        truyenService.save(truyen);
    }
    public void updateTruyen(Truyen truyen){
        truyenService.save(truyen);
    }
    public void deleteTruyen(Long id){
        truyenService.deleteById(id);
    }
}
