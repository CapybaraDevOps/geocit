package com.softserveinc.geocitizen.mail;

import java.time.LocalDateTime;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/2/18 at 11:49 PM
 */
public class PasswordRecoveryEmailMessage extends AbstractCitizenEmailMessage {

	private static String frontUrl;

	{
		setSubject("Password recovery");
	}

	public static void setFrontUrl(String frontUrl) {
		PasswordRecoveryEmailMessage.frontUrl = frontUrl;
	}

	@Override
	public String getPreparedEmailContent() {
		return "<table border=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td></td><td width=\"700\">" +
				getHeader() +
				getMessage() +
				getFooter() +
				"</td><td></td></tr></table>";
	}

	public void setMessage(final String token, final String login, final String clientIpAddress) {
		this.setMessage("<div style=\"background-color: #303030; color: #d3d3d3; padding: 16px\">" +
				"<h3>Hello, <span style=\"font-style: italic\">" + login + "!</span></h3>" +
				"<h4>We've received a password recovering request today at " + LocalDateTime.now() +
				" from the  IP-address " + clientIpAddress + ".</h4>" +
				"<p>Use the following token to continue: <b style=\"color: #9636ff\">" + token + "</b>.</p>" +
				"<p>Or just follow next link: <b><a style=\"color: #9636ff\" href=\"" + frontUrl +
				"/auth/passwordRecovery/" + login + "/" + token + "\">CLICK ME</a></b>.</p>" +
				"<p>If you haven't sent the request then just ignore this message.</p>");
	}

	public static final class Builder {

		private PasswordRecoveryEmailMessage passwordRecoveryEmailMessage;

		private Builder() {
			passwordRecoveryEmailMessage = new PasswordRecoveryEmailMessage();
		}

		public static Builder aPasswordRecoveryEmailMessage() {
			return new Builder();
		}

		public Builder setContentType(String contentType) {
			passwordRecoveryEmailMessage.setContentType(contentType);
			return this;
		}

		public Builder setDestEmail(String destEmail) {
			passwordRecoveryEmailMessage.setDestEmail(destEmail);
			return this;
		}

		public Builder setEncoding(String encoding) {
			passwordRecoveryEmailMessage.setEncoding(encoding);
			return this;
		}

		public Builder setMessage(String token, String login, String clientIpAddress) {
			passwordRecoveryEmailMessage.setMessage(token, login, clientIpAddress);
			return this;
		}

		public Builder setSubject(String subject) {
			passwordRecoveryEmailMessage.setSubject(subject);
			return this;
		}

		public PasswordRecoveryEmailMessage build() {
			return passwordRecoveryEmailMessage;
		}
	}
}
