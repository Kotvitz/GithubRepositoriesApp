package com.example.githubrepositories.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.githubrepositories.model.GithubBranch;
import com.example.githubrepositories.model.GithubRepository;

import reactor.core.publisher.Flux;

@Service
public class GithubService {

	private final WebClient webClient;

	public GithubService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
	}

	public Flux<GithubRepository> getUserRepositories(String username) {
		return webClient.get().uri("/users/{username}/repos?type=owner", username).retrieve()
				.bodyToFlux(GithubRepository.class).filter(repo -> !repo.isFork())
				.flatMap(repo -> getBranchesForRepository(repo.owner(), repo.name()).collectList().map(branches -> {
					repo.setBranches(branches);
					return repo;
				}));
	}

	private Flux<GithubBranch> getBranchesForRepository(String owner, String repoName) {
		return webClient.get().uri("/repos/{owner}/{repo}/branches", owner, repoName).retrieve()
				.bodyToFlux(GithubBranch.class);
	}
}
