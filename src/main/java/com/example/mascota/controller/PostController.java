package com.example.mascota.controller;
import com.example.mascota.model.Post;
import com.example.mascota.model.User;
import com.example.mascota.repository.PostRepository;
import com.example.mascota.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("/api/post")

public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    @PostMapping("/add/{id}")
    public ResponseEntity<Post> createPost(@PathVariable(value = "id") String id, @RequestBody Post requestPost) {
      try {
          Post _post = userRepository.findById(id).map(user -> {
              requestPost.setUserID(id);
              return postRepository.save(requestPost);
          }).orElseThrow();
          return new ResponseEntity<>(_post, HttpStatus.CREATED);
      }catch (Exception e){
          return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPost() {
        List<Post> posts = postRepository.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

}
