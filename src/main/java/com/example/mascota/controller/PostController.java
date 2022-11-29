package com.example.mascota.controller;

import com.example.mascota.model.Post;
import com.example.mascota.repository.PostRepository;
import com.example.mascota.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/post")

public class PostController {
    @Autowired
    PostRepository postRepository;
    @Autowired
    UserRepository userRepository;

    //Add post
    @PostMapping("/add/{id}")
    public ResponseEntity<Post> createPost(@PathVariable("id") String id, @RequestBody Post requestPost) {
        try {
            Post _post = userRepository.findById(id).map(user -> {
                requestPost.setUserID(id);
                return postRepository.save(requestPost);
            }).orElseThrow();
            return new ResponseEntity<>(_post, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //update post
    @PutMapping("/update/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable("id") String id, @RequestBody Post requestPost) {
        Optional<Post> searchPost = postRepository.findById(id);
        if (searchPost.isPresent()) {
            Post postUp = searchPost.get();
            if (requestPost.getDescription() == null) {
                postUp.setDescription(postUp.getDescription());
            } else {
                postUp.setDescription(requestPost.getDescription());
            }
            if (requestPost.getReward() == 0) {
                postUp.setReward(postUp.getReward());
            } else {
                postUp.setReward(requestPost.getReward());
            }
            return new ResponseEntity<>(postRepository.save(postUp), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //List posts
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getAllPost() {
        List<Post> posts = postRepository.findAll();
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    //Post one
    @GetMapping("/one/{id}")
    public ResponseEntity<Post> getPost(@PathVariable("id") String id) {
        Optional<Post> searchPost = postRepository.findById(id);
        if (searchPost.get() != null) {
            return new ResponseEntity<>(searchPost.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deletePost(@PathVariable("id")String id){
        try{
            postRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
