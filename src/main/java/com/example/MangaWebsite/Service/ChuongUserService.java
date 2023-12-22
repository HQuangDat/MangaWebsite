package com.example.MangaWebsite.Service;


import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Chuong_User;
import com.example.MangaWebsite.Repository.IChuongUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChuongUserService {
    @Autowired
    private IChuongUserRepository iChuongUserRepository;

    public boolean userBoughtChuong(Chuong chuong, User user) {
        Optional<Chuong_User> chuongUserOptional = iChuongUserRepository.findByChuongIdAndUserId(chuong, user);
        return chuongUserOptional.isPresent();
    }
    public void updateChuong_User(Chuong chuong, User user) {
        Chuong_User newChuong_User = new Chuong_User();
        newChuong_User.setChuongId(chuong);
        newChuong_User.setUserId(user);
        newChuong_User.setNgay_mua(LocalDateTime.now());
        newChuong_User.setGia(chuong.getGiaTien());
        if((user.getSoDu() - chuong.getGiaTien()) > 0){
        user.setSoDu(user.getSoDu() - chuong.getGiaTien());
        iChuongUserRepository.save(newChuong_User);};
        }


    @Transactional
    public void DeleteAllByChuongId(Chuong chuongId) {
        List<Chuong_User> chuongUsers = iChuongUserRepository.findByChuongId(chuongId);
        iChuongUserRepository.deleteAll(chuongUsers);
    }


    public List<Chuong_User> getAllChuongUser() {
        return iChuongUserRepository.findAll();
    }


    @Autowired
    public ChuongUserService(IChuongUserRepository chuongUserRepository) {
        this.iChuongUserRepository = chuongUserRepository;
    }

    public List<Chuong_User> getChaptersAndUsersForAccount(User userId) {
        // Giả sử bạn có một phương thức trong kho của mình để lấy các chương và người dùng cho một tài khoản
        return iChuongUserRepository.findByUserId(userId);
    }
}
