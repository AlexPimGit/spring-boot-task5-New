package by.shurikpreproject.springboottask5.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String userPassword;

    @Column(name = "position")
    private String position;

    @Column(name = "age")
    private int age;

    @Column(name = "email")
    private String email;

    @ManyToMany(fetch = FetchType.EAGER) // один юзер может иметь много ролей, одна роль может иметь много юзеров
    @JoinTable(name = "users_roles", // делаем таблицу связей для сущности юзер и сущности роль
            //FetchType.EAGER – «жадная» загрузка, т.е. список ролей загружается вместе с пользователем сразу (не ждет пока к нему обратятся)
            joinColumns = @JoinColumn(name = "user_id"),//добавляем колонку связи "user_id"
            inverseJoinColumns = @JoinColumn(name = "role_id"))// добавляем колонку для обратной связи "role_id"
    private Set<Role> roles;

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public User() {
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public User(Long id, String name, String userPassword, String position, int age, String email, Set<Role> roles) {
        this.id = id;
        this.name = name;
        this.userPassword = userPassword;
        this.position = position;
        this.age = age;
        this.email = email;
        this.roles = roles;
    }

    public User(String name, String userPassword, Set<Role> roles) {
        this.name = name;
        this.userPassword = userPassword;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", position='" + position + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return getUserPassword();
    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}