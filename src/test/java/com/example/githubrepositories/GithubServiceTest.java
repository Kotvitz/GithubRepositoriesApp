package com.example.githubrepositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.githubrepositories.model.CustomResponse;
import com.example.githubrepositories.model.GithubBranch;
import com.example.githubrepositories.model.GithubCommit;
import com.example.githubrepositories.model.GithubRepository;
import com.example.githubrepositories.model.GithubUser;
import com.example.githubrepositories.service.GithubService;

import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class GithubServiceTest {

	@InjectMocks
    private GithubService githubService;

    @Mock
    private WebClient.Builder webClientBuilder;

    @Mock
    private WebClient webClient;
    
    @SuppressWarnings("rawtypes")
	@Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    public void setup() {
        when(webClientBuilder.baseUrl("https://api.github.com")).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);
    }
    
    @Test
    public void testGetUserRepositories() {
    	GithubUser user1 = new GithubUser("mojombo");
        GithubRepository repo1 = new GithubRepository("30daysoflaptops.github.io", user1, true);
        GithubRepository repo2 = new GithubRepository("conceptual_algorithms", user1, true);

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("/users/{username}/repos?type=owner", user1.login())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(GithubRepository.class)).thenReturn(Flux.fromIterable(Arrays.asList(repo1, repo2)));
        when(webClientBuilder.baseUrl("https://api.github.com")).thenReturn(webClientBuilder);
        when(webClientBuilder.build()).thenReturn(webClient);

        when(responseSpec.onStatus(HttpStatus.NOT_FOUND::equals, any())).thenReturn(responseSpec);
        when(responseSpec.onStatus(HttpStatus.NOT_ACCEPTABLE::equals, any())).thenReturn(responseSpec);

        GithubCommit commit1 = new GithubCommit("sha1");
        GithubCommit commit2 = new GithubCommit("sha2");

        GithubBranch branch1 = new GithubBranch("branch1", commit1);
        GithubBranch branch2 = new GithubBranch("branch2", commit2);

        when(requestHeadersUriSpec.uri("/repos/{owner}/{repo}/branches", user1.login(), repo1.name())).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToFlux(GithubBranch.class)).thenReturn(Flux.fromIterable(Arrays.asList(branch1, branch2)));

        Flux<CustomResponse> result = githubService.getUserRepositories(user1.login());

        List<CustomResponse> responses = result.collectList().block();

        assertEquals(1, responses.size());
        CustomResponse customResponse = responses.get(0);
        assertEquals("repo2", customResponse.getName());
        assertEquals("conceptual_algorithms", customResponse.getOwner());
        assertEquals(2, customResponse.getBranches().size());
    }

}
