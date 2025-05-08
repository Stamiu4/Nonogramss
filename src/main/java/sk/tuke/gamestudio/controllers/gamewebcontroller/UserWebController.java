package sk.tuke.gamestudio.controllers.gamewebcontroller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entities.User;
import sk.tuke.gamestudio.service.UserService;

@Controller
public class UserWebController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }


    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }


    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        boolean success = userService.login(username, password);
        if (success) {
            User user = new User();
            user.setUsername(username);
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }


    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String confirm,
                           Model model,
                           HttpSession session) {
        if (!password.equals(confirm)) {
            model.addAttribute("error", "Passwords do not match");
            return "register";
        }

        boolean success = userService.register(username, password);
        if (success && userService.login(username, password)) {
            User user = new User();
            user.setUsername(username);
            session.setAttribute("user", user);
            return "redirect:/";
        }

        model.addAttribute("error", "User with this username already exists");
        return "register";
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }
}
