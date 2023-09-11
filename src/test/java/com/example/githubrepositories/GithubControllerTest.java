package com.example.githubrepositories;

import com.example.githubrepositories.controller.GithubController;
import com.example.githubrepositories.exception.ErrorHandler;
import com.example.githubrepositories.model.CustomResponse;
import com.example.githubrepositories.model.GithubBranch;
import com.example.githubrepositories.model.GithubCommit;
import com.example.githubrepositories.service.GithubService;

import reactor.core.publisher.Flux;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.Mockito.when;

import java.util.Arrays;

@ExtendWith(MockitoExtension.class)
public class GithubControllerTest {

	@InjectMocks
	private GithubController githubController;

	@Mock
	private GithubService githubService;

	private WebTestClient webTestClient;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
		webTestClient = WebTestClient.bindToController(githubController).controllerAdvice(new ErrorHandler()).build();
	}

	@Test
	public void testListUserRepositories() {
        GithubCommit commit1 = new GithubCommit("0473d25b87ae15990d99a137dce7bb172c998af7");
        GithubCommit commit2 = new GithubCommit("52ea8096a319125a2118393894a421de7893d1d4");

		CustomResponse[] customResponses = {
				new CustomResponse("erlectricity", "mojombo", Arrays.asList(new GithubBranch("master", commit1))),
				new CustomResponse("30daysoflaptops.github.io", "mojombo", Arrays.asList(new GithubBranch("gh-pages", commit2))) };
		Flux<CustomResponse> sampleData = Flux.fromArray(customResponses);

		when(githubService.getUserRepositories("mojombo")).thenReturn(sampleData);

		webTestClient.get().uri("/github/repositories/mojombo").accept(MediaType.APPLICATION_JSON).exchange()
				.expectStatus().isOk().expectHeader().contentType(MediaType.APPLICATION_JSON)
				.expectBodyList(CustomResponse.class).hasSize(2);
	}
}
