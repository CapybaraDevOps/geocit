package com.softserveinc.geocitizen.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import static com.softserveinc.geocitizen.entity.UserConnection.TABLE_NAME;


@Entity
@Table(name = TABLE_NAME)
public class UserConnection {

	public static final String TABLE_NAME = "userconnection";
	public static final String USER_ID_COLUMN_NAME = "userid";
	public static final String PROVIDER_COLUMN_NAME = "providerid";
	public static final String PROVIDER_USER_ID_COLUMN_NAME = "provideruserid";

	@Id
	@NotNull
	@Column(name = USER_ID_COLUMN_NAME)
	private String userId;

	@NotNull
	@Column(name = PROVIDER_COLUMN_NAME)
	private String provider;

	@NotNull
	@Column(name = PROVIDER_USER_ID_COLUMN_NAME)
	private String providerUserId;


	public String getId() {
		return userId;
	}

	public void setId(String userId) {
		this.userId = userId;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	@Override
	public String toString() {
		return "UserConnection{" +
				"userId='" + userId + '\'' +
				", provider='" + provider + '\'' +
				", providerUserId='" + providerUserId + '\'' +
				'}';
	}
}
