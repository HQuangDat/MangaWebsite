package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Model.TaiKhoan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    //Tạo list chứa toàn bộ thông tin tai khoan
    private List<TaiKhoan> listTK = new ArrayList<>();
    //Tra ve toan bo cac tai khoan hien co
    public List<TaiKhoan> getAll(){ return listTK; }
    //Tra ve mot taikhoan theo id
    public TaiKhoan get(char id)
    {
        var findTK = listTK.stream().filter(p->p.getMaTK()==id).findFirst().orElse(null);
        if(findTK == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        return findTK;
    }
    //Them tai khoan
    public void add(TaiKhoan newTK)
    {
        var maxId = listTK.stream().mapToInt(TaiKhoan::getMaTK).max().orElse(0);
        listTK.add(newTK);
    }
    //Xoa tai khoan
    public void remove(char id)
    {
        var findTK = listTK.stream().filter(p->p.getMaTK()==id).findFirst().orElseThrow();
        listTK.remove(findTK);
    }
}
