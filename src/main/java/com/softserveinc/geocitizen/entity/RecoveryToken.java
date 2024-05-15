package com.softserveinc.geocitizen.entity;

import com.softserveinc.geocitizen.entity.interfaces.Identifiable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

import static com.softserveinc.geocitizen.entity.RecoveryToken.TABLE_NAME;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/2/18 at 2:36 PM
 */
@Entity
@Table(name = TABLE_NAME)
public class RecoveryToken implements Identifiable<Integer> {

	public static final String TABLE_NAME = "recovery_tokens";
	public static final String ID_COLUMN_NAME = "id";
	public static final String TOKEN_COLUMN_NAME = "token";
	public static final String USER_COLUMN_NAME = "user_id";
	public static final String EXPIRES_AT_COLUMN_NAME = "expires_at";
	public static final int MAX_TOKEN_LENGTH = 32;
	public static final int MIN_TOKEN_LENGTH = MAX_TOKEN_LENGTH;

	@Id
	@NotNull
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recovery_tokens_seq_gen")
	@SequenceGenerator(name = "recovery_tokens_seq_gen", sequenceName = "recovery_tokens_id_seq", allocationSize = 1)
	@Column(name = ID_COLUMN_NAME, nullable = false, unique = true)
	private Integer id;

	@NotNull
	@NotBlank
	@Size(min = MIN_TOKEN_LENGTH, max = MAX_TOKEN_LENGTH)
	@Column(name = TOKEN_COLUMN_NAME, nullable = false, unique = true, length = MAX_TOKEN_LENGTH)
	private String token;

	@NotNull
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
	@JoinColumn(name = USER_COLUMN_NAME, nullable = false)
	private User user;

	@NotNull
	@Column(name = EXPIRES_AT_COLUMN_NAME, nullable = false)
	private LocalDateTime expiresAt = LocalDateTime.now().plusHours(1);

	@Override
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public LocalDateTime getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(LocalDateTime expiresAt) {
		this.expiresAt = expiresAt;
	}

	@Override
	public String toString() {
		return "RecoveryToken{" +
				"id=" + id +
				", token='" + token + '\'' +
				", user=" + user +
				", expiresAt=" + expiresAt +
				'}';
	}

	public static final class Builder {

		private RecoveryToken recoveryToken;

		private Builder() {
			recoveryToken = new RecoveryToken();
		}

		public static Builder aRecoveryToken() {
			return new Builder();
		}

		public Builder setId(Integer id) {
			recoveryToken.setId(id);
			return this;
		}

		public Builder setToken(String token) {
			recoveryToken.setToken(token);
			return this;
		}

		public Builder setUser(User user) {
			recoveryToken.setUser(user);
			return this;
		}

		public Builder setExpiresAt(LocalDateTime expiresAt) {
			recoveryToken.setExpiresAt(expiresAt);
			return this;
		}

		public RecoveryToken build() {
			return recoveryToken;
		}
	}
}
