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

package com.softserveinc.geocitizen.security.service;

import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.repository.UsersRepository;
import com.softserveinc.geocitizen.security.exception.EmailNotFoundException;
import com.softserveinc.geocitizen.security.exception.LoginNotFoundException;
import com.softserveinc.geocitizen.security.model.AuthorizedUser;
import com.softserveinc.geocitizen.security.service.interfaces.ICitizenUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static com.softserveinc.geocitizen.entity.User.MAX_FAILED_AUTH_VALUE;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/21/17 at 3:23 PM
 */
@Service
@Transactional
public class UserDetailsServiceImpl implements ICitizenUserDetailsService {

	private final UsersRepository usersRepository;

	@Autowired
	public UserDetailsServiceImpl(UsersRepository usersRepository) {
		this.usersRepository = usersRepository;
	}

	@ReadOnlyProperty
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = usersRepository.getByLogin(login);

		if (user == null) {
			throw new LoginNotFoundException(login);
		}
		return new AuthorizedUser(user, getAuthorities(user));
	}

	public static Set<GrantedAuthority> getAuthorities(User user) {
		Set<GrantedAuthority> authorities = new HashSet<>();

		authorities.add(new SimpleGrantedAuthority(user.getType().name()));
		return authorities;
	}

	@ReadOnlyProperty
	@Override
	public UserDetails loadUserByEmail(final String email) throws EmailNotFoundException {
		User user = usersRepository.getByEmail(email);

		if (user == null) {
			throw new EmailNotFoundException(email);
		}
		return new AuthorizedUser(user, getAuthorities(user));
	}

	@Override
	public void increaseUserFailedAttempts(final UserDetails userDetails) {
		User user = usersRepository.getByLogin(userDetails.getUsername());

		user.setFailedAuthCount(user.getFailedAuthCount() + 1);

		if (user.getFailedAuthCount() >= MAX_FAILED_AUTH_VALUE) {
			user.setBlockingExpiresAt(LocalDateTime.now().plusMinutes(15));
		}
		usersRepository.save(user);
	}

	@Override
	public void resetUserFailedAttempts(final UserDetails userDetails) {
		User user = usersRepository.getByLogin(userDetails.getUsername());

		user.setFailedAuthCount(0);
		user.setBlockingExpiresAt(null);
		usersRepository.save(user);
	}
}
