package com.softserveinc.geocitizen.security.service.interfaces;

import com.softserveinc.geocitizen.security.exception.EmailNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/4/18 at 3:35 PM
 */
public interface ICitizenUserDetailsService extends UserDetailsService {

	UserDetails loadUserByEmail(final String email) throws EmailNotFoundException;

	void increaseUserFailedAttempts(final UserDetails userDetails);

	void resetUserFailedAttempts(final UserDetails userDetails);
}
