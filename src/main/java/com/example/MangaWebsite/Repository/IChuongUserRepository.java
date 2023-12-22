package com.example.MangaWebsite.Repository;


import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Model.Chuong;
import com.example.MangaWebsite.Model.Chuong_User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface IChuongUserRepository extends JpaRepository<Chuong_User, Long> {
    Optional<Chuong_User> findByChuongIdAndUserId(Chuong chuongId, User userId);
    List<Chuong_User> findByChuongId(Chuong chuong);

    List<Chuong_User> findByUserId(User userId);
}
