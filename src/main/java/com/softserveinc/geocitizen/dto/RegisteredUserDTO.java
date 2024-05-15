package com.softserveinc.geocitizen.dto;

public class RegisteredUserDTO {

	private int id;
	private String login;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Override
	public String toString() {
		return "RegisteredUserDTO{" +
				"login='" + login + '\'' +
				'}';
	}
}
