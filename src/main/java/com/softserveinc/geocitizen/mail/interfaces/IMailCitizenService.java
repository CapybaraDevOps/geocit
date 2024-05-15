package com.softserveinc.geocitizen.mail.interfaces;

import javax.mail.MessagingException;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/2/18 at 11:21 PM
 */
public interface IMailCitizenService {

	void send(final ICitizenEmailMessage message) throws MessagingException;
}
