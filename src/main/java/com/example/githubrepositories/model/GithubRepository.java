package com.example.githubrepositories.model;

import java.util.List;

public record GithubRepository(String name, GithubUser owner, List<GithubBranch> branches,
		boolean fork) {
	
	public boolean isFork() {
		return fork;
	}

	public GithubRepository setBranches(List<GithubBranch> branches) {
		return new GithubRepository(name, owner, branches, fork);
	}
}