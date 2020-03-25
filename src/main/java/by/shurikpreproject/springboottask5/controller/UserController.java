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
    public String getWelcome(@RequestParam(name = "message", required = false,
            defaultValue = "Aloha") String message,
                             Model model,
                             @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                             @RequestParam(value = "roleUser", required = false) String roleUser
    ) {
        model.addAttribute("message", message);
        model.addAttribute("greeting", "Hola, ");
        model.addAttribute("they", "Amigos");
        model.addAttribute("users", userService.listUser());
        model.addAttribute("roles", roleService.listRole());

        if (roleAdmin != null) {
            if (roleAdmin.equalsIgnoreCase("admin")) {
                model.addAttribute("status", "OK");
            }
        } else {
            model.addAttribute("status", "Not ok");
        }

        return "welcome";
    }

    @GetMapping("/admin/addUser")
    public String showAddForm(User user) {
        return "addUser";
    }

    @PostMapping("/admin/addUser")
    public String addUser(@Valid User user, Role role,
                          @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                          @RequestParam(value = "roleUser", required = false) String roleUser,
                          BindingResult result, Model model
    ) {
        if (result.hasErrors()) {// если не прошел Valid - заново
            return "addUser";
        }


        userService.addUser(user);
        model.addAttribute("users", userService.listUser());
        return "/welcome";
    }

    @GetMapping("/admin/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        return "updateUser";
    }

    @PostMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") long id, @Valid User user,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            user.setId(id);
            return "updateUser";
        }

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

//    private Set<Role> createRoleSet(String... roleName) {
//        return Stream.of(roleName).
//                filter(Objects::nonNull).
//                map(roleService::getRoleByName).
//                filter(Optional::isPresent).
//                map(Optional::get).
//                collect(Collectors.toSet());
//    }
}
