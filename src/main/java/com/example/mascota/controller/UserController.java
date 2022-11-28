package com.example.mascota.controller;

import com.example.mascota.model.User;
import com.example.mascota.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserRepository userRepository;

    //Add user
    @PostMapping("/add")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User _user = userRepository.save(new User(user.getName(), user.getMail(), user.getPassword()));
            return new ResponseEntity<>(_user, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //login
    @PostMapping("/login")
    public ResponseEntity<User> authLogin(@RequestBody User user) {
        List<User> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            if (u.getMail().equals(user.getMail())) {
                if (u.getPassword().equals(user.getPassword())) {
                    System.out.println("logeado");
                    return new ResponseEntity<>(u, HttpStatus.ACCEPTED);
                }
            }
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //list user
    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userRepository.findAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/one/{id}")
    public ResponseEntity<User> getUser(@PathVariable("id") String id) {
        Optional<User> searchUser = userRepository.findById(id);
        if (searchUser.get() != null) {
            return new ResponseEntity<>(searchUser.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update user
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUSer(@PathVariable("id") String id, @RequestBody User user) {
        Optional<User> searchUser = userRepository.findById(id);
        if (searchUser.isPresent()) {
            User upUser = searchUser.get();
            if (user.getName() == null) {
                upUser.setName(upUser.getName());
            } else {
                upUser.setName(user.getName());
            }
            if (user.getMail() == null) {
                upUser.setMail(upUser.getMail());
            } else {
                upUser.setMail(user.getMail());
            }
            if (user.getPassword() == null) {
                upUser.setPassword(upUser.getPassword());
            } else {
                upUser.setPassword(user.getPassword());
            }
            return new ResponseEntity<>(userRepository.save(upUser), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //delete
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUSer(@PathVariable("id") String id) {
        try {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
