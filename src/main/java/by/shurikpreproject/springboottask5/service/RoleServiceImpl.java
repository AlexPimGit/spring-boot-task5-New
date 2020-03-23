package by.shurikpreproject.springboottask5.service;

import by.shurikpreproject.springboottask5.dao.RoleDao;
import by.shurikpreproject.springboottask5.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void addRole(Role role) {
        roleDao.addRole(role);
    }

    @Override
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }

    @Override
    public void removeRole(Long id) {
        roleDao.removeRole(id);
    }

    @Override
    public List<Role> listRole() {
        return roleDao.listRole();
    }

    @Override
    public Role getRoleByName(String name) {
        return roleDao.getRoleByName(name);
    }
}
