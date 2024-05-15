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

package com.softserveinc.geocitizen.security;

import com.softserveinc.geocitizen.security.exception.CitizenBadCredentialsException;
import com.softserveinc.geocitizen.security.exception.NotSubmittedUserRegistrationException;
import com.softserveinc.geocitizen.security.exception.UserFailedAuthenticationCountOverflow;
import com.softserveinc.geocitizen.security.model.AuthorizedUser;
import com.softserveinc.geocitizen.security.service.interfaces.ICitizenUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static com.softserveinc.geocitizen.entity.User.MAX_FAILED_AUTH_VALUE;
import static com.softserveinc.geocitizen.entity.User.MIN_PASSWORD_LENGTH;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/21/17 at 3:11 PM
 */
public class AuthProvider implements AuthenticationProvider, InitializingBean {

	private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();
	private PasswordEncoder passwordEncoder;
	private ICitizenUserDetailsService userDetailsService;

	@Override
	public void afterPropertiesSet() {
		if (userDetailsService == null) {
			throw new IllegalArgumentException("A UserDetailsService must be set");
		}
	}

	@Override
	public Authentication authenticate(Authentication authentication) {
		String loginOrEmail = (authentication.getPrincipal() == null) ? null : authentication.getName();
		UserDetails userDetails = retrieveUserDetails(loginOrEmail);

		additionalAuthenticationChecks((AuthorizedUser) userDetails,
				(UsernamePasswordAuthenticationToken) authentication);
		return createSuccessAuthentication(authentication, userDetails);
	}

	private UserDetails retrieveUserDetails(String loginOrEmail) {
		UserDetails userDetails;

		if (!loginOrEmail.contains("@")) {
			userDetails = userDetailsService.loadUserByUsername(loginOrEmail);
		} else {
			userDetails = userDetailsService.loadUserByEmail(loginOrEmail);
		}

		if (userDetails == null) {
			throw new InternalAuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		} else if (userDetails instanceof AuthorizedUser
				&& ((AuthorizedUser) userDetails).getFailedAuthCount() >= MAX_FAILED_AUTH_VALUE
				&& LocalDateTime.now().isBefore(((AuthorizedUser) userDetails).getBlockingExpiresAt())) {
			throw new UserFailedAuthenticationCountOverflow(loginOrEmail);
		}
		return userDetails;
	}

	/**
	 * Main method that respond login page password validation
	 *
	 * @param userDetails
	 * @param authentication
	 *
	 * @throws BadCredentialsException
	 * 		if invalid password was entered
	 */
	private void additionalAuthenticationChecks(
			AuthorizedUser userDetails,
			UsernamePasswordAuthenticationToken authentication) {
		if (authentication.getCredentials() == null) {
			throw new CitizenBadCredentialsException(userDetails.getUsername());
		}

		if (!StringUtils.isEmpty(userDetails.getRegistrationToken())) {
			throw new NotSubmittedUserRegistrationException(userDetails.getUsername());
		}

		String password = authentication.getCredentials().toString();

		if (password.length() < MIN_PASSWORD_LENGTH
				|| !passwordEncoder.matches(password, userDetails.getPassword())) {
			if (userDetails.getBlockingExpiresAt() == null) {
				userDetailsService.increaseUserFailedAttempts(userDetails);
			}
			throw new CitizenBadCredentialsException(
					userDetails.getUsername(), userDetails.getFailedAuthCount() + 1);
		}
		userDetailsService.resetUserFailedAttempts(userDetails);
	}

	private Authentication createSuccessAuthentication(Authentication authentication, UserDetails user) {
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
				user,
				authentication.getCredentials(),
				authoritiesMapper.mapAuthorities(user.getAuthorities()));

		authenticationToken.setDetails(authentication.getDetails());
		return authenticationToken;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setUserDetailsService(ICitizenUserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}
}
