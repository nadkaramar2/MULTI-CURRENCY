package ams.cms.notification;

public interface SmsHandler 
{
	int processToSendSmSOTP(String otp,String mobileNo, String message) throws Exception;
	
	int processToSendSmS(String mobileNo, String message) throws Exception;
}
