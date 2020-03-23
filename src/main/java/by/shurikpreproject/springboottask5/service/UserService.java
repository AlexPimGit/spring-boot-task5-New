package by.shurikpreproject.springboottask5.service;

import by.shurikpreproject.springboottask5.model.User;

import java.util.List;

public interface UserService {
    void addUser(User user);

    void updateUser(User user);

    void removeUser(Long id);

    User getUserById(Long id);

    List<User> listUser();

    User findByUsername(String name);

    List findListByUsername(String filter);
}
