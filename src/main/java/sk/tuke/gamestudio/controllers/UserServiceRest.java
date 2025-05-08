package sk.tuke.gamestudio.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.service.UserService;

@RestController
@RequestMapping("/api/connect")
public class UserServiceRest {
    @Autowired
    private UserService userService;

    @GetMapping("/check/{username}")
    public int doesUsernameExist(@PathVariable String username) {
        return userService.doesUsernameExist(username);
    }
    @GetMapping("/register/{username}/{password}")
    public boolean registerUser(@PathVariable String username, @PathVariable String password) {
        return userService.register(username, password);
    }
    @GetMapping("/login/{username}/{password}")
    public boolean login(@PathVariable String username, @PathVariable String password){
        return userService.login(username, password);
    }
}