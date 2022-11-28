package com.example.mascota.controller;

import com.example.mascota.model.User;
import com.example.mascota.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @PostMapping("/add")
    public ResponseEntity<User> createUser(@RequestBody User user){
        try {
            User _user = userRepository.save(new User(user.getName(), user.getMail(), user.getPassword()));
            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //login
    @PostMapping("/login")
    public ResponseEntity<User> authLogin(@RequestBody User user){
        List<User> users = userRepository.findAll();
        for(int i=0; i<users.size();i++){
            User u = users.get(i);
            if (u.getMail().equals(user.getMail())){
                if(u.getPassword().equals(user.getPassword())){
                    System.out.println("logeado");
                    return new ResponseEntity<> (u, HttpStatus.ACCEPTED);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUser(){
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
