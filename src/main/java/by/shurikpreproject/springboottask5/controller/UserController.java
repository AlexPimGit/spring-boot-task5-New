package by.shurikpreproject.springboottask5.controller;


import by.shurikpreproject.springboottask5.model.Role;
import by.shurikpreproject.springboottask5.model.User;
import by.shurikpreproject.springboottask5.service.RoleService;
import by.shurikpreproject.springboottask5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Controller
public class UserController {
    private UserService userService;
    private RoleService roleService;

    @Autowired
    public UserController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public Model getUserPage(Model model) {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", userService.findByUsername(authUser.getName()));
        return model;
    }

    @RequestMapping("/user/{id}")
    public String showUserForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "user";
    }

    @GetMapping("/admin/welcome")
    public String getWelcome(@RequestParam(name = "message", required = false, defaultValue = "Aloha") String message,
                             Model model,
                             @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                             @RequestParam(value = "roleUser", required = false) String roleUser
    ) {
        model.addAttribute("message", message);
        model.addAttribute("greeting", "Hola, ");
        model.addAttribute("they", "Amigos");
        model.addAttribute("roleAdmin", roleAdmin);//кладем параметр roleAdmin в модель
        model.addAttribute("roleUser", roleUser);

        if (roleAdmin != null) {
            if (roleAdmin.equalsIgnoreCase("admin")) {
                model.addAttribute("status", "OKadmin");
            }
            if (roleUser.equalsIgnoreCase("user")) {
                model.addAttribute("status", "OKuser");
            }
        } else {
            model.addAttribute("status", "Not ok");
        }
        model.addAttribute("users", userService.listUser());
        return "welcome";
    }

    @GetMapping("/admin/addUser")
    public String showAddForm(User user) {
        return "addUser";
    }

    @PostMapping("/admin/addUser")
    public String addUser(@Valid User user,
                          //берем из формы параметр "roleAdmin", необязательный (false)
                          @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                          @RequestParam(value = "roleUser", required = false) String roleUser,
                          BindingResult result,
                          Model model) {
        if (result.hasErrors()) {// если не прошел Valid - заново
            return "addUser";
        }
        Set<Role> roles = createRoleSet(roleAdmin, roleUser);//result
        user.setRoles(roles);
        userService.addUser(user);
        model.addAttribute("users", userService.listUser());
        return "/welcome";
    }

    @GetMapping("/admin/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);//берем юзера из базы
        Set<String> preRoles = new HashSet<>();//рыба ролей
        user.getRoles().forEach(e -> preRoles.add(e.getName()));//кладем в рыбу каждую существующую роль юзера
        model.addAttribute("user", user);
        model.addAttribute("preRoles", preRoles);// отображаем в виде рыбу с ролями
        return "updateUser";
    }

    @PostMapping("/admin/update/{id}")
    public String updateUser(@Valid User user,
                             Model model,
                             @PathVariable("id") Long id,
                             @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                             @RequestParam(value = "roleUser", required = false) String roleUser) {

        Set<Role> roles = createRoleSet(roleAdmin, roleUser);
        user.setRoles(roles);
        userService.updateUser(user);
        model.addAttribute("users", userService.listUser());
        return "welcome";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model) {
        userService.removeUser(id);
        model.addAttribute("users", userService.listUser());
        return "welcome";
    }

    private Set<Role> createRoleSet(String roleAdmin, String roleUser) {
        Set<String> setRole = new HashSet<>();//preResult
        setRole.add(roleAdmin);
        setRole.add(roleUser);
        setRole.removeIf(Objects::isNull);
        Set<Role> roles = new HashSet<>();
        Iterator<String> itr = setRole.iterator();
        while (itr.hasNext()) {
            itr.forEachRemaining(roleName -> roles.add(roleService.getRoleByName(roleName)));
        }
        return roles;
    }
}
