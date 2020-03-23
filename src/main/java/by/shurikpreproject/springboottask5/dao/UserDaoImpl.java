package by.shurikpreproject.springboottask5.dao;


import by.shurikpreproject.springboottask5.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class UserDaoImpl implements UserDao {
    private Logger LOGGER = Logger.getLogger(UserDaoImpl.class.getName());
    @PersistenceContext
    private EntityManager entityManager;//использовать entity менеджер без СпрингДата

////    @Autowired
//    public UserDaoImpl(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }

    public UserDaoImpl() {
    }

    //как в этих методах быть с исключениями??
    @Override
    public void addUser(User user) {
        entityManager.persist(user);
        LOGGER.log(Level.INFO, "User successfully saved. User details: " + user);
    }

    @Override
    public void updateUser(User user) {
//        entityManager.refresh(user);
        entityManager.merge(user);
        LOGGER.log(Level.INFO, "User successfully updated. User details: " + user);
    }

    @Override
    public void removeUser(Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
        LOGGER.log(Level.INFO, "User successfully deleted. User details: " + user);
    }

    @Override
    public User getUserById(Long id) {
        User user = entityManager.find(User.class, id);
        LOGGER.log(Level.INFO, "User successfully loaded. User details: " + user);
        return user;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUser() {
        List<User> userList = entityManager.createQuery("FROM User").getResultList();
        for (User user : userList) {
            LOGGER.log(Level.INFO, "User list: " + user);
        }
        return userList;
    }

//    @Override
//    public User findByUsername(String name) {
//        Session session = sessionFactory.getCurrentSession();
//        User user = session.load(User.class, name);
//        LOGGER.log(Level.INFO, "User successfully loaded. User details: " + user);
//        return user;
//    }

    @Override
    public User findByUserEmail(String email) {
        User user = entityManager.find(User.class, email);
        LOGGER.log(Level.INFO, "User successfully loaded. User details: " + user);
        return user;
    }

    @Override
    public List findListByUsername (String filter) {
        String hql = "FROM User user WHERE user.name= :name";
        List userList = entityManager.createQuery(hql).getResultList();
        for (Object user : userList) {
            LOGGER.log(Level.INFO, "User list: " + user);
        }
        return userList;
    }

    @Override
    public User findByUsername(String name) {
        String hql = "FROM User user WHERE user.name= :name";
        User user = (User) entityManager.createQuery(hql).setParameter("name", name).getSingleResult();
        return user;
    }
}
