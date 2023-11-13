package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = ?1")
    User findByUsername(String username);
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO user_role (user_id, role_id)" + "VALUES (?1, ?2)", nativeQuery = true)
    void addRoleToUser(Long userId, Long roleId);
    @Query("SELECT u.id FROM User u WHERE u.username =?1")
    Long getUserIdByUsername(String username);

    @Query(value = "SELECT r.ten_loaitk FROM Role r INNER JOIN user_role ur " + "ON r.id = ur.role_id WHERE ur.user_id = ?1",nativeQuery = true)
    String[] getRolesOfUser(Long userId);
}
