package ams.cms.txn.handler;

import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.services.BankDetailsInfoService;
import ams.cms.utility.BankInfoResponseModel;
import ams.cms.utility.ExternalServerRequestResponseModel;
import ams.cms.utility.ExternalServerTokenModel;
import ams.cms.utility.Utils;

@Component("ExternalServerHandler")
public class ExternalServerHandlerImpl implements ExternalServerHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(ExternalServerHandlerImpl.class);
	
	private int serverCallCount = 0;
	private int serverCalledCnt = 0;
	
	@Autowired
	private BankDetailsInfoService bankDetailsInfoService;
	
	@Override
	public void addBankDetailsFromNIBSS(String data) 
	{
		try 
		{
			setBankDetailsFromNIBSS(false);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	@Override
	public TransactionConfig getBeneficiaryInfo(TransactionConfig transactionConfig) 
	{
		try 
		{
			transactionConfig.setCode("S0000");
			
			//Added for Handling server not response issue START
			serverCallCount = 4;
			//Added for Handling server not response issue END
			
			transactionConfig = getBeneficiaryAccountInfo(false, transactionConfig);
			
			ExternalServerRequestResponseModel externalServerRequestResponseModel = transactionConfig.getExternalServerRequestResponseModel();
			
			if (externalServerRequestResponseModel!=null) 
			{
				if (externalServerRequestResponseModel.getCode()!=null) 
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage(externalServerRequestResponseModel.getMessage());
				}
				else if (externalServerRequestResponseModel.getResponseCode()!=null && !"00".equalsIgnoreCase(externalServerRequestResponseModel.getResponseCode())) 
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Account Not Found.");
				}
				
				externalServerRequestResponseModel.setMessage(transactionConfig.getMessage());
				bankDetailsInfoService.updaNameEnquiryReqRes(externalServerRequestResponseModel);
			}
			else 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Error Getting Response.");
			}
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Exception getting during response");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	private TransactionConfig getBeneficiaryAccountInfo(boolean isAuthorizedErr, TransactionConfig transactionConfig) 
	{
		amsLogger.writeInfoLog("Instance level serverCallCount=["+serverCallCount+"]");
		amsLogger.writeInfoLog("serverCalledCnt=["+serverCalledCnt+"]");
		transactionConfig.setCode("S0000");
		
		ExternalServerRequestResponseModel externalServerRequestResponseModel =	transactionConfig.getExternalServerRequestResponseModel();
		externalServerRequestResponseModel.setChannelCode("1");
		externalServerRequestResponseModel.setTransactionId(Utils.getNibssTxnId());
		try 
		{
			bankDetailsInfoService.addNameEnquiryReqRes(externalServerRequestResponseModel);
			
			String token = getTokenFromExternalServerApi(isAuthorizedErr);
			amsLogger.writeInfoLog("Token Response::"+token);
			
			String serverUrl = "https://apitest.nibss-plc.com.ng/nipservice/v1/nip/nameenquiry";
			
			HttpHeaders headers = new HttpHeaders();			
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.set("Authorization", "Bearer "+token);
		    
		    Map<String, Object> requestBody = new HashMap<>();
		    requestBody.put("accountNumber", externalServerRequestResponseModel.getAccountNumber());
		    requestBody.put("channelCode", externalServerRequestResponseModel.getChannelCode());
		    requestBody.put("destinationInstitutionCode", externalServerRequestResponseModel.getDestinationInstitutionCode());
		    requestBody.put("transactionId", externalServerRequestResponseModel.getTransactionId());
		    
		    HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestBody, headers);
		    
		    RestTemplate restTemplate = new RestTemplate();
		  
		    String resp = restTemplate.postForObject(serverUrl, request, String.class);
			
		    amsLogger.writeInfoLog("resp::"+resp);
			
		    externalServerRequestResponseModel = new ObjectMapper().readValue(resp, ExternalServerRequestResponseModel.class);
		    
			amsLogger.writeInfoLog("externalServerRequestResponseModel response::"+externalServerRequestResponseModel);
			
			if (externalServerRequestResponseModel!=null && externalServerRequestResponseModel.getMessage()!=null && "Unauthorized".equalsIgnoreCase(externalServerRequestResponseModel.getMessage()))
			{
				serverCalledCnt++;
				if (serverCallCount != serverCalledCnt) 
				{
					getBeneficiaryAccountInfo(true, transactionConfig);
				}
			}
			if (externalServerRequestResponseModel!=null)
			{
				externalServerRequestResponseModel.setChannelCode(externalServerRequestResponseModel.getChannelCode());
				externalServerRequestResponseModel.setDestinationInstitutionCode(externalServerRequestResponseModel.getDestinationInstitutionCode());
				externalServerRequestResponseModel.setTransactionId(externalServerRequestResponseModel.getTransactionId());
			}
			transactionConfig.setExternalServerRequestResponseModel(externalServerRequestResponseModel);
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal Server Error While getting data from NIBSS");
			
			serverCalledCnt++;
			amsLogger.writeInfoLog("Inside Catch serverCalledCnt=["+serverCalledCnt+"]");
			if (serverCallCount != serverCalledCnt) 
			{
				bankDetailsInfoService.updaNameEnquiryReqRes(externalServerRequestResponseModel);
				getBeneficiaryAccountInfo(true, transactionConfig);
			}
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	private void setBankDetailsFromNIBSS(boolean isAuthorizedErr) 
	{
		try 
		{
			String token = getTokenFromExternalServerForBankDetailsApi(isAuthorizedErr);
			amsLogger.writeInfoLog("Token Response::"+token);
			
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "Bearer "+token);

			HttpEntity<String> entity = new HttpEntity<>(headers);

			ResponseEntity<?> response = restTemplate.exchange("https://apitest.nibss-plc.com.ng/nibsspayplus/v2/Banks", HttpMethod.GET, entity, String.class);
			amsLogger.writeInfoLog("response get it...");
			
			String resp = (String) response.getBody();
			
			amsLogger.writeInfoLog("resp::"+resp);
			
			BankInfoResponseModel bankInfoResponseModel = new ObjectMapper().readValue(resp, BankInfoResponseModel.class);
			amsLogger.writeInfoLog("bankInfoResponseModel response::"+bankInfoResponseModel);
			
			if ("Unauthorized".equalsIgnoreCase(bankInfoResponseModel.getMessage()))
			{
				setBankDetailsFromNIBSS(true);
			}
			
			if (bankInfoResponseModel!=null && bankInfoResponseModel.getData()!=null 
					&& bankInfoResponseModel.getData().getBanks()!=null && bankInfoResponseModel.getData().getBanks().size() > 0)
			{
				bankDetailsInfoService.truncateTable("bank_details_info");
				
				int[] resultArr = bankDetailsInfoService.batchEntryOfBankDetailsInfoFromNIBSSS(bankInfoResponseModel.getData().getBanks()); if (resultArr.length > 0) 
				{
					  amsLogger.writeInfoLog("total "+resultArr.length+" Data Added SuccessFully."); 
				}
			}			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private String getTokenFromExternalServerForBankDetailsApi(boolean isAuthorizedErr) 
	{
		try 
		{
			ExternalServerTokenModel externalServerTokenModel = new ExternalServerTokenModel();
			externalServerTokenModel.setToken_id("B001");
			String tokenStr = bankDetailsInfoService.getAccessToken(externalServerTokenModel);
			if (tokenStr !=null && tokenStr.length()>0 && !isAuthorizedErr) 
			{
				return tokenStr;
			}
			if(tokenStr !=null && tokenStr.length()>0) 
			{
				externalServerTokenModel.setToken_active("I");
				bankDetailsInfoService.updateankRelatedToken(externalServerTokenModel);
			}
			
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id","2aee5ee6-c8d7-4da2-ad17-001d6d5902ff");
			map.add("client_secret","Je.8Q~RPHNZ8xhFgEnzg42ip_y2O..fcqxlcFaVu");
			map.add("grant_type","client_credentials");
			map.add("scope","2aee5ee6-c8d7-4da2-ad17-001d6d5902ff/.default");

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

			ResponseEntity<?> response = restTemplate.exchange("https://apitest.nibss-plc.com.ng/reset",
			                          HttpMethod.POST,
			                          entity,
			                          String.class);
			amsLogger.writeInfoLog("response get it...");
			
			String resp = (String) response.getBody();
			
			amsLogger.writeInfoLog("resp::"+resp);
			
			externalServerTokenModel = new ObjectMapper().readValue(resp, ExternalServerTokenModel.class);
			amsLogger.writeInfoLog("externalServerTokenModel response::"+externalServerTokenModel);
			
			amsLogger.writeInfoLog("externalServerTokenModel access_token::"+externalServerTokenModel.getAccess_token());
			
			externalServerTokenModel.setToken_active("A");
			externalServerTokenModel.setToken_id("B001");
			bankDetailsInfoService.addBankRelatedToken(externalServerTokenModel);
			
			return externalServerTokenModel.getAccess_token();
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return "";
	}
	
	private String getTokenFromExternalServerApi(boolean isAuthorizedErr) 
	{
		try 
		{
			ExternalServerTokenModel externalServerTokenModel = new ExternalServerTokenModel();
			externalServerTokenModel.setToken_id("N001");
			
			String tokenStr = bankDetailsInfoService.getAccessToken(externalServerTokenModel);
			if (tokenStr !=null && tokenStr.length()>0 && !isAuthorizedErr) 
			{
				return tokenStr;
			}
			if(tokenStr !=null && tokenStr.length()>0) 
			{
				externalServerTokenModel.setToken_active("I");
				bankDetailsInfoService.updateankRelatedToken(externalServerTokenModel);
			}
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			map.add("client_id","0b54e219-9681-47cf-bcfb-bf72315157d9");
			map.add("client_secret","uzY8Q~l1l54gZWpK4ShaqoGc.3H4TE0.deKbacZF");
			map.add("grant_type","client_credentials");
			map.add("scope","0b54e219-9681-47cf-bcfb-bf72315157d9/.default");
			
			externalServerTokenModel = getTokenFromNibssServer(map);
			
			externalServerTokenModel.setToken_active("A");
			externalServerTokenModel.setToken_id("N001");
			bankDetailsInfoService.addBankRelatedToken(externalServerTokenModel);
			
			return externalServerTokenModel.getAccess_token();
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return "";
	}
	
	private ExternalServerTokenModel getTokenFromNibssServer(MultiValueMap<String, String> map) 
	{
		ExternalServerTokenModel externalServerTokenModel = new ExternalServerTokenModel();
		try 
		{
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

			ResponseEntity<?> response = restTemplate.exchange("https://apitest.nibss-plc.com.ng/reset",
			                          HttpMethod.POST,
			                          entity,
			                          String.class);
			amsLogger.writeInfoLog("response get it...");
			
			String resp = (String) response.getBody();
			
			amsLogger.writeInfoLog("resp::"+resp);
			
			externalServerTokenModel = new ObjectMapper().readValue(resp, ExternalServerTokenModel.class);
			amsLogger.writeInfoLog("externalServerTokenModel response::"+externalServerTokenModel);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return externalServerTokenModel;
	}

	//Send SMS to Nigeria USers [START]
	
	@Override
	public TransactionConfig sendOtpSms(TransactionConfig transactionConfig) 
	{
		try
		{
			String serverUrl = "https://ecsmontra-1176861782.eu-west-1.elb.amazonaws.com/sendsms/api/v1/sms/sendsms";
			
			HttpHeaders headers = new HttpHeaders();			
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    headers.set("montracode", "MNXMXT2022");
		    
		    Map<String, Object> requestData = new HashMap<>();
		    requestData.put("id", "123");
		    
		    requestData.put("to", transactionConfig.getToNumbersListforSms());		    
		    requestData.put("body", transactionConfig.getSmsMessage());
		    
		    requestData.put("sender_mask", "Artha");
		    requestData.put("expiry", "5");
		    requestData.put("priority", "high");
		    
		    HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestData, headers);
		    
		     //Added for SSL certificate disable Start
		    TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
		    SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
		    SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
		    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
		    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		    requestFactory.setHttpClient(httpClient);

		    RestTemplate restTemplate = new RestTemplate(requestFactory);
		    //Added for SSL certificate disable End
		  
		    String resp = restTemplate.postForObject(serverUrl, request, String.class);
		    amsLogger.writeInfoLog("sendOtpSms resp::["+resp+"]");
		    
		    ExternalServerRequestResponseModel externalServerRequestResponseModel = new ObjectMapper().readValue(resp, ExternalServerRequestResponseModel.class);
		    amsLogger.writeInfoLog("sendOtpSms externalServerRequestResponseModel::["+externalServerRequestResponseModel+"]");
		    if (externalServerRequestResponseModel!=null && externalServerRequestResponseModel.isSuccess()) 
		    {
		    	transactionConfig.setCode("S0000");
		    	transactionConfig.setMessage("Successfully Sent SMS.");
		    }
		    else
		    {
		    	transactionConfig.setCode("E0000");
		    	transactionConfig.setMessage("Issue Occuring While Sending SMS.");
		    }
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Issue while sending sms to user");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
		
	//Send SMS to Nigeria USers [END]
	
}
