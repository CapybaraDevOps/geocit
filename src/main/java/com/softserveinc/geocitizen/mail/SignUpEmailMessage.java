package com.softserveinc.geocitizen.mail;

import java.time.LocalDateTime;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/22/18 at 2:14 PM
 */
public class SignUpEmailMessage extends AbstractCitizenEmailMessage {

	private static String frontUrl;

	{
		setSubject("Registration submitting");
	}

	public static void setFrontUrl(String frontUrl) {
		SignUpEmailMessage.frontUrl = frontUrl;
	}

	@Override
	public String getPreparedEmailContent() {
		return "<table border=\"0\" cellspacing=\"0\" width=\"100%\"><tr><td></td><td width=\"700\">" +
				getHeader() +
				getMessage() +
				getFooter() +
				"</td><td></td></tr></table>";
	}

	public void setMessage(final String token, final String login) {
		this.setMessage("<div style=\"background-color: #303030; color: #d3d3d3; padding: 16px\">" +
				"<h3>Hello, <span style=\"font-style: italic\">" + login + "!</span></h3>" +
				"<h4>We've received a registration request today at " + LocalDateTime.now() + ".</h4>" +
				"<p>Follow next link to submit your E-Mail address: <b><a style=\"color: #9636ff\" href=\""
				+ frontUrl + "/submitSignUp/" + login + "/" + token + "\">CLICK ME</a></b>.</p>" +
				"<p>If you haven't sent the request then just ignore this message.</p>");
	}

	public static final class Builder {

		private SignUpEmailMessage signUpEmailMessage;

		private Builder() {
			signUpEmailMessage = new SignUpEmailMessage();
		}

		public static Builder aSignUpEmailMessage() {
			return new Builder();
		}

		public Builder setContentType(String contentType) {
			signUpEmailMessage.setContentType(contentType);
			return this;
		}

		public Builder setDestEmail(String destEmail) {
			signUpEmailMessage.setDestEmail(destEmail);
			return this;
		}

		public Builder setEncoding(String encoding) {
			signUpEmailMessage.setEncoding(encoding);
			return this;
		}

		public Builder setMessage(final String token, final String login) {
			signUpEmailMessage.setMessage(token, login);
			return this;
		}

		public Builder setSubject(String subject) {
			signUpEmailMessage.setSubject(subject);
			return this;
		}

		public SignUpEmailMessage build() {
			return signUpEmailMessage;
		}
	}
}
