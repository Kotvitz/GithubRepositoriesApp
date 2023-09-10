package com.example.githubrepositories.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.githubrepositories.model.CustomResponse;
import com.example.githubrepositories.model.GithubBranch;
import com.example.githubrepositories.model.GithubRepository;

import reactor.core.publisher.Flux;

@Service
public class GithubService {

	private final WebClient webClient;

	public GithubService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
	}

	public Flux<CustomResponse> getUserRepositories(String username) {
		return webClient.get().uri("/users/{username}/repos?type=owner", username).retrieve()
				.bodyToFlux(GithubRepository.class).filter(repo -> !repo.isFork()).flatMap((GithubRepository repo) -> {
					return getBranchesForRepository(repo.owner().login(), repo.name()).collectList().map(branches -> {
						repo.setBranches(branches);
						return new CustomResponse(repo.name(), repo.owner().login(), branches);
					});
				});
	}

	public Flux<GithubBranch> getBranchesForRepository(String owner, String repoName) {
		return webClient.get().uri("/repos/{owner}/{repo}/branches", owner, repoName).retrieve()
				.bodyToFlux(GithubBranch.class);
	}
}
