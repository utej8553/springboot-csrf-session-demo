package com.example.demo.controller;

import com.example.demo.model.user;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class usercontroller {
    List<user> users = new ArrayList<>(List.of(
            new user(1, "Utej"),
            new user(2, "Utej2")
    ));
    @GetMapping("csrf-token")
    public CsrfToken getcsrfToken(HttpServletRequest request){
        return (CsrfToken) request.getAttribute("_csrf");
    }
    @GetMapping("users")
    public List<user> getUsers(){
        return users;
    }
    @PostMapping("users")
    public void addUser(@RequestBody user user){
        users.add(user);
    }
}
