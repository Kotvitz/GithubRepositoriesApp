package com.example.githubrepositories.service;

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

	public GithubService(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("https://api.github.com").build();
	}

	/*
	 * public Flux<Object> getUserRepositories(String username) { try { return
	 * webClient.get().uri("/users/{username}/repos?type=owner", username)
	 * .header("Authorization",
	 * "Bearer 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").retrieve()
	 * .bodyToFlux(GithubRepository.class).filter(repo -> !repo.isFork())
	 * .flatMap((GithubRepository repo) -> { return
	 * getBranchesForRepository(repo.owner().login(), repo.name()).collectList()
	 * .map(branches -> { repo.setBranches(branches); return new
	 * CustomResponse(repo.name(), repo.owner().login(), branches); }); }); } catch
	 * (WebClientResponseException.NotFound ex) { ErrorResponse errorResponse = new
	 * ErrorResponse(404, "User not found"); return Flux.just(errorResponse); } }
	 */

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
