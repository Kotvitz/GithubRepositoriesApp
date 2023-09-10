package com.example.githubrepositories.model;

public record GithubUser(String login) {

	public GithubUser {
		if (login == null || login.isBlank()) {
			throw new IllegalArgumentException("Login cannot be null or empty.");
		}
	}
}
