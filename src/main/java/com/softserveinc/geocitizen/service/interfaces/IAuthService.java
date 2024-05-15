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

package com.softserveinc.geocitizen.service.interfaces;

import com.softserveinc.geocitizen.dto.*;
import com.softserveinc.geocitizen.exception.AbstractCitizenException;

import javax.mail.MessagingException;

/**
 * Interface for managing user accounts.
 *
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 12/21/17 at 5:27 PM
 */
public interface IAuthService {

	/**
	 * Creates new "session" for recovering of the users password.
	 *
	 * @param login
	 * 		of the user
	 *
	 * @return the users login
	 *
	 * @throws AbstractCitizenException
	 */
	String generateRecoveryToken(final String login, final String ip) throws AbstractCitizenException, MessagingException;

	/**
	 * Returns current user's session information.
	 *
	 * @return DTO with the information.
	 */
	UserSessionDTO getCurrentSession();

	/**
	 * Recovers users password with new one via received token.
	 *
	 * @param dto
	 * 		that contains necessary data
	 *
	 * @return DTO with registered user information
	 *
	 * @see com.softserveinc.geocitizen.dto.PasswordRecoveryDTO
	 * @see com.softserveinc.geocitizen.dto.RegisteredUserDTO
	 */
	RegisteredUserDTO recoverPassword(PasswordRecoveryDTO dto) throws AbstractCitizenException;

	/**
	 * Creates new user account and returns error.
	 *
	 * @param user
	 * 		that contains information for registration
	 *
	 * @return DTO with registered user information
	 *
	 * @throws AbstractCitizenException
	 * @see RegisterUserDTO
	 * @see com.softserveinc.tools.model.JsonResponse
	 */
	RegisteredUserDTO signUp(final RegisterUserDTO dto) throws AbstractCitizenException, MessagingException;

	void submitSignUp(final SubmitRegistrationDTO dto) throws AbstractCitizenException;

	RegisteredUserDTO update(RegisterUserDTO user) throws AbstractCitizenException;

}
