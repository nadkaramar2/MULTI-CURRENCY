package ams.cms.notification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import ams.cms.messaging.SendEmail;

@Component
public class SmsHandlerImpl implements SmsHandler 
{
	@Autowired
	private Environment environment;
	
	@Autowired
	SendEmail sendEmailService;
	
	@Override
	public int processToSendSmSOTP(String otp, String mobileNo, String message) throws Exception 
	{
		try
		{
				StringBuilder dataBuilder = new StringBuilder("username=" + URLEncoder.encode("di78-AMS", "UTF-8"));
				dataBuilder.append("&password=" + URLEncoder.encode("digimile", "UTF-8"));
				dataBuilder.append("&type=0");
				dataBuilder.append("&dlr=1");
				dataBuilder.append("&destination=" + mobileNo);
				dataBuilder.append("&source=" + URLEncoder.encode("AMS", "UTF-8"));
				dataBuilder.append("&message=" + URLEncoder.encode(message, "UTF-8"));
				dataBuilder.append("&entityid=1101363910000016951");
				dataBuilder.append("&tempid=1107165285867522889");
				
				String smsUrl = "";				
				if (mobileNo.indexOf("+91")!=-1) 
				{
					smsUrl = environment.getProperty("ams.sms.local.url");
				}
				else
				{
					smsUrl = environment.getProperty("ams.sms.global.url");
				}
				System.out.println("smsUrl::["+smsUrl+"]");
				
				System.out.println("dataBuilder::"+dataBuilder);
				
				URL url = new URL(smsUrl+"?" + dataBuilder.toString());

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setUseCaches(false);
				conn.connect();

				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				StringBuffer buffer = new StringBuffer();
				while ((line = rd.readLine()) != null)
				{
					buffer.append(line).append("\n");
				}

				rd.close();
				conn.disconnect();
				
				//System.out.println("sendSms - sending Buffer" + buffer);
				
				if (buffer.length() > 4) 
				{
					String processingMsg = buffer.toString();					
					String[] respElement = processingMsg.split("\\|");

					if (null != respElement && respElement.length > 1) 
					{
					}
					return 1;
				} 
				else
				{
					return 0;
				}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}

	//added by sunil Y , to send sms to customer.
	@Override
	public int processToSendSmS(String mobileNo, String message) throws Exception {
		try
		{
				StringBuilder dataBuilder = new StringBuilder("username=" + URLEncoder.encode("di78-AMS", "UTF-8"));
				dataBuilder.append("&password=" + URLEncoder.encode("digimile", "UTF-8"));
				dataBuilder.append("&type=0");
				dataBuilder.append("&dlr=1");
				dataBuilder.append("&destination=" + mobileNo);
				dataBuilder.append("&source=" + URLEncoder.encode("AMS", "UTF-8"));
				dataBuilder.append("&message=" + URLEncoder.encode(message, "UTF-8"));
				dataBuilder.append("&entityid=1101363910000016951");
				dataBuilder.append("&tempid=1107165285867522889");
				
				String smsUrl = "";				
				if (mobileNo.indexOf("+91")!=-1) 
				{
					smsUrl = environment.getProperty("ams.sms.local.url");
				}
				else
				{
					smsUrl = environment.getProperty("ams.sms.global.url");
				}
				System.out.println("smsUrl::["+smsUrl+"]");
				
				System.out.println("dataBuilder::"+dataBuilder);
				
				URL url = new URL(smsUrl+"?" + dataBuilder.toString());

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setDoOutput(true);
				conn.setDoInput(true);
				conn.setUseCaches(false);
				conn.connect();

				BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				String line;
				StringBuffer buffer = new StringBuffer();
				while ((line = rd.readLine()) != null)
				{
					buffer.append(line).append("\n");
				}

				rd.close();
				conn.disconnect();
				
				//System.out.println("sendSms - sending Buffer" + buffer);
				
				if (buffer.length() > 4) 
				{
					String processingMsg = buffer.toString();					
					String[] respElement = processingMsg.split("\\|");

					if (null != respElement && respElement.length > 1) 
					{
					}
					return 1;
				} 
				else
				{
					return 0;
				}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
}
