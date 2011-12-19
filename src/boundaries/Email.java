package boundaries;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

import entities.Config;
import entities.Constants;

public class Email {

	private static Logger logger = Logger.getLogger(Email.class);

	public static void sendHtmlReportInEmail(Config config)
			throws AddressException, MessagingException {

		String[] to = config.getEmailTo().split(",");
		String temp = "";
		for (int i = 0; i < to.length; i++) {
			temp += to[i] + ",";
		}

		logger.info("Sending email to: " + temp.substring(0, temp.length() - 1));

		String host = config.getEmailHost();
		String from = config.getEmailFrom();
		String pass = config.getEmailPassword();
		Properties props = System.getProperties();
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.user", from);
		props.put("mail.smtp.password", pass);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.required", "true");

		Session session = Session.getDefaultInstance(props, null);
		MimeMessage message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));

		InternetAddress[] toAddress = new InternetAddress[to.length];

		// To get the array of addresses
		for (int i = 0; i < to.length; i++) {
			toAddress[i] = new InternetAddress(to[i]);
		}

		for (int i = 0; i < toAddress.length; i++) {

			message.addRecipient(Message.RecipientType.TO, toAddress[i]);
		}

		// Create the message part
		BodyPart messageBodyPart = new MimeBodyPart();

		// Fill the message
		messageBodyPart.setContent(HTMLReport.getReportContent(), "text/html");

		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		// Set text message part
		multipart.addBodyPart(messageBodyPart);

		// Part two is attachment
		if (config.isEmailAttachedLog()) {
			messageBodyPart = new MimeBodyPart();
			String filename = Constants.OUT_DIR + "/log.txt";
			DataSource source = new FileDataSource(filename);
			messageBodyPart.setDataHandler(new DataHandler(source));
			messageBodyPart.setFileName("log.txt");

			multipart.addBodyPart(messageBodyPart);
		}

		// Send the complete message parts
		message.setContent(multipart);

		message.setSubject("Automation Report (Auto-Generated Email)");

		Transport transport = session.getTransport("smtps");
		transport.connect(host, from, pass);
		transport.sendMessage(message, message.getAllRecipients());
		transport.close();

	}

}
