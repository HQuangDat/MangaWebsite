package com.example.MangaWebsite.Service;

import com.example.MangaWebsite.Entity.Role;
import com.example.MangaWebsite.Entity.User;
import com.example.MangaWebsite.Repository.IRoleRepository;
import com.example.MangaWebsite.Repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class RoleService {
        @Autowired
        private IUserRepository userRepository;
        @Autowired
        private IRoleRepository iRoleRepository;


    }



