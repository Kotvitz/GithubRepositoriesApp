package com.example.githubrepositories.model;

import java.util.List;

public record GithubRepository(String name, GithubUser owner, boolean fork) {
	
	public boolean isFork() {
		return fork;
	}
}