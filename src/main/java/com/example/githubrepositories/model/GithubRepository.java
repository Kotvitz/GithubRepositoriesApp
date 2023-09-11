package com.example.githubrepositories.model;

public record GithubRepository(String name, GithubUser owner, boolean fork) {
	
	public boolean isFork() {
		return fork;
	}
}