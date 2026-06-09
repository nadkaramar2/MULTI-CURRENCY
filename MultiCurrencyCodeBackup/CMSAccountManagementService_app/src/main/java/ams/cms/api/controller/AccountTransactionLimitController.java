package ams.cms.api.controller;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountLimitResponse;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountTransactionLimit;
import ams.cms.api.model.AccountTxnLimitResponse;
import ams.cms.api.model.CreditAccountLimitResponse;
import ams.cms.api.model.CreditTxnLimitResponse;
import ams.cms.api.model.TxnLimitResponse;
import ams.cms.api.service.AccountMasterApiService;
import ams.cms.api.service.AccountTransactionLimitService;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.services.AccountMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

//created by ankit
@RestController
public class AccountTransactionLimitController
{	
	private AMSLogger amsLogger = AMSLogger.getInstance(CommonApiController.class);
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private AccountTransactionLimitService accountTransactionLimitService;
	
	@Autowired
	AccountMasterApiService accountMasterApiService;
	
	@Autowired
	AccountMasterService  accountMasterService;
		  
    @Autowired
    private ApiSecretKeyUtility apiSecretKeyUtility;
    
    @Autowired
	private	AppInfo appInfo;
	
	@RequestMapping(value = "/txnLimitDetails", method = RequestMethod.GET)
	public ResponseEntity<?> getCreditAndTransactionLimit(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountLimitRequest accountLimitRequest = new ObjectMapper().readValue(decrypt, AccountLimitRequest.class);
	
			AccountTransactionLimit limitDetails = accountTransactionLimitService.getLimitDetails(accountLimitRequest);
			TxnLimitResponse txnLimitResponse = mapAccountTransactionLimitToTxnLimitResponse(limitDetails);
	
			if (txnLimitResponse.getStrSingleTxnLimit() == null || txnLimitResponse.getStrMonthlyTxnLimit() == null) 
			{
				List<TxnLimitResponse> txnLimitResponseAsList = Arrays.asList(txnLimitResponse);
				processResponse.setTxnLimitResponse(txnLimitResponseAsList);
				
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Transaction Credit Limit Details");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			} 
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Please Check Details");
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
			}
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error....");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	private TxnLimitResponse mapAccountTransactionLimitToTxnLimitResponse(AccountTransactionLimit limitDetails) 
	{
		TxnLimitResponse txnLimitResponse = new TxnLimitResponse();
		txnLimitResponse.setStrDailyTxnLimit(limitDetails.getStrDailyTxnLimit());
		txnLimitResponse.setStrMonthlyTxnLimit(limitDetails.getStrMonthlyTxnLimit());
		txnLimitResponse.setStrSingleTxnLimit(limitDetails.getStrSingleTxnLimit());
		txnLimitResponse.setStrYearlyTxnLimit(limitDetails.getStrYearlyTxnLimit());
		return txnLimitResponse;
	}	
	
	//created by ankit
	@RequestMapping(value = "/txnCreditLimit", method = RequestMethod.POST)
	//public ResponseEntity<?> getPrepaidTransactionLimit(@RequestBody AccountLimitRequest accountLimitRequest) {
	public ResponseEntity<?> getPrepaidTransactionLimit(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		String message = null;
		try 
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountLimitRequest accountLimitRequest = new ObjectMapper().readValue(decrypt, AccountLimitRequest.class);
			
			//Add by Abhishek on 18-04-2023-Start
			AccountMaster accontMaster = new AccountMaster();
			accontMaster.setStrAccountNumber(accountLimitRequest.getStrAccountNumber());
		    accontMaster = accountMasterApiService.getUpdateOrAvailableCreditlimits(accontMaster);
		    if (accontMaster!=null && accontMaster.getStrID()!=null && accontMaster.getStrID().length()>0)
			{
		    	CreditTxnLimitResponse creditTxnLimitResponse = mapDataToCreditTxnLimitResponse(accontMaster);			
		    	List<CreditTxnLimitResponse> creditTxnLimitResponseASList = Arrays.asList(creditTxnLimitResponse);
			
				if (creditTxnLimitResponseASList!=null && creditTxnLimitResponseASList.size() > 0) 
				{
					processResponse.setAccountCreditResponse(creditTxnLimitResponseASList);
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Successfully Fetched Transaction Limit Details");
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Could Not Fetch Limit, Please Try Again!");
				}
			}	
		    else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Could Not Fetch Limit, Please Try Again!");
			}
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//created By ankit
	
	private CreditTxnLimitResponse mapDataToCreditTxnLimitResponse(AccountMaster accontMaster) 
	{
		CreditTxnLimitResponse creditTxnLimitResponse = new CreditTxnLimitResponse();
		
		//Default i.e. account type based updates limits
		creditTxnLimitResponse.setStrSingleTxnMaxLimit(accontMaster.getStrSingleTxnMaxLimit());
		creditTxnLimitResponse.setStrPerDayMaxAssignedlimit(accontMaster.getStrPerDayMaxAssignedlimit());
    	creditTxnLimitResponse.setStrMonthlyMaxAssignedLimit(accontMaster.getStrMonthlyMaxAssignedLimit());
		creditTxnLimitResponse.setStrYearlyMaxAssignedLimit(accontMaster.getStrYearlyMaxAssignedLimit());
		creditTxnLimitResponse.setStrCreditMaxAssignedLimit(accontMaster.getStrCreditMaxAssignedLimit());
		
		//User based updates limits
		creditTxnLimitResponse.setStrSingleTxnLimit(String.valueOf(accontMaster.getStrSingleTxnLimit()));
		creditTxnLimitResponse.setStrPerDayAssignLimit(accontMaster.getStrPerDayAssignLimit());
		creditTxnLimitResponse.setStrMonthlyAssignLimit(accontMaster.getStrMonthlyAssignLimit());
		creditTxnLimitResponse.setStrYearlyAssignLimit(accontMaster.getStrYearlyAssignLimit());
		creditTxnLimitResponse.setStrCreditMaxAssignedLimit(accontMaster.getStrCreditMaxAssignedLimit());
		creditTxnLimitResponse.setStrCreditLimitAmount(accontMaster.getStrCreditLimitAmount());
				
		//Txn based updates limits
		creditTxnLimitResponse.setStrPerDayAvailableLimit(accontMaster.getStrAvailableDailyLimit());
		creditTxnLimitResponse.setStrMonthlyAvailableLimit(accontMaster.getStrAvailableMonthlyLimit());
		creditTxnLimitResponse.setStrYearlyAvailableLimit(accontMaster.getStrAvailableYearlyLimit());
		creditTxnLimitResponse.setStrAvailableCreditLimit(accontMaster.getStrAvailableCreditLimit());
		
		return creditTxnLimitResponse;
	}
	//Add by Abhishek on 18-04-2023-End
	
	//SUPPORT METHOD FOR txnPrepaidlimit CONTROLLER //created by ankit
	private CreditAccountLimitResponse mapDataToCreditAccountLimitResponse(AccountMaster accountMaster, AccountTransactionLimit accountTransactionLimit){
		String strAvailableMonthlyLimit2 = accountMaster.getStrAvailableMonthlyLimit();
		String strAvailableDailyLimit2 = accountMaster.getStrAvailableDailyLimit();
		String strAvailableCreditLimit = accountMaster.getStrCreditLimitAmount();
		String strDailyTxnLimit2 = accountTransactionLimit.getStrDailyTxnLimit();
		String strMonthlyTxnLimit2 = accountTransactionLimit.getStrMonthlyTxnLimit();
		CreditAccountLimitResponse creditAccountLimitResponse = new CreditAccountLimitResponse();
		creditAccountLimitResponse.setStrTxnLimit(strAvailableCreditLimit);
		creditAccountLimitResponse.setStrPerDayAssignedLimit(strDailyTxnLimit2);
		creditAccountLimitResponse.setStrPerDayAvailableLimit(strAvailableDailyLimit2);
		creditAccountLimitResponse.setStrMonthlyAssignedLimit(strMonthlyTxnLimit2);
		creditAccountLimitResponse.setStrMonthlyAvailableLimit(strAvailableMonthlyLimit2);
		return creditAccountLimitResponse;
	}
	//created by ankit
	
	@RequestMapping(value = "/txnPrepaidLimit", method = RequestMethod.POST)
	public ResponseEntity<?> getCreditTransactionLimit(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountLimitRequest accountLimitRequest = new ObjectMapper().readValue(decrypt, AccountLimitRequest.class);
			
			AccountMaster accontMaster = new AccountMaster();
			accontMaster.setStrAccountNumber(accountLimitRequest.getStrAccountNumber());
			
			accontMaster = accountMasterApiService.getUpdateOrAvailableTxnlimits(accontMaster);			
			if (accontMaster!=null && accontMaster.getStrID()!=null && accontMaster.getStrID().length()>0)
			{
				AccountTxnLimitResponse accountTxnLimitResponse = mapDataToAccountTxnLimitResponse(accontMaster);
				List<AccountTxnLimitResponse> accountLimitResponseAsList = Arrays.asList(accountTxnLimitResponse);
				
				if (accountLimitResponseAsList!=null && accountLimitResponseAsList.size() > 0) 
				{
					processResponse.setAccountLimitResponse(accountLimitResponseAsList);
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Successfully Fetched Transaction Limit Details");
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Could Not Fetch Limit, Please Try Again!!");
				}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Could Not Fetch Limit, Please Try Again!");
			}
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		 return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//created By ankit
	
	
	//SUPPORT METHOD FOR txnPrepaidlimit CONTROLLER //created by ankit
	private AccountLimitResponse mapDataToAccountLimitResponse(AccountMaster accountMaster, AccountTransactionLimit accountTransactionLimit){
		
		String strAvailableMonthlyLimit2 = accountMaster.getStrAvailableMonthlyLimit();
		String strAvailableDailyLimit2 = accountMaster.getStrAvailableDailyLimit();
		String strSingleTxnLimit2 = accountTransactionLimit.getStrSingleTxnLimit();
		String strDailyTxnLimit2 = accountTransactionLimit.getStrDailyTxnLimit();
		String strMonthlyTxnLimit2 = accountTransactionLimit.getStrMonthlyTxnLimit();
		
		AccountLimitResponse accountLimitResponse = new AccountLimitResponse();
		accountLimitResponse.setStrSingleTxnLimit(strSingleTxnLimit2);
		accountLimitResponse.setStrPerDayAssignedLimit(strDailyTxnLimit2);
		accountLimitResponse.setStrPerDayAvailableLimit(strAvailableDailyLimit2);
		accountLimitResponse.setStrMonthlyAssignedLimit(strMonthlyTxnLimit2);
		accountLimitResponse.setStrMonthlyAvailableLimit(strAvailableMonthlyLimit2);

		return accountLimitResponse;
	}
	//created by ankit
	//Added By Abhishek T Start
	private AccountTxnLimitResponse mapDataToAccountTxnLimitResponse(AccountMaster accontMaster) 
	{
		AccountTxnLimitResponse accountTxnLimitResponse = new AccountTxnLimitResponse();
		
		//Default i.e. account type based updates limits
		accountTxnLimitResponse.setStrSingleTxnMaxLimit(accontMaster.getStrSingleTxnMaxLimit());
		accountTxnLimitResponse.setStrPerDayMaxAssignedlimit(accontMaster.getStrPerDayMaxAssignedlimit());
		accountTxnLimitResponse.setStrMonthlyMaxAssignedLimit(accontMaster.getStrMonthlyMaxAssignedLimit());
		accountTxnLimitResponse.setStrYearlyMaxAssignedLimit(accontMaster.getStrYearlyMaxAssignedLimit());
		
		//User based updates limits
		accountTxnLimitResponse.setStrSingleTxnLimit(String.valueOf(accontMaster.getStrSingleTxnLimit()));
		accountTxnLimitResponse.setStrPerDayAssignLimit(accontMaster.getStrPerDayAssignLimit());
		accountTxnLimitResponse.setStrMonthlyAssignLimit(accontMaster.getStrMonthlyAssignLimit());
		accountTxnLimitResponse.setStrYearlyAssignLimit(accontMaster.getStrYearlyAssignLimit());
		
		//Txn based updates limits
		accountTxnLimitResponse.setStrPerDayAvailableLimit(accontMaster.getStrAvailableDailyLimit());
		accountTxnLimitResponse.setStrMonthlyAvailableLimit(accontMaster.getStrAvailableMonthlyLimit());
		accountTxnLimitResponse.setStrYearlyAvailableLimit(accontMaster.getStrAvailableYearlyLimit());
		
		return accountTxnLimitResponse;
	}
	//Added By Abhishek T End
	//Add by Abhishek T for updateCreditLimitAccount on 18/04/2023 - Start
	@RequestMapping(value = "/updateCreditLimitAccount", method = RequestMethod.POST)
	public ResponseEntity<?> processToUpdatecreditlimitaccount(HttpServletRequest request,@RequestBody PayloadReqRes req) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{ 
			ProcessResponse processResps = apiSecretKeyUtility.validateApiKey(request,processResponse); 
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps); 

			if ("E0000".equalsIgnoreCase(processResps.getCode())) 
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req,processResps)); 
			}
			
			String participantId = appInfo.getStrParticipantId();
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountMaster accountMaster = new ObjectMapper().readValue(decrypt,AccountMaster.class);			
			
			AccountMaster exitingCreditlimitAmount = accountMasterApiService.getCreditLimitAmount(accountMaster);
			
			Long existingCreditLimit = 0l;
			if (exitingCreditlimitAmount!=null && exitingCreditlimitAmount.getStrCreditLimitAmount()!=null)
			{
				String strExistingCreditLimit = exitingCreditlimitAmount.getStrCreditLimitAmount().trim();
				existingCreditLimit = Long.parseLong(strExistingCreditLimit);
				
				Long userCreditLimt = 0l;
				if (accountMaster!=null && accountMaster.getStrCreditLimitAmount()!=null)
				{
					userCreditLimt = Long.parseLong(accountMaster.getStrCreditLimitAmount().trim());
				}
				
				if(userCreditLimt <= existingCreditLimit)
				{
					AccountCreation accountCreation = new AccountCreation();
					accountCreation.setStrAccountNumber(accountMaster.getStrAccountNumber());
					accountCreation.setStrCustId(accountMaster.getStrCustId());
					
					AccountCreation exitingTxnLimits = accountMasterService.getTransactionLimits(accountCreation);			
					if (accountMaster.getStrPerTxnLimit() <= exitingTxnLimits.getStrSingleTxnLimit()) 
					{
						if (accountMaster.getStrDailyTxnLimit() <= exitingTxnLimits.getStrDailyTxnLimit())
						{
							if (Integer.parseInt(accountMaster.getStrMonthlyTxnLimit()) <= Integer.parseInt(exitingTxnLimits.getStrMonthlyTxnLimit())) 
							{
								int update = accountMasterApiService.updatecreditLimitAmount(accountMaster);
								if (update > 0)
								{
									processResponse.setCode("S0000");
									processResponse.setStatus("Success");
									processResponse.setMessage("Limits Successfully Updated.");
								} 
								else
								{
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage("Exception occured during limit updation.");
								}
							}
							else 
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Please enter a valid monthly amount.");
							}
						}
						else
					    {
					    	processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Please enter a valid Daily amount.");//
						}
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Please enter a valid Per Txn amount.");//
					}
				}				  
				else 
				{	
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Please enter a valid Credit amount.");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Please enter a valid Account Number.");
			}
		}				
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error ");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
    }
   //Add by Abhishek T for updateCreditLimitAccount on 18/04/2023 - End
}
