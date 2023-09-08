package com.example.githubrepositories.model;

import java.util.List;

public record GithubRepository(String name, String owner, List<GithubBranch> branches) {
	
	public boolean isFork() {
		return !owner.equals(name);
	}

	public GithubRepository setBranches(List<GithubBranch> branches) {
		return new GithubRepository(name, owner, branches);
	}
}
