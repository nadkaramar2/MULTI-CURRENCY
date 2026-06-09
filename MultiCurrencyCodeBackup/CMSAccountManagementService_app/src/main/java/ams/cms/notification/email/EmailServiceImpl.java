package ams.cms.notification.email;

import java.io.File;
import java.util.Arrays;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import ams.cms.logger.AMSLogger;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.NotificationHandler;

@Transactional
@Service
public class EmailServiceImpl implements EmailService
{
	private AMSLogger amsLogger = AMSLogger.getInstance(EmailServiceImpl.class);
	
	@Autowired
	private NotificationHandler notificationHandler;
	
	//@Autowired
	//private JavaMailSender javaMailSender;

	@Override
	public String sendSimpleMessage(EmailTemplate emailTemplate) throws Exception
	{
		try 
		{
			JavaMailSender javaMailSender = getJavaMailSender(emailTemplate); //New Added for test
			
			// Creating a simple mail message
            SimpleMailMessage mailMessage = new SimpleMailMessage();
 
            // Setting up necessary details
            mailMessage.setFrom(emailTemplate.getStrFrom());
            mailMessage.setTo(emailTemplate.getStrTo());
            mailMessage.setText(emailTemplate.getStrText());
            mailMessage.setSubject(emailTemplate.getStrSubject());
 
            // Sending the mail
            javaMailSender.send(mailMessage);
            
            return "Success:Email Sent";
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String sendMessageWithAttachment(EmailTemplate emailTemplate) throws Exception 
	{
        
        try 
        {
        	JavaMailSender javaMailSender = getJavaMailSender(emailTemplate); //New Added for test
        	
        	MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        	MimeMessageHelper mimeMessageHelper;
        	
        	// Setting multipart as true for attachments to
            // be send
            mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(emailTemplate.getStrFrom());
           //mimeMessageHelper.setTo(emailTemplate.getStrTo());
            
            getToDataForMail(mimeMessageHelper, emailTemplate);            
			getBccDataForMail(mimeMessageHelper, emailTemplate);			
			getCcDataForMail(mimeMessageHelper, emailTemplate);
            
            //mimeMessageHelper.setText(emailTemplate.getStrText());
            mimeMessageHelper.setText("",emailTemplate.getStrText());
            mimeMessageHelper.setSubject(emailTemplate.getStrSubject());
 
            // Adding the attachment
            FileSystemResource file = new FileSystemResource(new File(emailTemplate.getStrPathToAttachment()));
 
            mimeMessageHelper.addAttachment(file.getFilename(), file);
 
            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }
        catch (Exception e) 
        {
        	e.printStackTrace();
		}
		return null;
	}

	@Override
	public String sendSimpleHtmlContentMessage(EmailTemplate emailTemplate) throws Exception
	{
		try 
		{
			JavaMailSender javaMailSender = getJavaMailSender(emailTemplate); //New Added for test
			
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			
			mimeMessageHelper.setFrom(emailTemplate.getStrFrom());
			
			getToDataForMail(mimeMessageHelper, emailTemplate);            
			getBccDataForMail(mimeMessageHelper, emailTemplate);			
			getCcDataForMail(mimeMessageHelper, emailTemplate);
            
            mimeMessageHelper.setText("",emailTemplate.getStrText());
            mimeMessageHelper.setSubject(emailTemplate.getStrSubject());
	            
            // Sending the mail
            javaMailSender.send(mimeMessage);
            
            return "Success: Mail sent Successfully.";   
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	MimeMessageHelper getToDataForMail(MimeMessageHelper mimeMessageHelper, EmailTemplate emailTemplate) 
	{
		try 
		{
			if(emailTemplate.getStrTo()!=null)
			{
				mimeMessageHelper.setTo(emailTemplate.getStrTo());
			}
            else if (emailTemplate.getStrToList()!=null && emailTemplate.getStrToList().size() > 0) 
            {
            	String[] strToArr = Arrays.copyOf(emailTemplate.getStrToList().toArray(), emailTemplate.getStrToList().size(), String[].class);
            	amsLogger.writeInfoLog("Inside performTxn PayloadReq::"+Arrays.toString(strToArr));
            	mimeMessageHelper.setTo(strToArr);
            }
            else
            {
            	amsLogger.writeInfoLog("----No TO Added For MAIL------");
            }
		}
		catch (Exception e) {
			//e.printStackTrace();
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return mimeMessageHelper;
	}
	
	MimeMessageHelper getCcDataForMail(MimeMessageHelper mimeMessageHelper, EmailTemplate emailTemplate) 
	{
		try 
		{
			if(emailTemplate.getStrCc()!=null) 
			{
				mimeMessageHelper.setCc(emailTemplate.getStrCc());
			}
			else if (emailTemplate.getStrCcList()!=null && emailTemplate.getStrCcList().size() > 0) 
			{
				String[] strCcArr = new String[emailTemplate.getStrCcList().size()];
				mimeMessageHelper.setCc(strCcArr);
			}
			else
			{
				amsLogger.writeInfoLog("----No CC Added For MAIL------");
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return mimeMessageHelper;
	}
	
	MimeMessageHelper getBccDataForMail(MimeMessageHelper mimeMessageHelper, EmailTemplate emailTemplate)
	{
		try 
		{
			if(emailTemplate.getStrBcc()!=null) 
			{
				mimeMessageHelper.setBcc(emailTemplate.getStrBcc());
			}
			else if (emailTemplate.getStrBccList()!=null && emailTemplate.getStrBccList().size() > 0)
			{
				String[] strBccArr = new String[emailTemplate.getStrBccList().size()];
				mimeMessageHelper.setBcc(strBccArr);
			}
			else
			{
				amsLogger.writeInfoLog("----No BCC Added For MAIL------");
			}
		}
		catch (Exception e) {
			//e.printStackTrace();
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return mimeMessageHelper;
	}
	
	//Added for testing purpose
	private JavaMailSender getJavaMailSender(EmailTemplate emailTemplate) 
	{
		return notificationHandler.getJavaMailSenderBasedOnParticipant(emailTemplate);
	}
}
