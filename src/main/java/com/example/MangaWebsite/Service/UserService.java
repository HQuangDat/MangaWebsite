package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Entity.Role;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Repository.IUserRepository;
import com.example.MangaWebsite.Repository.IRoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


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

    public Long getUserIdByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return (user != null) ? user.getId() : null;
    }

    public void rechargeUser(Long userId, Double amount) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Thực hiện nạp tiền cho người dùng
            double currentBalance = user.getSoDu();
            user.setSoDu(currentBalance + amount);

            // Lưu lại thông tin người dùng đã được cập nhật vào cơ sở dữ liệu
            userRepository.save(user);
        }
    }

    public List<User> getAllUsersWithRoles(long roleid) {
        List<User> usersWithRole1 = userRepository.findByRoleId(roleid);
        return usersWithRole1;
    }

    public void updateRoleCTV(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Role collaboratorRole = roleRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("Collaborator role not found"));

            // Kiểm tra xem User đã là Collaborator chưa
            if (user.getRoles().stream().noneMatch(role -> role.getId() == 3L)) {
                // Nếu chưa, thêm vai trò Collaborator
                user.getRoles().add(collaboratorRole);
                userRepository.save(user);
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }


    public void removeRoleCTV(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Role collaboratorRole = roleRepository.findById(3L)
                    .orElseThrow(() -> new RuntimeException("Collaborator role not found"));

            // Kiểm tra xem User đã là CTV chưa
            if (user.getRoles().stream().noneMatch(role -> role.getId() != 3L)) {

                user.getRoles().remove(collaboratorRole);
            }

            userRepository.save(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}