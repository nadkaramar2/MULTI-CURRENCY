package ams.cms.notification.email;

import ams.cms.notification.EmailTemplate;

public interface EmailService 
{
	String sendSimpleMessage(EmailTemplate emailTemplate) throws Exception;
	
	String sendMessageWithAttachment(EmailTemplate emailTemplate) throws Exception;
	
	String sendSimpleHtmlContentMessage(EmailTemplate emailTemplate) throws Exception;
}
