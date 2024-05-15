package com.softserveinc.geocitizen.dto;

import com.softserveinc.geocitizen.entity.Image;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

import static com.softserveinc.geocitizen.entity.User.Type;


public class UserProfileDTO implements Serializable {

	private int id;

	private String login;

	private String email;

	private Type type;

	private Image image;

	private String name;

	private String surname;

	private boolean facebookConnected;

	private boolean googleConnected;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogin() {
		return StringUtils.trim(login);
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return StringUtils.trim(email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return StringUtils.trim(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return StringUtils.trim(surname);
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public boolean isFacebookConnected() {
		return facebookConnected;
	}

	public void setFacebookConnected(boolean facebookConnected) {
		this.facebookConnected = facebookConnected;
	}

	public boolean isGoogleConnected() {
		return googleConnected;
	}

	public void setGoogleConnected(boolean googleConnected) {
		this.googleConnected = googleConnected;
	}
}
