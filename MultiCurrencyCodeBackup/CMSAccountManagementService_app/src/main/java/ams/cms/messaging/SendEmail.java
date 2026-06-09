package ams.cms.messaging;

import java.util.Properties;

import javax.mail.Session;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Component;

@Component
public class SendEmail 
{
	public String sendEmailCustomer(String emailId, String otp) 
	{
		String stringSenderEmail = "contactus@AMStechnologies.com";
		String stringReceiverEmail = emailId;
		String stringPasswordSenderEmail = "Waz80463";
		String stringHost = "smtp.office365.com";

		Properties properties = System.getProperties();
		properties.put("mail.smtp.host", stringHost);
		properties.put("mail.smtp.port", "587");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");

		properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		javax.mail.Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			@Override
			protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(stringSenderEmail, stringPasswordSenderEmail);
			}
		});
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {

					Message message = new MimeMessage(session);
					message.setFrom(new InternetAddress(stringSenderEmail));
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(stringReceiverEmail));
					message.setSubject("VERIFICATION OTP");
					message.setText("Dear user, your one time password is\r\n" + "\r\n" + otp + "\r\n\r\n"
							+ " Please enter the OTP to proceed. Powered by AMS Technologies Pvt Ltd.\n");

					Transport.send(message);

					System.out.println("Done");
					

				} catch (MessagingException e) {
					throw new RuntimeException(e);
				}
			}
		});
		thread.start();
		return "Email Sent Successfully";
	}

}
