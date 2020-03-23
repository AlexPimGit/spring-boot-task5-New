package by.shurikpreproject.springboottask5.dao;

import by.shurikpreproject.springboottask5.model.Role;
import by.shurikpreproject.springboottask5.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class RoleDaoImpl implements RoleDao {
    private Logger LOGGER = Logger.getLogger(RoleDaoImpl.class.getName());
    @PersistenceContext
    private EntityManager entityManager;

////    @Autowired
//    public RoleDaoImpl(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }


    public RoleDaoImpl() {
    }

    @Override
    public void addRole(Role role) {
        entityManager.persist(role);//persist (добавление Entity под управление JPA)
        LOGGER.log(Level.INFO, "Role successfully saved. Role details: " + role);
    }

    @Override
    public void updateRole(Role role) {
//        entityManager.update(role);
        entityManager.refresh(role);
        LOGGER.log(Level.INFO, "Role successfully updated. Role details: " + role);
    }

    @Override
    public void removeRole(Long id) {
        Role role = entityManager.find(Role.class, id);
        if (role != null) {
            entityManager.remove(role);
        }
        LOGGER.log(Level.INFO, "Role successfully deleted. Role details: " + role);
    }

    @Override
    public List<Role> listRole() {
        List<Role> roleList = entityManager.createQuery("FROM Role").getResultList();
        for (Role role : roleList) {
            LOGGER.log(Level.INFO, "Role list: " + role);
        }
        return roleList;
    }

    @Override
    public Role getRoleByName(String name) {
        String hql = "FROM Role role WHERE role.name= :name";
        Role role = (Role) entityManager.createQuery(hql).setParameter("name", name).getSingleResult();
        return role;
    }
}

