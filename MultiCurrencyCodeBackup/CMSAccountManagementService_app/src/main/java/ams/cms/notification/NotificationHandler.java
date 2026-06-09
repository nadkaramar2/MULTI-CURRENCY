package ams.cms.notification;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import ams.cms.config.AppInfo;
import ams.cms.model.EmailSettingMaster;
import ams.cms.services.EmailSettingMasterService;

@Component
public class NotificationHandler 
{
	@Autowired
	private EmailSettingMasterService emailSettingMasterService;
	
	@Autowired
	private	AppInfo appInfo;
	
	//@Bean
	public JavaMailSender getJavaMailSender() 
	{
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		try 
		{
			System.out.println("Email emailSettingMasterService::"+emailSettingMasterService);
			
			EmailSettingMaster emailSettingMaster = new EmailSettingMaster();
			emailSettingMaster.setStrParticipantId(appInfo.getStrParticipantId());
			
			EmailSettingMaster response = emailSettingMasterService.getEmailSettingInfoBasedOnParticipant(emailSettingMaster);
			System.out.println("Email response::"+response);

			mailSender.setHost(response.getHost());			
			mailSender.setPort(response.getPort());
			
			mailSender.setUsername(response.getUsername());
			mailSender.setPassword(response.getPassword());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	    //mailSender.setHost("smtp.gmail.com");
	    //mailSender.setHost("smtp.office365.com");
	    
	    //mailSender.setHost("email-smtp.eu-west-1.amazonaws.com");
	    //mailSender.setPort(587);
	    
	    //mailSender.setUsername("contactus@AMStechnologies.com");
	    //mailSender.setPassword("S3QnR0@2022");
	    
	    //mailSender.setUsername("AKIARVYL2PGCUDMKZKET");
	    //mailSender.setPassword("BHZGXd1Nh34wXLxC7S+6xlO8cp8FQ34mC0O84jRCmFq5");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "true");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	    
	    
	    return mailSender;
	}
	
	public JavaMailSender getJavaMailSenderBasedOnParticipant(EmailTemplate emailTemplate)
	{
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();;
		try 
		{
			String FromApprovedEmailId = "contactus@AMStechnologies.com";
			String footerSignatureText = "Powered by AMS Technologies Pvt Ltd.";
			
			getDefaultJavaMailSender(mailSender);
			System.out.println("mailSender:::"+mailSender);
			
			String participantId = emailTemplate.getStrParticipantid();
			System.out.println("participantId=["+participantId+"]");
			
			if (participantId != null && participantId.trim().length() > 0 ) 
			{
				mailSender = null;
				mailSender = new JavaMailSenderImpl();
				
				EmailSettingMaster emailSettingMaster = new EmailSettingMaster();
				emailSettingMaster.setStrParticipantId(participantId);
				
				EmailSettingMaster response = emailSettingMasterService.getEmailSettingInfoBasedOnParticipantId(emailSettingMaster);
				System.out.println("Email response::"+response);
				
				mailSender.setHost(response.getHost());			
				mailSender.setPort(response.getPort());
				
				mailSender.setUsername(response.getUsername());
				mailSender.setPassword(response.getPassword());
				
				
				FromApprovedEmailId = response.getFromEmailAddress();
				footerSignatureText = response.getFooterSignatureText();
			}
			
			String emailContent = emailTemplate.getStrText() + "<br/><br/><br/>" + footerSignatureText;
			
			emailTemplate.setStrFrom(FromApprovedEmailId);
			emailTemplate.setStrText(emailContent);
			
			Properties props = mailSender.getJavaMailProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.debug", "true");
			props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	    return mailSender;
	}
	
	private JavaMailSender getDefaultJavaMailSender(JavaMailSenderImpl mailSender) 
	{
		//JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		
		//mailSender.setHost("smtp.gmail.com");
	    mailSender.setHost("smtp.office365.com");	    
	    mailSender.setPort(587);
	    
	    mailSender.setUsername("contactus@AMStechnologies.com");
	    mailSender.setPassword("Waz80463");	
	    
	    return mailSender;
	}
}

