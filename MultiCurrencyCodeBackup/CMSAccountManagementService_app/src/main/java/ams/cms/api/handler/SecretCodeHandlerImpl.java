package ams.cms.api.handler;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.SecretCodeRequest;
import ams.cms.api.model.SecretCodeResponse;
import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;

@Component
public class SecretCodeHandlerImpl implements SecretCodeHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(SecretCodeHandlerImpl.class);
	
	@Autowired
	private Environment environment;
	
	@Override
	public TransactionConfig validateSecretCode(TransactionConfig transactionConfig) 
	{
		return verifyKeyResponse(transactionConfig);
	}
	
	private TransactionConfig verifyKeyResponse(TransactionConfig transactionConfig) 
	{
		try 
		{
			SecretCodeRequest secretCodeRequest = transactionConfig.getSecretCodeRequest();
			
			String data = generateTOTP(secretCodeRequest);
			if (data != null) 
			{
				String serverUrl = environment.getProperty("montra_verify_key_url");
				amsLogger.writeInfoLog("In generateTOTP serverUrl=["+serverUrl+"]");
				
				RestTemplate restTemplate = new RestTemplate();
				
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				
				Map<String, Object> requestBody = new HashMap<>();
				requestBody.put("txnid", secretCodeRequest.getTxnid());
				requestBody.put("custid", secretCodeRequest.getCustid());
				requestBody.put("totp", data);
				
				HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestBody, headers);
				
				ResponseEntity<?> response = restTemplate.exchange(serverUrl, HttpMethod.POST, request, String.class);
				amsLogger.writeInfoLog("Response From generateTOTP Url...");
				
				String responseBody = (String) response.getBody();			
				amsLogger.writeInfoLog("Inside generateTOTP responseBody::"+responseBody);
				
				SecretCodeResponse secretCodeResponse = new ObjectMapper().readValue(responseBody, SecretCodeResponse.class);
				amsLogger.writeInfoLog("secretCodeResponse response::"+secretCodeResponse);
				
				if (secretCodeResponse.getSuccess()) 
				{
					transactionConfig.setCode("S0000");
					transactionConfig.setMessage(secretCodeResponse.getMessage());
				}
				else 
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage(secretCodeResponse.getMessage());
				}
			}
			else 
			{
				transactionConfig.setCode("S0000");
				transactionConfig.setMessage("Data not found!");
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	private String generateTOTP(SecretCodeRequest secretCodeRequest) 
	{
		String result = null;
		try 
		{
			String serverUrl = environment.getProperty("montra_generate_totp_url");
			amsLogger.writeInfoLog("In generateTOTP serverUrl=["+serverUrl+"]");
			
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//headers.set("Authorization", "Bearer "+token);			
			
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("txnid", secretCodeRequest.getTxnid());
			requestBody.put("custid", secretCodeRequest.getCustid());
			
			HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestBody, headers);

			ResponseEntity<?> response = restTemplate.exchange(serverUrl, HttpMethod.POST, request, String.class);
			amsLogger.writeInfoLog("Response From generateTOTP Url...");
			
			String responseBody = (String) response.getBody();			
			amsLogger.writeInfoLog("Inside generateTOTP responseBody::"+responseBody);
			
			SecretCodeResponse secretCodeResponse = new ObjectMapper().readValue(responseBody, SecretCodeResponse.class);
			amsLogger.writeInfoLog("secretCodeResponse response::"+secretCodeResponse);
			
			result = String.valueOf(secretCodeResponse.getData());
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("generateTOTP result::["+result+"]");
		return result;
	}
	
}
