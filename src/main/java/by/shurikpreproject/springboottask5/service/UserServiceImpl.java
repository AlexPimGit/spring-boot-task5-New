package by.shurikpreproject.springboottask5.service;

import by.shurikpreproject.springboottask5.dao.RoleDao;
import by.shurikpreproject.springboottask5.dao.UserDao;
import by.shurikpreproject.springboottask5.model.Role;
import by.shurikpreproject.springboottask5.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional//не надо вызывать типовой код try entityManager / .begin();/.commit();/catch .rollback();
public class UserServiceImpl implements UserService {
    private UserDao userDao;
    private RoleDao roleDao;
    //@Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserDao userDao, RoleDao roleDao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void addUser(User user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
//        user.setRoles(new HashSet<>(roleDao.listRole()));
//        List roles = roleDao.listRole();
////        roles.add(new Role("ADMIN"));
//        user.setRoles(roles);
        userDao.addUser(user);
    }

    @Override
    public void updateUser(User user) {
        userDao.updateUser(user);
    }

    @Override
    public void removeUser(Long id) {
        userDao.removeUser(id);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public List<User> listUser() {
        return userDao.listUser();
    }

    @Override
    public User findByUsername(String name) {
        return userDao.findByUsername(name);
    }

    @Override
    public List findListByUsername(String filter) {
        return userDao.findListByUsername(filter);
    }
}
