package com.softserveinc.geocitizen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserveinc.geocitizen.entity.User;

/**
 * A DTO that is returned and contains
 * current user's session information.
 *
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/30/17 at 1:48 AM
 */
public class UserSessionDTO {

	private Integer id;
	private String login;
	private User.Type type;

	@JsonProperty("logged_in")
	private boolean loggedIn;

	public UserSessionDTO(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	public UserSessionDTO(Integer id, String login, User.Type type) {
		this.id = id;
		this.login = login;
		this.type = type;
		loggedIn = true;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public User.Type getType() {
		return type;
	}

	public void setType(User.Type type) {
		this.type = type;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}
}
