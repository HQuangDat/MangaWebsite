package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.Truyen;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MangaService {
    //Tạo list chứa toàn bộ thông tin của truyện
    private List<Truyen> listTruyen = new ArrayList<>();
    //Tra ve toan bo cac truyen hien co
    public List<Truyen> getAll(){ return listTruyen; }
    //Tra ve mot truyen theo id
    public Truyen get(char id)
    {
        var findTruyen = listTruyen.stream().filter(p->p.getMaTruyen()==id).findFirst().orElse(null);
        if(findTruyen == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return findTruyen;
    }
    //Them truyen
    public void add(Truyen newTruyen)
    {
        var maxId = listTruyen.stream().mapToInt(Truyen::getMaTruyen).max().orElse(0);
        listTruyen.add(newTruyen);
    }
    //Xoa truyen
    public void remove(char id)
    {
        var findTruyen = listTruyen.stream().filter(p->p.getMaTruyen()==id).findFirst().orElseThrow();
        listTruyen.remove(findTruyen);
    }
}
