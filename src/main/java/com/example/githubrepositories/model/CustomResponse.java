package com.example.githubrepositories.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class CustomResponse {

    private String name;
    private String owner;
    private List<GithubBranch> branches;

    public CustomResponse() {
    	
    }
    
    public CustomResponse(String name, String owner, List<GithubBranch> branches) {
        this.name = name;
        this.owner = owner;
        this.branches = branches;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<GithubBranch> getBranches() {
		return branches;
	}

	public void setBranches(List<GithubBranch> branches) {
		this.branches = branches;
	}
}
