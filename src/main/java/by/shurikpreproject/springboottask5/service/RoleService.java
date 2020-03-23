package by.shurikpreproject.springboottask5.service;

import by.shurikpreproject.springboottask5.model.Role;

import java.util.List;

public interface RoleService {
    void addRole(Role role);

    void updateRole(Role role);

    void removeRole(Long id);

    List listRole();

    Role getRoleByName(String name);
}
