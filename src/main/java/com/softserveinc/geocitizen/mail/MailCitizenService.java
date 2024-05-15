package com.softserveinc.geocitizen.mail;

import com.softserveinc.geocitizen.mail.interfaces.ICitizenEmailMessage;
import com.softserveinc.geocitizen.mail.interfaces.IMailCitizenService;
import com.sun.mail.smtp.SMTPTransport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

/**
 * @author softserveinc (https://t.me/Shrralis)
 * @version 1.0
 * Created 1/2/18 at 11:20 PM
 */
@Service
public class MailCitizenService implements IMailCitizenService {

	private final Properties emailProperties;

	@Autowired
	public MailCitizenService(Properties emailProperties) {
		this.emailProperties = emailProperties;
	}

	@Override
	public void send(final ICitizenEmailMessage message) throws MessagingException {
		final Session session = Session.getInstance(emailProperties, null);
		final MimeMessage msg = new MimeMessage(session);

		msg.setFrom(emailProperties.getProperty("senderEmail"));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(message.getDestEmail(), false));
		msg.setSubject(message.getSubject());
		msg.setContent(message.getPreparedEmailContent(),
				message.getContentType() + "; charset=" + message.getEncoding());
		msg.setSentDate(new Date());

		SMTPTransport transport = (SMTPTransport) session.getTransport("smtps");

		transport.connect(emailProperties.getProperty("mail.smtps.host"),
				emailProperties.getProperty("senderEmail"), emailProperties.getProperty("emailPassword"));
		transport.sendMessage(msg, msg.getAllRecipients());
		transport.close();
	}
}
