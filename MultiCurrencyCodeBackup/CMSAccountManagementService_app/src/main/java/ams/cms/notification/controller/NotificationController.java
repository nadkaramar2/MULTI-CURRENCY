package ams.cms.notification.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.config.AppInfo;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/notify-by")
public class NotificationController 
{
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private	AppInfo appInfo;
	
	@RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
	public ResponseEntity<?> sendEmail(@RequestBody EmailTemplate emailTemplate)
	{
		String result = "";
		try 
		{
			if (emailTemplate.getStrPathToAttachment()!=null) 
			{
				result = emailService.sendMessageWithAttachment(emailTemplate);
			}
			else
			{
				result = emailService.sendSimpleHtmlContentMessage(emailTemplate);				
			}
			return ResponseEntity.ok(result);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/sendTestEmail", method = RequestMethod.POST)
	public ResponseEntity<?> sendEmailForTest()
	{
		try 
		{
			boolean isMailSent = sendTestMail();
			System.out.println("isMailSent::"+isMailSent);
			
			if (isMailSent) 
			{
				return ResponseEntity.ok("Success");
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
		return ResponseEntity.ok("failure");
	}
	
	private boolean sendTestMail() 
	{
		try 
		{
			String emailId = "ssoni@AMStechnologies.com";
			
			StringBuilder bodyMsg = new StringBuilder("Dear User, ");
			bodyMsg.append("<br/><br/>"); 
  		  	bodyMsg.append( "Please find the below account number and user id:");
  		  	bodyMsg.append("<br/>");
  		  	bodyMsg.append("Account Number is : 0600000054");
  		  	//bodyMsg.append("<br/>");
  		  	//bodyMsg.append("User Id is: 9321561968");
  		  	
  		  	bodyMsg.append("<br/><br/>"); 
  		  	
  		  	bodyMsg.append("Powered by Montra");
  		  
  		  	String subject = "Account Creation Details";	
  		  	
  		  	//String fromMailId = "contactus@AMStechnologies.com";
  		  	
  		  	String fromMailId = "no-reply@montra.org";
  		  	//String fromMailId = "pgsupport@montra.org";
			
  		  	EmailTemplate emailTemplate =	Utils.getEmailTemplateForSendMail(null, emailId, subject, bodyMsg.toString());
  		  	emailTemplate.setStrParticipantid(appInfo.getStrParticipantId());
		
  		  	emailService.sendSimpleHtmlContentMessage(emailTemplate);
  		  	
  		  	return true;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return false;
	}
}
