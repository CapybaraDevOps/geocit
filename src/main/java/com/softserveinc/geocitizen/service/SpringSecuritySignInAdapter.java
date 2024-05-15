package com.softserveinc.geocitizen.service;

import com.softserveinc.geocitizen.entity.User;
import com.softserveinc.geocitizen.security.exception.CitizenBadCredentialsException;
import com.softserveinc.geocitizen.security.model.AuthorizedUser;
import com.softserveinc.geocitizen.security.service.UserDetailsServiceImpl;
import com.softserveinc.geocitizen.service.interfaces.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.Collections;

@Service
public class SpringSecuritySignInAdapter implements SignInAdapter {

	@Autowired
	private IUserService userDetailsService;

	@Override
	public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) throws CitizenBadCredentialsException {
		if (AuthorizedUser.getCurrent() != null && !StringUtils.contains(connection.fetchUserProfile().getEmail(), AuthorizedUser.getCurrent().getEmail())) {
			throw new CitizenBadCredentialsException("Email doesn't match with yours");
		}
		User user = this.userDetailsService.getUser(Integer.parseInt(localUserId));
		AuthorizedUser authorizedUser = new AuthorizedUser(user, UserDetailsServiceImpl.getAuthorities(user));

		SecurityContextHolder.getContext().setAuthentication(
				new UsernamePasswordAuthenticationToken(authorizedUser, null,
						Collections.singleton(new SimpleGrantedAuthority(user.getType().name()))));
		return null;
	}
}
