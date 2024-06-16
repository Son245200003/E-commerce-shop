package com.project.shopapp.services;

import com.project.shopapp.models.Role;

import java.util.List;

public interface RoleService {
    List<Role> findAll();

    Role findRoleById();
}
