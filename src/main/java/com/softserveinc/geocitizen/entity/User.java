/*
 * The following code have been created by Yaroslav Zhyravov (shrralis).
 * The code can be used in non-commercial way for everyone.
 * But for any commercial way it needs a author's agreement.
 * Please contact the author for that:
 *  - https://t.me/Shrralis
 *  - https://twitter.com/Shrralis
 *  - shrralis@gmail.com
 *
 * Copyright (c) 2017 by shrralis (Yaroslav Zhyravov).
 */

package com.softserveinc.geocitizen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.softserveinc.geocitizen.entity.interfaces.Identifiable;
import com.softserveinc.geocitizen.util.PsqlEnum;
import org.hibernate.annotations.Proxy;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

import static com.softserveinc.geocitizen.entity.User.TABLE_NAME;

@Entity
@Proxy(lazy = false)
@Table(name = TABLE_NAME)
@TypeDef(
		name = "user_type",
		typeClass = PsqlEnum.class
)
public class User implements Identifiable<Integer> {

	public static final String LOGIN_PATTERN = "^[A-Za-z_\\-.0-9]+$";
	public static final String PASS_PATTERN = "^(?=.*\\d)(?=.*[a-zа-яіїє])(?=.*[A-ZА-ЯІЇЄ])" +
			"(?=.*[-`!@#$%^&*()_+=\"'<>,./|\\\\?]).*$";
	public static final String NAME_PATTERN = "^[A-ZА-ЯІЇЄ]('?[a-zа-яіїє])+?(-[A-ZА-ЯІЇЄ]('?[a-zа-яіїє])+)?$";
	public static final String TABLE_NAME = "users";
	public static final String ID_COLUMN_NAME = "id";
	public static final String LOGIN_COLUMN_NAME = "login";
	public static final String TYPE_COLUMN_NAME = "type";
	public static final String EMAIL_COLUMN_NAME = "email";
	public static final String PASS_COLUMN_NAME = "password";
	public static final String IMAGE_COLUMN_NAME = "image_id";
	public static final String NAME_COLUMN_NAME = "name";
	public static final String SURNAME_COLUMN_NAME = "surname";
	public static final String FAILED_AUTH_COUNT_COLUMN_NAME = "failed_auth_count";
	public static final String REGISTRATION_TOKEN_COLUMN_NAME = "registration_token";
	public static final String BLOCKING_EXPIRES_AT_COLUMN_NAME = "blocking_expires_at";
	public static final int MAX_LOGIN_LENGTH = 16;
	public static final int MIN_LOGIN_LENGTH = 4;
	public static final int MAX_EMAIL_LENGTH = 256;
	public static final int MIN_EMAIL_LENGTH = 6;
	public static final int MAX_PASSWORD_LENGTH = 64;
	public static final int MIN_PASSWORD_LENGTH = 8;
	public static final int MAX_NAME_LENGTH = 16;
	public static final int MIN_NAME_LENGTH = 1;
	public static final int MAX_SURNAME_LENGTH = 32;
	public static final int MIN_SURNAME_LENGTH = 1;
	public static final int MAX_FAILED_AUTH_VALUE = 5;
	public static final int MAX_REGISTRATION_TOKEN_LENGTH = 32;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq_gen")
	@SequenceGenerator(name = "users_seq_gen", sequenceName = "users_id_seq", allocationSize = 1)
	@Column(name = ID_COLUMN_NAME, nullable = false, unique = true)
	private Integer id;

	@NotNull
	@NotBlank
	@Pattern(regexp = User.LOGIN_PATTERN)
	@Size(min = MIN_LOGIN_LENGTH, max = MAX_LOGIN_LENGTH)
	@Column(name = LOGIN_COLUMN_NAME, nullable = false, unique = true, length = MAX_LOGIN_LENGTH)
	private String login;

	@NotNull
	@Enumerated(EnumType.STRING)
	@org.hibernate.annotations.Type(type = "user_type")
	@Column(name = TYPE_COLUMN_NAME, nullable = false)
	private Type type = Type.ROLE_USER;

	@NotNull
	@NotBlank
	@Email
	@Size(min = MIN_EMAIL_LENGTH, max = MAX_EMAIL_LENGTH)
	@Column(name = EMAIL_COLUMN_NAME, nullable = false, unique = true, length = MAX_EMAIL_LENGTH)
	private String email;

	@JsonIgnore
	@NotNull
	@NotBlank
	@Size(min = MIN_PASSWORD_LENGTH, max = MAX_PASSWORD_LENGTH)
	@Column(name = PASS_COLUMN_NAME, nullable = false, length = MAX_PASSWORD_LENGTH)
	private String password;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = IMAGE_COLUMN_NAME)
	private Image image;

	@Pattern(regexp = NAME_PATTERN)
	@Size(min = MIN_NAME_LENGTH, max = MAX_NAME_LENGTH)
	@Column(name = NAME_COLUMN_NAME, length = MAX_NAME_LENGTH)
	private String name;

	@Pattern(regexp = NAME_PATTERN)
	@Size(min = MIN_SURNAME_LENGTH, max = MAX_SURNAME_LENGTH)
	@Column(name = SURNAME_COLUMN_NAME, length = MAX_SURNAME_LENGTH)
	private String surname;

	@JsonProperty("failed_auth_count")
	@NotNull
	@Max(MAX_FAILED_AUTH_VALUE)
	@Column(name = FAILED_AUTH_COUNT_COLUMN_NAME, nullable = false)
	private Integer failedAuthCount = 0;

	@JsonIgnore
	@NotNull
	@Size(max = MAX_REGISTRATION_TOKEN_LENGTH)
	@Column(name = REGISTRATION_TOKEN_COLUMN_NAME, nullable = false, length = MAX_REGISTRATION_TOKEN_LENGTH)
	private String registrationToken = "";

	@JsonIgnore
	@Column(name = BLOCKING_EXPIRES_AT_COLUMN_NAME)
	private LocalDateTime blockingExpiresAt;

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

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public Integer getFailedAuthCount() {
		return failedAuthCount;
	}

	public void setFailedAuthCount(Integer failedAuthCount) {
		this.failedAuthCount = failedAuthCount;
	}

	public String getRegistrationToken() {
		return registrationToken;
	}

	public void setRegistrationToken(String registrationToken) {
		this.registrationToken = registrationToken;
	}

	public LocalDateTime getBlockingExpiresAt() {
		return blockingExpiresAt;
	}

	public void setBlockingExpiresAt(LocalDateTime blockingExpiresAt) {
		this.blockingExpiresAt = blockingExpiresAt;
	}

	@Override
	public String toString() {
		return "User{" +
				"id=" + id +
				", login='" + login + '\'' +
				", type=" + type +
				", email='" + email + '\'' +
				", image=" + image +
				", name='" + name + '\'' +
				", surname='" + surname + '\'' +
				", failedAuthCount=" + failedAuthCount +
				", registrationToken='" + registrationToken + '\'' +
				'}';
	}

	public enum Type {
		ROLE_BANNED,
		ROLE_USER,
		ROLE_ADMIN,
		ROLE_MASTER;

		public String getRole() {
			return this.name().substring(this.name().indexOf('_') + 1);
		}
	}

	public static final class Builder {

		private User user;

		private Builder() {
			user = new User();
		}

		public static Builder anUser() {
			return new Builder();
		}

		public Builder setId(Integer id) {
			user.setId(id);
			return this;
		}

		public Builder setLogin(String login) {
			user.setLogin(login);
			return this;
		}

		public Builder setType(Type type) {
			user.setType(type);
			return this;
		}

		public Builder setEmail(String email) {
			user.setEmail(email);
			return this;
		}

		public Builder setPassword(String password) {
			user.setPassword(password);
			return this;
		}

		public Builder setImage(Image image) {
			user.setImage(image);
			return this;
		}

		public Builder setName(String name) {
			user.setName(name);
			return this;
		}

		public Builder setSurname(String surname) {
			user.setSurname(surname);
			return this;
		}

		public Builder setFailedAuthCount(Integer failedAuthCount) {
			user.setFailedAuthCount(failedAuthCount);
			return this;
		}

		public Builder setRegistrationToken(String registrationToken) {
			user.setRegistrationToken(registrationToken);
			return this;
		}

		public Builder setBlockingExpiresAt(LocalDateTime blockingExpiresAt) {
			user.setBlockingExpiresAt(blockingExpiresAt);
			return this;
		}

		public User build() {
			return user;
		}
	}
}
