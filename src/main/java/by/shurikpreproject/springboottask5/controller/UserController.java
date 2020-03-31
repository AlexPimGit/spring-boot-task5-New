package by.shurikpreproject.springboottask5.controller;


import by.shurikpreproject.springboottask5.model.Role;
import by.shurikpreproject.springboottask5.model.User;
import by.shurikpreproject.springboottask5.service.RoleService;
import by.shurikpreproject.springboottask5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

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
    public ModelAndView getUserPage() {
        User authUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new ModelAndView("user", "user", userService.findByUsername(authUser.getName()));
    }

//    @RequestMapping("/user/{id}")
//    public String showUserForm(@PathVariable("id") Long id, Model model) {
//        model.addAttribute("user", userService.getUserById(id));
//        return "user";
//    }

    @GetMapping("/admin/getAllUsers")
    public ModelAndView getAllUsers() {
        return new ModelAndView("welcome", "users", userService.listUser());
    }

    @GetMapping("/admin/welcome")
    public String getWelcome(ModelMap modelMap,
                             @RequestParam(value = "roleAdmin", required = false) String roleAdmin,//
                             @RequestParam(value = "roleUser", required = false) String roleUser
    ) {
        modelMap.addAttribute("roleAdmin", roleAdmin);//кладем параметр roleAdmin в модель
        modelMap.addAttribute("roleUser", roleUser);
        return "redirect:/admin/getAllUsers";
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
        return "redirect:/admin/getAllUsers";
    }

    @GetMapping("/admin/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id);//берем юзера из базы по id из юрла
        Set<String> preRoles = new HashSet<>();//рыба ролей
        user.getRoles().forEach(e -> preRoles.add(e.getName()));//кладем в рыбу каждую существующую роль юзера
        model.addAttribute("user", user); // заносим из базы юзера
        model.addAttribute("preRoles", preRoles);// отображаем в виде рыбу с ролями
        return "updateUser";//отображаем форму для изма
    }

    @PostMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @Valid User user,
                             BindingResult result,
                             Model model,
                             @RequestParam(value = "roleAdmin", required = false) String roleAdmin,
                             @RequestParam(value = "roleUser", required = false) String roleUser) {

        if (result.hasErrors()) {
            user.setId(id);
            return "updateUser";
        }
        Set<Role> roles = createRoleSet(roleAdmin, roleUser);
        user.setRoles(roles);
        userService.updateUser(user);
        return "redirect:/admin/getAllUsers";
    }

    @GetMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return "redirect:/admin/getAllUsers";
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
