package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Entity.Role;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Repository.IUserRepository;
import com.example.MangaWebsite.Repository.IRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IRoleRepository roleRepository;
    @Autowired
    private IRoleRepository iRoleRepository;


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
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteUserById(userId);
    }
    public User getUserbyId(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public String[] getUserRolebyId(Long id){return userRepository.getRolesOfUser(id);}

    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return (user != null) ? user.getId() : null;
    }


    public List<User> getAllUsersWithRoles(long roleid) {
        List<User> usersWithRole1 = userRepository.findByRoleId(roleid);
        return usersWithRole1;
    }

    public void updateRoleCTVForUser(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            Role newRole = iRoleRepository.findById(3L).orElse(null);
            if (newRole != null) {
                user.getRoles().clear();
                user.getRoles().add(newRole);
                userRepository.save(user);
            }
        }
    }
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
@Transactional
    public void deleteRoleAssignment(Long userId, long roleId) {
        userRepository.deleteRoleAssignment(userId,roleId);
    }

    public List<User> getUsersBySingleRole(Long roleId) {
        // Lấy danh sách người dùng với chính xác một vai trò là roleId từ repository hoặc thông qua các truy vấn khác
        return userRepository.findUsersBySingleRole(roleId);
    }
}