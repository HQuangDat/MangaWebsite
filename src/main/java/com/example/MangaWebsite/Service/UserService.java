package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Repository.IUserRepository;
import com.example.MangaWebsite.Repository.IRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;

    @Transactional
    public void save(User user) {
        userRepository.save(user);
        Long userId = userRepository.getUserIdByUsername(user.getUsername());
        Long roleId = roleRepository.getRoleIdByName("USER");
        if (roleId != null && roleId != 0 && userId != null && userId != 0) {
            userRepository.addRoleToUser(userId, roleId);
        }
    }

    public void updateUser(User user){
        userRepository.save(user);
    }

    public User getUserbyId(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public String[] getUserRolebyId(Long id){return userRepository.getRolesOfUser(id);}
}