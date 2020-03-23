package by.shurikpreproject.springboottask5.dao;

import by.shurikpreproject.springboottask5.model.User;

import java.util.List;


public interface UserDao {
    void addUser(User user);

    void updateUser(User user);

    void removeUser(Long id);

    User getUserById(Long id);

    List listUser();

    User findByUsername(String name);

    User findByUserEmail(String email);

    List findListByUsername(String filter);
}
