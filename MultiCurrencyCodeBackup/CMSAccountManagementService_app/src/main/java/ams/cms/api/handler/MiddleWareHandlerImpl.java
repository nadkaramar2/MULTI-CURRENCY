package ams.cms.api.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.MiddleWareBankRequestModel;
import ams.cms.api.service.MiddleWareBankRequestService;
import ams.cms.config.AppInfo;
import ams.cms.config.TransactionConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.BankDetailsInfo;
import ams.cms.model.PullAccountModel;
import ams.cms.services.BankDetailsInfoService;
import ams.cms.services.PullAccountService;
import ams.cms.utility.AccessBankDataObj;
import ams.cms.utility.BankInfoResponseModel;
import ams.cms.utility.MiddleWareBankResponse;
import ams.cms.utility.MiddleWareRequestModel;
import ams.cms.utility.MiddleWareTokenModel;
import ams.cms.utility.NameEnquiryAccessBankResponse;
import ams.cms.utility.NameEnquiryOtherBankResponse;
import ams.cms.utility.NameEnquiryRequestResponseModel;
import ams.cms.utility.OtherBankListResponse;
import ams.cms.utility.PoolAccountBalanceInfo;
import ams.cms.utility.Utils;

@Component
public class MiddleWareHandlerImpl implements MiddleWareHandler
{
	private AMSLogger amsLogger = AMSLogger.getInstance(MiddleWareHandlerImpl.class);
	
	@Autowired
	private Environment environment; 
	
	@Autowired
	private BankDetailsInfoService bankDetailsInfoService;
	
	@Autowired
	private MiddleWareBankRequestService middleWareBankRequestService;
	
	@Autowired
	private PullAccountService pullAccountService;
    
	@Autowired
	private AppInfo appInfo;
	
	@Override
	public List<BankInfoResponseModel> getBankNameList() 
	{
		try 
		{
			List<BankInfoResponseModel> getBankDetails = setBankDetails(false);
			return getBankDetails;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return null;
	}
	
	private List<BankInfoResponseModel> setBankDetails(boolean isAuthorizedErr) 
	{
		List<BankInfoResponseModel> responseList = null;
		try 
		{ 
			//add for participantID
			String participantId = appInfo.getStrParticipantId();
			/*
			String token = getTokenFromMiddleWareForBankDetailsApi(isAuthorizedErr);
			amsLogger.writeInfoLog("Token Response::"+token);
			*/
			
			String serverUrl = environment.getProperty("middleware_getbank_url");
			amsLogger.writeInfoLog("In Get Bank Detail from Middleware serverUrl=["+serverUrl+"]");
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			//headers.set("Authorization", "Bearer "+token);
			headers.set("loginid", "amsuser");
			headers.set("Authorization", "YW11c2Vyc2VjcmV0MjAyMw==");
			
			
			MiddleWareBankRequestModel middleWareBankRequestModel = new MiddleWareBankRequestModel();
			middleWareBankRequestModel.setParticipantId(participantId);
			
			middleWareBankRequestModel = bankDetailsInfoService.getRequestDataForBank(middleWareBankRequestModel); //Get middleware_app_info appId, auditId
			
			Map<String, Object> requestBody = new HashMap<>();
			requestBody.put("appId", middleWareBankRequestModel.getAppId());
			requestBody.put("auditId", middleWareBankRequestModel.getAuditId());
			    
			//HttpEntity<String> entity = new HttpEntity<>(headers);
			HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestBody, headers);
			
			RestTemplate restTemplate = new RestTemplate();			
			//String resp = restTemplate.postForObject(serverUrl, request, String.class);
			
			ResponseEntity<?> response = restTemplate.exchange(serverUrl, HttpMethod.POST, request, String.class);
			amsLogger.writeInfoLog("Response From bank Url...");
			
			String responseBody = (String) response.getBody();			
			amsLogger.writeInfoLog("Inside getBankDetails responseBody::"+responseBody);
			
			MiddleWareBankResponse bankInfoResponseModel = new ObjectMapper().readValue(responseBody, MiddleWareBankResponse.class);
			amsLogger.writeInfoLog("bankInfoResponseModel response::"+bankInfoResponseModel);
			
			
			//Need to check this code during test Start
			if (bankInfoResponseModel!=null)
			{
				if (bankInfoResponseModel!=null && bankInfoResponseModel.getData()!=null && bankInfoResponseModel.getData().size() > 0)
				{
					bankDetailsInfoService.truncateTable("bank_details_info");
					
					int[] resultArr = bankDetailsInfoService.batchEntryOfBankDetailsInfoFromMiddleWare(bankInfoResponseModel.getData()); 
					if (resultArr.length > 0) 
					{
						  amsLogger.writeInfoLog("total "+resultArr.length+" Data Added SuccessFully."); 
					}
				}				
			}	
			//Need to check this code during test End
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return responseList;
	}
	
	private BankDetailsInfo mapBankDetailsInfo(BankInfoResponseModel bankInfoResponseModel) 
	{
		BankDetailsInfo bankDetailsInfo = new BankDetailsInfo();
		bankDetailsInfo.setStrBankCategory(bankInfoResponseModel.getCategory());
		bankDetailsInfo.setStrBankCode(bankInfoResponseModel.getBankCode());
		bankDetailsInfo.setStrNipCode(bankInfoResponseModel.getCode());
		bankDetailsInfo.setStrBankName(bankInfoResponseModel.getName());
		bankDetailsInfo.setCreationDateTime(bankInfoResponseModel.getCreationDateTime());
		return bankDetailsInfo;
	}

	
	private BankInfoResponseModel mapBankDetailsResponseInfo(BankDetailsInfo bankDetailsInfo) 
	{
		BankInfoResponseModel bankInfoResponseModel = new BankInfoResponseModel();
		bankInfoResponseModel.setCategory(bankDetailsInfo.getStrBankCategory());
		bankInfoResponseModel.setBankCode(bankDetailsInfo.getStrBankCode());
		bankInfoResponseModel.setCode(bankDetailsInfo.getStrNipCode());
		bankInfoResponseModel.setName(bankDetailsInfo.getStrBankName());
		//bankInfoResponseModel.setCreationDateTime(bankDetailsInfo.getCreationDateTime());
		
		return bankInfoResponseModel;
	}

	public String getTokenFromMiddleWareForBankDetailsApi(boolean isAuthorizedErr) 
	{
		try 
		{
			MiddleWareTokenModel middleWareTokenModel = new MiddleWareTokenModel();
			middleWareTokenModel.setToken_id("A1111");
			middleWareTokenModel.setToken_active("Y");
			
			String tokenStr = bankDetailsInfoService.getMiddleWareToken(middleWareTokenModel);
			if (tokenStr !=null && tokenStr.length()>0 && !isAuthorizedErr) 
			{
				return tokenStr;
			}
			if(tokenStr !=null && tokenStr.length()>0) 
			{
				middleWareTokenModel.setToken_active("I");
				bankDetailsInfoService.updateBankRelatedMiddleWareToken(middleWareTokenModel);
			}
			
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			
			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
			
			map.add("clientId", "113334");    
			map.add("clientSecret","FXowry9746gd");
			

			HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

			//will be used for getting token
			
			/*
			 * ResponseEntity<?> response =
			 * restTemplate.exchange(environment.getProperty("middleware_api_authorization")
			 * , HttpMethod.POST, entity, String.class);
			 */
			
			middleWareTokenModel.setMiddleware_token("XYZ");
			ResponseEntity<?> response = new ResponseEntity<>(middleWareTokenModel,HttpStatus.OK);
			amsLogger.writeInfoLog("response get it...");
			
			//String resp = (String) response.getBody();
			
			//amsLogger.writeInfoLog("resp::"+resp);
			
			//middleWareTokenModel = new ObjectMapper().readValue(resp, MiddleWareTokenModel.class);
			amsLogger.writeInfoLog("middleWareTokenModel response::"+middleWareTokenModel);
			
			amsLogger.writeInfoLog("middleWareTokenModel access_token::"+middleWareTokenModel.getMiddleware_token());
			
			middleWareTokenModel.setToken_active("A");
			middleWareTokenModel.setToken_id("B001");
			bankDetailsInfoService.addBankRelatedMiddleWareToken(middleWareTokenModel);
			
			return middleWareTokenModel.getMiddleware_token();
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			e.printStackTrace();
		}
		return "";
	}

	@Override
	public TransactionConfig getNameEnquiryInfo(TransactionConfig transactionConfig) 
	{
		try 
		{
			amsLogger.writeInfoLog("Insied getNameEnquiryInfo transactionConfig::"+transactionConfig);
			transactionConfig.setCode("S0000");
			transactionConfig.setStatus("Success");			
			
			transactionConfig = getNameEnquiryAccountInfo(false, transactionConfig);			
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel = transactionConfig.getNameEnquiryRequestResponseModel();
				amsLogger.writeInfoLog("Insied getNameEnquiryInfo NameEnquiryRequestResponseModel::"+nameEnquiryRequestResponseModel);
				if (nameEnquiryRequestResponseModel!=null) 
				{
					amsLogger.writeInfoLog("Insied getNameEnquiryInfo NameEnquiryRequestResponseModel Code::["+nameEnquiryRequestResponseModel.getCode()+"]");
					if (nameEnquiryRequestResponseModel.getCode()!=null && nameEnquiryRequestResponseModel.getAccountName()!=null) 
					{
						transactionConfig.setCode("S0000");
						transactionConfig.setStatus("Success");
						transactionConfig.setMessage("Successfully Retrive account name.");
						transactionConfig.setAccountName(nameEnquiryRequestResponseModel.getAccountName());
					}
					else //if (nameEnquiryRequestResponseModel.getResponseCode() != null && ! "00".equalsIgnoreCase(nameEnquiryRequestResponseModel.getResponseCode())) 
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setStatus("Failed");
						transactionConfig.setMessage("Account Not Found.");
					}
					
					nameEnquiryRequestResponseModel.setMessage(transactionConfig.getMessage());
					//bankDetailsInfoService.updateNameEnquiryRequestResponse(nameEnquiryRequestResponseModel);
				}
				else 
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setStatus("Failed");
					transactionConfig.setMessage("Server Error during retrieving account name");
				}
			}
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setStatus("Failed");
			transactionConfig.setMessage("Exception getting during response");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	private TransactionConfig getNameEnquiryAccountInfo(boolean isAuthorizedErr, TransactionConfig transactionConfig) 
	{
		amsLogger.writeInfoLog("isAuthorizedErr=["+transactionConfig+"]");
		transactionConfig.setCode("S0000");
		
		MiddleWareRequestModel middleWareRequestModel = transactionConfig.getMiddleWareRequestModel();
		try 
		{
			
			/*
			String token = getTokenFromMiddleWareForBankDetailsApi(isAuthorizedErr);			
			amsLogger.writeInfoLog("Token Response::"+token);
			*/
			
			//String serverUrl = "https://apitest.nibss-plc.com.ng/nipservice/v1/nip/nameenquiry";
			
			String serverUrl = environment.getProperty("middleware_nameenquiry_url");
			amsLogger.writeInfoLog("In getNameEnquiryAccountInfo from Middleware serverUrl=["+serverUrl+"]");
			
			HttpHeaders headers = new HttpHeaders();			
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    //headers.set("Authorization", "Bearer "+token);
		    
		    headers.set("loginid", "amsuser");
			headers.set("Authorization", "YW11c2Vyc2VjcmV0MjAyMw==");
			
			amsLogger.writeInfoLog("In getNameEnquiryAccountInfo from Middleware headers=["+headers+"]");
			
			String txnType = middleWareRequestModel.getTxnType();
			txnType = txnType.trim();
			
			amsLogger.writeInfoLog("In getNameEnquiryAccountInfo from Middleware txnType=["+txnType+"]");
			
		    Map<String, Object> requestBody = new HashMap<>();
		    if ("TPO".equalsIgnoreCase(txnType)) 
		    {
		    	//getting PullAccountModel by participant Id 
				PullAccountModel pullAccountModelObj =  new PullAccountModel();
				pullAccountModelObj.setStrParticipantId(appInfo.getStrParticipantId());
				 
				//Getting pool Account info data
				PullAccountModel pullAccountModel = pullAccountService.getPullAccountModelByParticipantId(pullAccountModelObj);
				requestBody.put("senderaccountnumber", pullAccountModel.getStrPoolAccount());
		    }
		    else
		    {
		    	requestBody.put("senderaccountnumber", middleWareRequestModel.getSenderAccountNumber());
		    }
		    
		    requestBody.put("beneficiaryaccountnumber", middleWareRequestModel.getBeneficiaryAccountNumber());
		    
		    requestBody.put("beneficiarybankcode", middleWareRequestModel.getBeneficiaryBankCode());
		    requestBody.put("beneficiaryinstitutioncode", middleWareRequestModel.getBeneficiaryInstitutionCode());
		    requestBody.put("appid", middleWareRequestModel.getAppId());
		    
		    requestBody.put("auditid", getUpdatedAuditId(middleWareRequestModel.getMiddleWareAppInfoData()));
		    
		    requestBody.put("channelcode", middleWareRequestModel.getChannelCode());
		    requestBody.put("transaction_type", txnType);
		    
		    amsLogger.writeInfoLog("In getNameEnquiryAccountInfo from Middleware requestBody=["+requestBody+"]");
		    
		    HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestBody, headers);
		    
		    RestTemplate restTemplate = new RestTemplate();		    
		    //String resp = restTemplate.postForObject(serverUrl, request, String.class);			
		    //amsLogger.writeInfoLog("Inside getNameEnquiryAccountInfo response from MiddleWare::"+resp);
		    ResponseEntity<?> response = restTemplate.exchange(serverUrl, HttpMethod.POST, request, String.class);
			
			String responseBody = (String) response.getBody();			
			amsLogger.writeInfoLog("Inside getNameEnquiryAccountInfo responseBody::"+responseBody);
			
		    //map response
		   // NameEnquiryMiddleWareResponse nameEnquiryMiddleWareResponse  = new ObjectMapper().readValue(resp, NameEnquiryMiddleWareResponse.class);
			//amsLogger.writeInfoLog("nameEnquiryMiddleWareResponse response::"+nameEnquiryMiddleWareResponse);
			
		    amsLogger.writeInfoLog("In getNameEnquiryAccountInfo from Middleware transaction Type=["+txnType+"]");
			NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel = new NameEnquiryRequestResponseModel();
			if ("TPO".equalsIgnoreCase(txnType)) 
			{
				amsLogger.writeInfoLog("============ MAPPING TPO =================================");
				NameEnquiryOtherBankResponse nameEnquiryOtherBankResponse = new ObjectMapper().readValue(responseBody, NameEnquiryOtherBankResponse.class);
				amsLogger.writeInfoLog("Inside getNameEnquiryAccountInfo Mapped nameEnquiryOtherBankResponse::"+nameEnquiryOtherBankResponse);
				if (nameEnquiryOtherBankResponse!=null && nameEnquiryOtherBankResponse.getObj()!=null)
				{
					if (nameEnquiryOtherBankResponse.getObj().getData()!=null)
					{
						nameEnquiryRequestResponseModel.setAccountName(nameEnquiryOtherBankResponse.getObj().getData().getRecipientAccountName());
						nameEnquiryRequestResponseModel.setAccountNumber(nameEnquiryOtherBankResponse.getObj().getData().getRecipientAccountNumber());
						nameEnquiryRequestResponseModel.setBankVerificationNumber(nameEnquiryOtherBankResponse.getObj().getData().getRecipientBvn());
						nameEnquiryRequestResponseModel.setDestinationInstitutionCode(nameEnquiryOtherBankResponse.getObj().getData().getRecipientInstitutionCode());
					}
				}
			}
			else //For TPA 
			{
				amsLogger.writeInfoLog("----------------- MAPPING TPA ------------------");
				NameEnquiryAccessBankResponse nameEnquiryAccessBankResponse = new ObjectMapper().readValue(responseBody, NameEnquiryAccessBankResponse.class);
				amsLogger.writeInfoLog("Inside getNameEnquiryAccountInfo Mapped nameEnquiryAccessBankResponse::"+nameEnquiryAccessBankResponse);
				
				if (nameEnquiryAccessBankResponse!=null && nameEnquiryAccessBankResponse.getObj()!=null) 
				{
					/*if (nameEnquiryAccessBankResponse.getObj().getObj()!=null) 
					{
						nameEnquiryRequestResponseModel.setAccountName(nameEnquiryAccessBankResponse.getObj().getObj().getAccountName());
						nameEnquiryRequestResponseModel.setAccountNumber(nameEnquiryAccessBankResponse.getObj().getObj().getAccountNo());
						nameEnquiryRequestResponseModel.setBankVerificationNumber(nameEnquiryAccessBankResponse.getObj().getObj().getBVN());
					}
					*/
					if (nameEnquiryAccessBankResponse.getObj().getGetcustomeracctsdetailsresp()!=null && nameEnquiryAccessBankResponse.getObj().getGetcustomeracctsdetailsresp().size() > 0) 
					{
						AccessBankDataObj accessBankDataObj = nameEnquiryAccessBankResponse.getObj().getGetcustomeracctsdetailsresp().get(0);
						nameEnquiryRequestResponseModel.setAccountName(accessBankDataObj.getAccountName());
						nameEnquiryRequestResponseModel.setAccountNumber(accessBankDataObj.getAccountNo());
						nameEnquiryRequestResponseModel.setBankVerificationNumber(accessBankDataObj.getBVN());
					}
				}
			}		
			
		    nameEnquiryRequestResponseModel.setAccountName(nameEnquiryRequestResponseModel.getAccountName());
		    nameEnquiryRequestResponseModel.setAccountNumber(nameEnquiryRequestResponseModel.getAccountNumber());		    
		    nameEnquiryRequestResponseModel.setBankVerificationNumber(nameEnquiryRequestResponseModel.getBankVerificationNumber());
		    nameEnquiryRequestResponseModel.setDestinationInstitutionCode(nameEnquiryRequestResponseModel.getDestinationInstitutionCode());
		    
		    /*
		    if (nameEnquiryMiddleWareResponse!=null) 
		    {
		    	nameEnquiryRequestResponseModel.setResponseCode(nameEnquiryMiddleWareResponse.getRespCode());
		    	
		    	nameEnquiryRequestResponseModel.setKycLevel(nameEnquiryMiddleWareResponse.getObj().getKycLevel());
		    	nameEnquiryRequestResponseModel.setCurrencyCode(nameEnquiryMiddleWareResponse.getObj().getCurrencyCode());
		    	nameEnquiryRequestResponseModel.setCustomerNo(nameEnquiryMiddleWareResponse.getObj().getCustomerNo());
		    	nameEnquiryRequestResponseModel.setSessionID(nameEnquiryMiddleWareResponse.getObj().getSessionID());
		    }
		    */
		    
		    amsLogger.writeInfoLog("Inside getNameEnquiryAccountInfo nameEnquiryRequestResponseModel::"+nameEnquiryRequestResponseModel);
		    nameEnquiryRequestResponseModel.setChannelCode(middleWareRequestModel.getChannelCode());		    
			
		    if (nameEnquiryRequestResponseModel.getAccountName()!=null && nameEnquiryRequestResponseModel.getAccountName().trim().length() > 0) 
		    {
		    	nameEnquiryRequestResponseModel.setCode("S0000");
		    }
		    else 
		    {
		    	nameEnquiryRequestResponseModel.setCode("E0000");
		    }
		    transactionConfig.setNameEnquiryRequestResponseModel(nameEnquiryRequestResponseModel);
			
		    try 
		    {
		    	bankDetailsInfoService.addNameEnquiryRequestResponse(nameEnquiryRequestResponseModel);
		    }
		    catch (Exception e) {
		    	amsLogger.writeExceptionLog("Exception occured during addition::"+ExceptionUtils.getStackTrace(e));
			}
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal Server Error While getting data from MiddleWare");
			
			//getNameEnquiryAccountInfo(true, transactionConfig);
			
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}

	@Override
	public MiddleWareBankResponse getMiddleWareBankResponse() 
	{
		try 
		{
			String serverUrl = environment.getProperty("middleware_getbank_url");
			amsLogger.writeInfoLog("In getMiddleWareBankResponse Get Bank Middleware serverUrl=["+serverUrl+"]");
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);			
			headers.set("loginid", "amsuser");
			headers.set("Authorization", "YW11c2Vyc2VjcmV0MjAyMw==");
			
			amsLogger.writeInfoLog("In getMiddleWareBankResponse Middleware headers=["+headers+"]");
			
			MiddleWareBankRequestModel middleWareBankRequestModelIns = new MiddleWareBankRequestModel();
			middleWareBankRequestModelIns.setParticipantId(appInfo.getStrParticipantId());
			
			MiddleWareBankRequestModel middleWareBankRequestModel = bankDetailsInfoService.getRequestDataForBank(middleWareBankRequestModelIns); //Get middleware_app_info appId, auditId
			//amsLogger.writeInfoLog("In getMiddleWareBankResponse Middleware middleWareBankRequestModel="+middleWareBankRequestModel+"");
			
			Map<String, Object> requestBody = new HashMap<>();
			
			requestBody.put("appId", middleWareBankRequestModel.getAppId());
			requestBody.put("auditId", getUpdatedAuditId(middleWareBankRequestModel));
			
			amsLogger.writeInfoLog("In getMiddleWareBankResponse Middleware requestBody="+requestBody+"");
			    
			HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String, Object>>(requestBody, headers);
			
			RestTemplate restTemplate = new RestTemplate();			
			ResponseEntity<?> response = restTemplate.exchange(serverUrl, HttpMethod.POST, request, String.class);
			
			String responseBody = (String) response.getBody();			
			//amsLogger.writeInfoLog("Inside getBankDetails responseBody::"+responseBody);
			
			OtherBankListResponse otherBankListResponse = new ObjectMapper().readValue(responseBody, OtherBankListResponse.class);
			//amsLogger.writeInfoLog("Mapped otherBankListResponse::"+otherBankListResponse);			
			
			MiddleWareBankResponse middleWareBankResp = null;
			
			if (otherBankListResponse != null)
			{
				if (otherBankListResponse != null && otherBankListResponse.getObj() != null )
				{
					middleWareBankResp = otherBankListResponse.getObj();
					//amsLogger.writeInfoLog("In getMiddleWareBankResponse middleWareBankResp=["+middleWareBankResp+"]");
					if (middleWareBankResp != null && middleWareBankResp.getData() != null && middleWareBankResp.getData().size() > 0) 
					{
						try 
						{
							bankDetailsInfoService.truncateTable("bank_details_info");
							
							int[] resultArr = bankDetailsInfoService.batchEntryOfBankDetailsInfoFromMiddleWare(middleWareBankResp.getData()); 
							//amsLogger.writeInfoLog("In getMiddleWareBankResponse Middleware resultArr=["+resultArr+"]");
							if (resultArr != null && resultArr.length > 0) 
							{
								amsLogger.writeInfoLog("total "+resultArr.length+" Data Added SuccessFully."); 
							}
						}
						catch (Exception e) 
						{
							amsLogger.writeExceptionLog("Exception occured during bank details info entries::"+ExceptionUtils.getStackTrace(e));
						}
					}
					return middleWareBankResp;
				}
			}	
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	private String getUpdatedAuditId(MiddleWareBankRequestModel middleWareBankRequestModel) 
	{
		String resAuditId = "";
		try
		{
			amsLogger.writeInfoLog("Inside getUpdatedAuditId middleWareBankRequestModel=["+middleWareBankRequestModel+"]");
			String julianYear = Utils.getJulianYear();
			amsLogger.writeInfoLog("Inside getUpdatedAuditId julianYear=["+julianYear+"]");
			
			String julianDate = Utils.getJulianDate();
			amsLogger.writeInfoLog("Inside getUpdatedAuditId julianDate=["+julianDate+"]");
			
			String incrementedValue = Utils.getIncrementedValue(middleWareBankRequestModel.getStrLastAuditSerNo());
			
			amsLogger.writeInfoLog("Inside getUpdatedAuditId incrementedValue=["+incrementedValue+"]");
			
			resAuditId = middleWareBankRequestModel.getStrInitialValue() + julianYear + julianDate + incrementedValue;
			
			amsLogger.writeInfoLog("Inside getUpdatedAuditId resAuditId=["+resAuditId+"]");
			
			middleWareBankRequestModel.setAuditId(resAuditId);
			middleWareBankRequestModel.setStrYear(julianYear);
			middleWareBankRequestModel.setStrJulianDate(julianDate); 
			middleWareBankRequestModel.setStrLastAuditSerNo(incrementedValue);
			
			middleWareBankRequestService.updateMiddleWareAppInfo(middleWareBankRequestModel);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return resAuditId;
	}
	
	@Override
	public TransactionConfig getPullAccountInformation(TransactionConfig transactionConfig) 
	{
		try 
		{
			transactionConfig.setCode("S0000");
			transactionConfig.setStatus("Success");			
			
			transactionConfig = getPullAccountInfo(false, transactionConfig);			
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				PoolAccountBalanceInfo poolAccountBalanceInfo = transactionConfig.getPoolAccountBalanceInfo();
				if (poolAccountBalanceInfo!=null && "S0000".equalsIgnoreCase(poolAccountBalanceInfo.getCode())) 
				{
					if (poolAccountBalanceInfo.getAvailableBalance()!=null) 
					{
						transactionConfig.setCode("S0000");
						transactionConfig.setStatus("Success");
						transactionConfig.setMessage("Successfully Retrive Pool Account Balance.");
						transactionConfig.setPoolAccountBalanceInfo(poolAccountBalanceInfo);
					}
					else{
						transactionConfig.setCode("E0000");
						transactionConfig.setStatus("Failed");
						transactionConfig.setMessage("Account Not Found.");
					}
					
						}
				else 
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setStatus("Failed");
					transactionConfig.setMessage("Server Error during retrieving account name");
				}
			}
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setStatus("Failed");
			transactionConfig.setMessage("Exception getting during response");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	@SuppressWarnings("unused")
	private TransactionConfig getPullAccountInfo(boolean isAuthorizedErr, TransactionConfig transactionConfig) 
	{
		amsLogger.writeInfoLog("isAuthorizedErr=["+transactionConfig+"]");
		transactionConfig.setCode("S0000");
		
		MiddleWareRequestModel middleWareRequestModel = transactionConfig.getMiddleWareRequestModel();
		PullAccountModel pullAccountModel = transactionConfig.getPullAccountModel();
		try 
		{
			//String serverUrl = "https://apitest.nibss-plc.com.ng/nipservice/v1/nip/nameenquiry";
			
			String serverUrl = environment.getProperty("middleware_nameenquiry_url");
			amsLogger.writeInfoLog("In getNameEnquiryAccountInfo from Middleware serverUrl=["+serverUrl+"]");
			
			HttpHeaders headers = new HttpHeaders();			
		    headers.setContentType(MediaType.APPLICATION_JSON);
		    //headers.set("Authorization", "Bearer "+token);
		    
		    headers.set("loginid", "amsuser");
			headers.set("Authorization", "YW11c2Vyc2VjcmV0MjAyMw==");
			
			amsLogger.writeInfoLog("In getNameEnquiryAccountInfo from Middleware headers=["+headers+"]");
			
			String txnType = "TPA";
			txnType = txnType.trim();
			
			amsLogger.writeInfoLog("In getNameEnquiryAccountInfo from Middleware txnType=["+txnType+"]");
			
		    Map<String, Object> requestBody = new HashMap<>();
		    
		    
		    requestBody.put("senderaccountnumber", null);
		    requestBody.put("beneficiaryaccountnumber", pullAccountModel.getStrPoolAccount());//pull account no 
		    requestBody.put("beneficiarybankcode",null);
		    requestBody.put("beneficiaryinstitutioncode", null);
		    requestBody.put("appid", middleWareRequestModel.getAppId());
		    requestBody.put("auditid", getUpdatedAuditId(middleWareRequestModel.getMiddleWareAppInfoData()));
		    requestBody.put("channelcode", middleWareRequestModel.getChannelCode());
		    requestBody.put("transaction_type", txnType);
		    
		    amsLogger.writeInfoLog("In getPoolAccountInfo from Middleware requestBody=["+requestBody+"]");		    
		 
			HttpEntity<Map<String, Object>> request = new HttpEntity<Map<String,Object>>(requestBody, headers);
			  
			RestTemplate restTemplate = new RestTemplate(); ResponseEntity<?> response = restTemplate.exchange(serverUrl, HttpMethod.POST, request, String.class);
			  
			String responseBody = (String) response.getBody();
			  
			NameEnquiryAccessBankResponse poolAccountResponse = new ObjectMapper().readValue(responseBody, NameEnquiryAccessBankResponse.class);
			  
			PoolAccountBalanceInfo poolAccountBalanceInfo = new PoolAccountBalanceInfo();
			if(poolAccountResponse != null) 
			{
				poolAccountBalanceInfo.setCode("S0000");
				poolAccountBalanceInfo.setAvailableBalance(poolAccountResponse.getObj().getGetcustomeracctsdetailsresp().get(0).getAvailableBalance());
			}
			else 
			{
				poolAccountBalanceInfo.setCode("E0000");
				poolAccountBalanceInfo.setMessage("No Pool Account Found");
			}
			
			transactionConfig.setPoolAccountBalanceInfo(poolAccountBalanceInfo);
		}
		catch (Exception e) 
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal Server Error While getting data from MiddleWare");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
}
