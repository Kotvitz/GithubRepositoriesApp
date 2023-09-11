package com.example.githubrepositories.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import com.example.githubrepositories.model.CustomResponse;
import com.example.githubrepositories.model.ErrorResponse;
import com.example.githubrepositories.model.GithubBranch;
import com.example.githubrepositories.model.GithubRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class GithubService {

	private final WebClient webClient;

	@Autowired
	public GithubService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
	}

	public Flux<CustomResponse> getUserRepositories(String username) {
		return webClient.get().uri("/users/{username}/repos?type=owner", username).retrieve()
				.onStatus(HttpStatus.NOT_FOUND::equals,
						clientResponse -> Mono
								.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")))
				.onStatus(HttpStatus.NOT_ACCEPTABLE::equals,
						clientResponse -> Mono
								.error(new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "Not acceptable")))
				.bodyToFlux(GithubRepository.class).filter(repo -> !repo.isFork())
				.flatMap(repo -> getBranchesForRepository(repo.owner().login(), repo.name()).collectList()
						.map(branches -> new CustomResponse(repo.name(), repo.owner().login(), branches)));
	}

	public Flux<GithubBranch> getBranchesForRepository(String owner, String repoName) {
		return webClient.get().uri("/repos/{owner}/{repo}/branches", owner, repoName).retrieve()
				.bodyToFlux(GithubBranch.class);
	}
}
