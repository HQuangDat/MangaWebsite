package com.example.MangaWebsite.Repository;

import com.example.MangaWebsite.Entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface IRoleRepository extends JpaRepository<Role, Long> {
    @Query("SELECT r.id FROM Role r WHERE r.TenLoaiTK = ?1")
    Long getRoleIdByName(String roleName);
   
}
