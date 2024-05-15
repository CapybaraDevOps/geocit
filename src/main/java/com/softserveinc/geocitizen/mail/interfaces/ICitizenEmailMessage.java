package com.softserveinc.geocitizen.mail.interfaces;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/2/18 at 11:33 PM
 */
public interface ICitizenEmailMessage {

	String getContentType();

	String getDestEmail();

	String getEncoding();

	String getPreparedEmailContent();

	String getSubject();
}
