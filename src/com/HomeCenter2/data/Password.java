package com.HomeCenter2.data;

import java.io.Serializable;

public class Password implements Serializable{
	private String name;
	private String password;

	public Password() {
		name = "";
		password = "";
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
