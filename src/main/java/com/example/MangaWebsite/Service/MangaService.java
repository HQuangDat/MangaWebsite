package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Truyen;
import com.example.MangaWebsite.Repository.IMangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MangaService {
    @Autowired
    private IMangaRepository mangaRepository;
    public List<Truyen> getAllTruyens(){
        return mangaRepository.findAll();
    }
    public Truyen getTruyenById(Long id){
        return mangaRepository.findById(id).orElse(null);
    }
    public void addTruyen(Truyen truyen){
        mangaRepository.save(truyen);
    }
    public void updateTruyen(Truyen truyen){
        mangaRepository.save(truyen);
    }
    public void deleteTruyen(Long id){
        mangaRepository.deleteById(id);
    }
}
