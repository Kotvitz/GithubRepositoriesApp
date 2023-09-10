package com.example.githubrepositories.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.githubrepositories.model.CustomResponse;
import com.example.githubrepositories.service.GithubService;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/github")
public class GithubController {
	
    private final GithubService githubService;
    
    @Autowired
    public GithubController(GithubService githubService) {
        this.githubService = githubService;
    }
    
    @GetMapping("/repositories/{username}")
    public Flux<CustomResponse> listUserRepositories(@PathVariable String username) {
        return githubService.getUserRepositories(username);
    }
}