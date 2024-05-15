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

package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.dto.*;
import com.softserveinc.geocitizen.dto.mapper.RegisteredUserMapper;
import com.softserveinc.geocitizen.entity.RecoveryToken;
import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.exception.*;
import com.softserveinc.geocitizen.mail.PasswordRecoveryEmailMessage;
import com.softserveinc.geocitizen.mail.SignUpEmailMessage;
import com.softserveinc.geocitizen.mail.interfaces.IMailCitizenService;
import com.softserveinc.geocitizen.repository.RecoveryTokensRepository;
import com.softserveinc.geocitizen.repository.UsersRepository;
import com.softserveinc.geocitizen.security.exception.EmailNotFoundException;
import com.softserveinc.geocitizen.security.exception.LoginNotFoundException;
import com.softserveinc.geocitizen.security.exception.TooManyNonExpiredRecoveryTokensException;
import com.softserveinc.geocitizen.security.model.AuthorizedUser;
import com.softserveinc.geocitizen.security.service.UserDetailsServiceImpl;
import com.softserveinc.geocitizen.service.interfaces.IAuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Collections;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/21/17 at 5:26 PM
 */
@Service
@Transactional
public class AuthServiceImpl implements IAuthService {

	private final UsersRepository repository;
	private final RecoveryTokensRepository tokensRepository;
	private final PasswordEncoder passwordEncoder;
	private final RegisteredUserMapper userToRegisteredUserDto;
	private final IMailCitizenService mailService;
	private final AuthenticationTrustResolver authTrustResolver;

	@Autowired
	public AuthServiceImpl(
			UsersRepository repository,
			RecoveryTokensRepository tokensRepository,
			PasswordEncoder passwordEncoder,
			RegisteredUserMapper userToRegisteredUserDto,
			IMailCitizenService mailService,
			AuthenticationTrustResolver authTrustResolver) {
		this.repository = repository;
		this.tokensRepository = tokensRepository;
		this.passwordEncoder = passwordEncoder;
		this.userToRegisteredUserDto = userToRegisteredUserDto;
		this.mailService = mailService;
		this.authTrustResolver = authTrustResolver;
	}

	@Override
	public String generateRecoveryToken(final String loginOrEmail, final String ip)
			throws AbstractCitizenException, MessagingException {
		final User user;
		if (!loginOrEmail.contains("@")) {
			user = repository.getByLogin(loginOrEmail);
			if (user == null) {
				throw new LoginNotFoundException(loginOrEmail);
			}
		} else {
			user = repository.getByEmail(loginOrEmail);
			if (user == null) {
				throw new EmailNotFoundException(loginOrEmail);
			}
		}

		if (tokensRepository.countNonExpiredByUser(user.getId()) > 2) {
			throw new TooManyNonExpiredRecoveryTokensException(user.getLogin());
		}

		final RecoveryToken token = RecoveryToken.Builder.aRecoveryToken()
				.setUser(user)
				.setToken(DigestUtils.md5DigestAsHex((loginOrEmail + ip + LocalDateTime.now().toString()).getBytes()))
				.build();

		tokensRepository.save(token);
		mailService.send(PasswordRecoveryEmailMessage.Builder.aPasswordRecoveryEmailMessage()
				.setDestEmail(user.getEmail())
				.setMessage(token.getToken(), user.getLogin(), ip)
				.build());
		return loginOrEmail;
	}

	@Override
	public UserSessionDTO getCurrentSession() {
		final AuthorizedUser authorizedUser = AuthorizedUser.getCurrent();

		if (isCurrentAuthenticationAnonymous(
				SecurityContextHolder.getContext().getAuthentication(), authTrustResolver)
				|| authorizedUser == null) {
			return new UserSessionDTO(false);
		}
		return new UserSessionDTO(
				authorizedUser.getId(),
				authorizedUser.getUsername(),
				authorizedUser.getType());
	}

	@Override
	public RegisteredUserDTO recoverPassword(final PasswordRecoveryDTO dto) throws AbstractCitizenException {
		final RecoveryToken token = tokensRepository.getByToken(dto.getToken());
		final User user = repository.getByLogin(dto.getLogin());

		checkDataForPasswordRecovering(token, user);
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user.setFailedAuthCount(0);
		repository.save(user);
		tokensRepository.delete(token);
		return userToRegisteredUserDto.userToRegisteredUserDto(user);
	}

	@Override
	public RegisteredUserDTO signUp(final RegisterUserDTO dto) throws AbstractCitizenException, MessagingException {
		if (repository.getByLogin(dto.getLogin()) != null) {
			throw new EntityNotUniqueException(EntityNotUniqueException.Entity.USER, "login");
		}

		if (repository.getByEmail(dto.getEmail()) != null) {
			throw new EntityNotUniqueException(EntityNotUniqueException.Entity.USER, "email");
		}
		dto.setPassword(passwordEncoder.encode(dto.getPassword()));

		final User user = User.Builder.anUser()
				.setLogin(dto.getLogin())
				.setEmail(dto.getEmail())
				.setPassword(dto.getPassword())
				.setName(dto.getName())
				.setSurname(dto.getSurname())
				.setRegistrationToken(DigestUtils.md5DigestAsHex((dto.getLogin() + dto.getPassword()).getBytes()))
				.build();

		repository.save(user);
		mailService.send(SignUpEmailMessage.Builder.aSignUpEmailMessage()
				.setDestEmail(user.getEmail())
				.setMessage(user.getRegistrationToken(), user.getLogin())
				.build());
		return userToRegisteredUserDto.userToRegisteredUserDto(user);
	}

	@Override
	public void submitSignUp(SubmitRegistrationDTO dto) throws AbstractCitizenException {
		final User user = repository.getByLogin(dto.getLogin());

		if (user == null) {
			throw new EntityNotUniqueException(EntityNotUniqueException.Entity.USER, "login");
		}

		if (!StringUtils.equals(user.getRegistrationToken(), dto.getRegistrationToken())) {
			throw new IllegalArgumentException(SubmitRegistrationDTO.REGISTRATION_TOKEN_FIELD);
		}
		user.setRegistrationToken("");
		repository.save(user);
	}

	@Override
	public RegisteredUserDTO update(final RegisterUserDTO user) throws AbstractCitizenException {
		if (repository.getByLogin(user.getLogin()) != null) {
			throw new EntityNotUniqueException(EntityNotUniqueException.Entity.USER, "login");
		}
		final User savedUser = repository.getByEmail(user.getEmail());
		savedUser.setName(user.getName());
		savedUser.setSurname(user.getSurname());
		savedUser.setPassword(passwordEncoder.encode(user.getPassword()));
		savedUser.setLogin(user.getLogin());
		repository.save(savedUser);
		AuthorizedUser authorizedUser = new AuthorizedUser(savedUser, UserDetailsServiceImpl.getAuthorities(savedUser));
		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(authorizedUser, null,
						Collections.singleton(new SimpleGrantedAuthority(savedUser.getType().name()))));

		return userToRegisteredUserDto.userToRegisteredUserDto(savedUser);
	}

	private void checkDataForPasswordRecovering(RecoveryToken token, User user)
			throws AbstractCitizenException {
		if (token == null) {
			throw new EntityNotExistException(EntityNotExistException.Entity.RECOVERY_TOKEN);
		}

		if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
			throw new ExpiredRecoveryTokenException();
		}

		if (user == null) {
			throw new EntityNotExistException(EntityNotExistException.Entity.USER, "login");
		}

		if (!user.getId().equals(token.getUser().getId())) {
			throw new IllegalParameterException("login");
		}
	}

	private boolean isCurrentAuthenticationAnonymous(final Authentication auth,
	                                                 final AuthenticationTrustResolver authTrustResolver) {
		return authTrustResolver.isAnonymous(auth);
	}
}
