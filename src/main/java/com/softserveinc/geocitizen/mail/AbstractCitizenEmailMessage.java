package com.softserveinc.geocitizen.mail;

import com.softserveinc.geocitizen.mail.interfaces.ICitizenEmailMessage;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/2/18 at 11:27 PM
 */
public abstract class AbstractCitizenEmailMessage implements ICitizenEmailMessage {

	private String contentType = "text/html";
	private String destEmail;
	private String encoding = "UTF-8";
	private String message;
	private String subject = "Geo Citizen";

	@Override
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	@Override
	public String getDestEmail() {
		return destEmail;
	}

	public void setDestEmail(String destEmail) {
		this.destEmail = destEmail;
	}

	@Override
	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	@Override
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	protected String getMessage() {
		return message;
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	public String getHeader() {
		return "<div style=\"background-color: #9636ff; color: #eeeeee; text-shadow: 2px 2px 5px #8a8a8a; " +
				"padding: 16px 24px 16px 24px; text-align: center\"><h1>Geo Citizen</h1>" +
				"<h3>We're doing it better...</h3></div>";
	}

	public String getFooter() {
		return "<div style=\"background-color: #9636ff; color: #d1d1d1; text-shadow: 2px 2px 5px #1d1d1d; " +
				"padding: 8px; text-align: center\"><h4>Thank you for that you're with us!<br />" +
				"<em>With respect and the best wishes, Geo Citizen team!</em></h4></div>";
	}
}
