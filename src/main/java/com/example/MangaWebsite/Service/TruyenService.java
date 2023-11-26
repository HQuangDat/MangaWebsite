package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Repository.IMangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TruyenService {
    @Autowired
    private IMangaRepository truyenRepository;
    public List<Truyen> getAllTruyens(){
        return truyenRepository.findAll();
    }
    public Truyen getTruyenById(Long id){
        return truyenRepository.findById(id).orElse(null);
    }
    public List<Truyen> getTruyensByUserId(Long userId){return truyenRepository.findAllById(userId);};

    public void addTruyen(Truyen truyen){
        truyenRepository.save(truyen);
    }
    public void updateTruyen(Truyen truyen){
        truyenRepository.save(truyen);
    }
    public void deleteTruyen(Long id){
        truyenRepository.deleteById(id);
    }

}
