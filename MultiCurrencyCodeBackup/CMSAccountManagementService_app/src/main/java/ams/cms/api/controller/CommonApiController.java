package ams.cms.api.controller;

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

import ams.cms.api.model.AccountBalanceResponse;
import ams.cms.api.model.AccountCreationResponse;
import ams.cms.api.model.AccountCreditBalanceTxnResponse;
import ams.cms.api.model.AccountCreditCardInterestResponse;
import ams.cms.api.model.AccountCreditCardTxnResponse;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.AccountStatementResponse;
import ams.cms.api.model.AccountStatementSortResponse;
import ams.cms.api.model.AccountTranscationResponse;
import ams.cms.api.model.AccountWiseInterestMasterResponse;
import ams.cms.api.model.CardAccountLinkageResponse;
import ams.cms.api.model.CumulativelimitResponse;
import ams.cms.api.model.DynamicQrGeneration;
import ams.cms.api.model.WalletBalanceResponse;
import ams.cms.api.service.QrCodeService;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.AccountTypeTierBasedLimit;
import ams.cms.model.CardAccountLinkage;
import ams.cms.model.WalletAccountMaster;
import ams.cms.services.AccountCreditCardTxnServices;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.ApproveUpgradeTierService;
import ams.cms.services.CardAccountLinkageService;
import ams.cms.services.WalletAccountMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.Utils;

@RestController
public class CommonApiController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(CommonApiController.class);
	
	@Autowired
	private	EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private	WalletAccountMasterService walletAccountMasterService;
	
	@Autowired
	private	AccountMasterService  accountMasterService;
	
	@Autowired
	private	AccountStatementService accountStatementService;
	
	@Autowired
	private	CardAccountLinkageService cardAccountLinkageService;
	
	@Autowired
	private	AccountCreditCardTxnServices accountCreditCardTxnServices;
	
	@Autowired
	private	AccountTypeMasterService  accountTypeMasterService;
	
	@Autowired
	private	QrCodeService qrCodeService;
		
	@Autowired
	private	ApproveUpgradeTierService approveUpgradeTierService;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;	
	
	@Autowired
	private	AppInfo appInfo;
		
	@RequestMapping(value="/walletbalanceshow",method = RequestMethod.POST) 
	public ResponseEntity<?> processToSaveWalletBalanceShowInfo(@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			WalletAccountMaster walletAccountMaster = new ObjectMapper().readValue(decrypt, WalletAccountMaster.class);
			
			processResponse = walletAccountMasterService.getWalletShowBalance(walletAccountMaster, processResponse);			
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value="/walletbalancelist",method=RequestMethod.POST) 
	public ResponseEntity<?> processToSaveWalletBalanceInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			WalletAccountMaster walletAccountMaster = new ObjectMapper().readValue(decrypt, WalletAccountMaster.class);
			
			List<WalletBalanceResponse> walletBalancelist = walletAccountMasterService.getWalletBalancelist(walletAccountMaster);			
			if (walletBalancelist!=null && walletBalancelist.size() > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetched Successfully.");
				processResponse.setWalletBalance(walletBalancelist);
			}
			else
			{  
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
				processResponse.setWalletBalance(walletBalancelist);
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	//For Account Profile creation Api
	@RequestMapping(value="/accountinfo",method=RequestMethod.POST)
	public ResponseEntity<?> processToSaveAccountInfo(@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
	    {
			  String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			  AccountCreation accountCreation = new ObjectMapper().readValue(decrypt, AccountCreation.class);
	        
			  List<AccountCreationResponse> accountCreationlist = accountMasterService.getAccountProfilelist(accountCreation);		  
			  if (accountCreationlist!=null && accountCreationlist.size() > 0) 
			  {
				  processResponse.setCode("S0000");
				  processResponse.setStatus("Success");
				  processResponse.setMessage("Data Fetched Successfully.");
				  processResponse.setAccountProfile(accountCreationlist);
			  }
			  else
			  {    
				  processResponse.setCode("E0000");
				  processResponse.setStatus("Failed");
				  processResponse.setMessage("Data Not Found.");
			  }
		}
	    catch (Exception e) 
	    {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
       return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	//For Account view Balance view 
	@RequestMapping(value="/accountbalanceview",method=RequestMethod.POST)
	public ResponseEntity<?> processToAccountBalanceView(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			  AccountCreation accountCreation = new ObjectMapper().readValue(decrypt, AccountCreation.class);
			  
			  List<AccountBalanceResponse> accountBalanceView = accountMasterService.getAccountBalanceView(accountCreation);			  
			  if (accountBalanceView != null && accountBalanceView.size() > 0) 
			  {
				  processResponse.setCode("S0000");
				  processResponse.setStatus("Success");
				  processResponse.setMessage("Data Fetched Successfully.");
				  processResponse.setAccountBalanceView(accountBalanceView);
			  }
			  else
			  {
				  processResponse.setCode("E0000");
				  processResponse.setStatus("Failed");
				  processResponse.setMessage("Data Not Found.");
			  }
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
		
	@RequestMapping(value="/accountstatement",method=RequestMethod.POST)
	public ResponseEntity<?> processToSaveAccountStatementInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			  AccountStatement accountStatement = new ObjectMapper().readValue(decrypt, AccountStatement.class);
			  
			  List<AccountStatementResponse> accountstatementlist = accountStatementService.getAccountStatementlist(accountStatement);
		      if (accountstatementlist!=null && accountstatementlist.size() > 0) 
		      {
		    	  processResponse.setCode("S0000");
				  processResponse.setStatus("Success");
		    	  processResponse.setMessage("Data Fetched Successfully.");
		    	  processResponse.setAccountstatementlist(accountstatementlist);
		    	
		      }
		      else
		      {
		    	processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
		      }
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//for last 5 record list account statement
	@RequestMapping(value="/accountstatementlist",method = RequestMethod.POST)
	public ResponseEntity<?> SortToAccountStatementlist(@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
		    String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
		    AccountStatement accountStatement = new ObjectMapper().readValue(decrypt, AccountStatement.class);
			     
	        List<AccountStatementSortResponse> accountstatementSortlist = accountStatementService.updateAccountStatementlist(accountStatement);
			
			if (accountstatementSortlist!=null && accountstatementSortlist.size() > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetched Successfully.");
				processResponse.setAccountstatementSortlist(accountstatementSortlist);
			}
			else
			{    
				processResponse.setCode("E0000");
			    processResponse.setStatus("Failed");
			    processResponse.setMessage("Data Not Found.");
			}
		} 
		catch (Exception e) 
		 {
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	    return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	//Added data Account Txn by Account Statement list 
	@RequestMapping(value="/accountTransactionview",method=RequestMethod.POST)
	public ResponseEntity<?> processToSaveAccountTranMaster(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			  AccountStatement accountStatement = new ObjectMapper().readValue(decrypt, AccountStatement.class);
			 
			  List<AccountTranscationResponse> accountTransaction = accountStatementService.getAccountTranscation(accountStatement);
			  if (accountTransaction!=null && accountTransaction.size() > 0) 
			  {
				  processResponse.setCode("S0000");
				  processResponse.setStatus("Success");
				  processResponse.setMessage("Data Fetched Successfully.");
				  processResponse.setAccountTransaction(accountTransaction);
			  }
			  else
			  {
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
			  }
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
    
	//Added data Account Txn by Account Statement last 5 txn
	@RequestMapping(value="/lastfivetxnlist",method=RequestMethod.POST)
	public ResponseEntity<?> processToSaveFiveAccountTxn(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			  AccountStatement accountStatement = new ObjectMapper().readValue(decrypt, AccountStatement.class);
			 
			  List<AccountTranscationResponse> accountTransaction = accountStatementService.getLastFiveAccountTxn(accountStatement);
			  if (accountTransaction!=null && accountTransaction.size() > 0) 
			  {
				  processResponse.setCode("S0000");
				  processResponse.setStatus("Success");
				  processResponse.setMessage("Data Fetched Successfully.");
				  processResponse.setAccountTransaction(accountTransaction);
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Data Not Found.");
				}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	@RequestMapping(value="/accountlinkedcardlist",method=RequestMethod.POST)
	public ResponseEntity<?> processToSaveLinkedCardInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			CardAccountLinkage cardAccountLinkage = new ObjectMapper().readValue(decrypt, CardAccountLinkage.class); 
			
			List<CardAccountLinkageResponse> cardlinkagelist = cardAccountLinkageService.getLinkagecardlist(cardAccountLinkage);
			  
			if (cardlinkagelist!=null && cardlinkagelist.size() > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
			    processResponse.setMessage("Data Fetched Successfully.");
				processResponse.setCardlinkagelist(cardlinkagelist);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("No Card Linked.");
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
	
	//for account credit card transaction for outstanding total balance 
	@RequestMapping(value="/totalOustandingBalance",method = RequestMethod.POST)
	public ResponseEntity<?> processToSaveCreditCradBalanceInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			AccountCreation accountCreation = new ObjectMapper().readValue(decrypt, AccountCreation.class); 
				
			List<AccountCreditCardTxnResponse> carditBalancelist=accountCreditCardTxnServices.getCreditCardBalancelist(accountCreation);
			
			if (carditBalancelist!=null && carditBalancelist.size() > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetched Successfully.");
				processResponse.setCarditBalancelist(carditBalancelist);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
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
	
	//for account credit card transaction for outstanding total balance list  
	@RequestMapping(value="/oustandingbalancelist",method=RequestMethod.POST)
	public ResponseEntity<?> processToSaveCreditCardInfo(@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			AccountCreditCardTransactionModel accountCreditCardTransactionModel = new ObjectMapper().readValue(decrypt, AccountCreditCardTransactionModel.class); 
			
			AccountTypeMaster accountTypeMster = new AccountTypeMaster();
			accountTypeMster.setStrAccountType(accountCreditCardTransactionModel.getStrAccountType());	
			
			accountTypeMster = accountTypeMasterService.getAccountTypeObject(accountTypeMster);
			
			/*
			List<AccountCreditBalanceTxnResponse> carditcardtxnlist = null;
			if ("Y".equalsIgnoreCase(accountTypeMster.getStrIsRevolvingCredit()))
			{
				carditcardtxnlist = revolvingCreditCardService.getRevolvingCreditCardlist(accountCreditCardTransactionModel);
			}
			else
			{
				carditcardtxnlist = accountCreditCardTxnServices.getCreditCardTxn(accountCreditCardTransactionModel);			 
			}
			*/
			List<AccountCreditBalanceTxnResponse> carditcardtxnlist = accountCreditCardTxnServices.getCreditCardTxn(accountCreditCardTransactionModel);
	
			if (carditcardtxnlist != null && carditcardtxnlist.size() > 0) 
			{
				processResponse.setMessage("Data Fetched Successfully.");
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setCarditcardtxnlist(carditcardtxnlist);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
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
	
	//for account credit card transaction for outstanding total Interest 
	@RequestMapping(value="/totalOutstandingInterest",method=RequestMethod.POST)
	public ResponseEntity<?> processToSaveCreditCradInterestInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			AccountCreation accountCreation = new ObjectMapper().readValue(decrypt, AccountCreation.class); 
			  
			List<AccountCreditCardInterestResponse> carditInterestlist=accountCreditCardTxnServices.getCreditCardInterestlist(accountCreation);			  
			if (carditInterestlist!=null && carditInterestlist.size() > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetched Successfully.");
				processResponse.setCarditInterestlist(carditInterestlist);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
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
	
	//for account credit card transaction for outstanding total interest list
	@RequestMapping(value="/outstandinginterestlist",method=RequestMethod.POST)
	public ResponseEntity<?> processToSaveCreditCardInterestInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
		  	ams.cms.scheduler.model.AccountWiseInterestMaster accountWiseInterestMaster = new ObjectMapper().readValue(decrypt, ams.cms.scheduler.model.AccountWiseInterestMaster.class); 
		  	
		  	AccountTypeMaster accountTypeMster = new AccountTypeMaster();
			accountTypeMster.setStrAccountType(accountWiseInterestMaster.getStrAccountType());			
			accountTypeMster = accountTypeMasterService.getAccountTypeObject(accountTypeMster);

			/*
		  	List<AccountWiseInterestMasterResponse> carditcardInterestlist=null;
		  	if ("Y".equalsIgnoreCase(accountTypeMster.getStrIsRevolvingCredit()))
			{
		  		carditcardInterestlist  = revolvingCreditCardService.getRevolvingCreditCardInterestlist(accountWiseInterestMaster);
			}
		  	else
		  	{
		  		carditcardInterestlist = accountCreditCardTxnServices.getCreditAccountWiseInterestList(accountWiseInterestMaster);			  
		  	}
		  	*/
			List<AccountWiseInterestMasterResponse> carditcardInterestlist = accountCreditCardTxnServices.getCreditAccountWiseInterestList(accountWiseInterestMaster);
			if (carditcardInterestlist!=null && carditcardInterestlist.size() > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetched Successfully.");
				processResponse.setCarditcardInterestlist(carditcardInterestlist);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
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
	
	//Wallet Percentage Edit Api by Jyoti S
	@RequestMapping(value="/walletpercentageupdate",method=RequestMethod.POST) 
	public ResponseEntity<?> processToUpdateWalletPercentage(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			WalletAccountMaster walletAccountMaster = new ObjectMapper().readValue(decrypt, WalletAccountMaster.class);
			
			int update = walletAccountMasterService.updateWalletPercentage(walletAccountMaster);	
			if (update > 0) 
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Updated Successfully.");
			}
			else
			{  
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Exception while updating data");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//Wallet Percentage Edit Api end
	
	//dyanmic Qr code [Start]
	@RequestMapping(value="/dyamicQrCodeImgUrl",method=RequestMethod.POST) 
	public ResponseEntity<?> getDynamicQrCodeImageUrl(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			AccountMaster accountMaster = new ObjectMapper().readValue(decrypt, AccountMaster.class);
			
			accountMaster.setStrAccountNumber(accountMaster.getStrAccountNumber());
			accountMaster.setStrCustId(accountMaster.getStrCustId());
			
			AccountMaster accountMasterResponse = accountMasterService.getAccountQRDetails(accountMaster);
			if(accountMasterResponse != null && accountMasterResponse.getStrQrCodeData() != null && accountMasterResponse.getStrQrCodeData().trim().length() > 0)
			{
				DynamicQrGeneration dynamicqrInfo = new DynamicQrGeneration();				
				dynamicqrInfo.setFromAccountNumber(accountMaster.getStrAccountNumber());
				dynamicqrInfo.setFromAccountType(accountMasterResponse.getStrAccountType());
				dynamicqrInfo.setAmount(accountMaster.getStrTxnAmount());
				dynamicqrInfo.setRefNo(Utils.getRefNo());
				dynamicqrInfo.setQrDate(Utils.getCurrentDate());
				dynamicqrInfo.setQrData(accountMasterResponse.getStrQrCodeData());
				dynamicqrInfo.setAccountMaster(accountMasterResponse);
				
				dynamicqrInfo = qrCodeService.getDynamicQrCodeImageUrl(dynamicqrInfo);	
				if(dynamicqrInfo != null && dynamicqrInfo.getStrQrCodeImageUrl() != null && dynamicqrInfo.getStrQrCodeImageUrl().trim().length() > 0)
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Dyanmic Qr Code Image successfully get.");
					processResponse.setDynamicQrCodeImgUrl(dynamicqrInfo.getStrQrCodeImageUrl());
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Exception while generating DynamicQrCode");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("QrCode not getting for this Account");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//dyanmic Qr code [End]
	
	//Added by Abhishek to update accountTransactionlimitAPi 14/04/2023-Start
	@RequestMapping(value = "/updateAccountTxnLimit", method = RequestMethod.POST)
	public ResponseEntity<?> processToUpdatetransactionlimit(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			AccountCreation accountCreation = new ObjectMapper().readValue(decrypt, AccountCreation.class);

			AccountCreation exitingTxnLimits = accountMasterService.getTransactionLimits(accountCreation);			
			if (accountCreation.getStrSingleTxnLimit() <= exitingTxnLimits.getStrSingleTxnLimit()) 
			{
				if (accountCreation.getStrDailyTxnLimit() <= exitingTxnLimits.getStrDailyTxnLimit())
				{
					if (Integer.parseInt(accountCreation.getStrMonthlyTxnLimit()) <= Integer.parseInt(exitingTxnLimits.getStrMonthlyTxnLimit())) 
					{
						int update = accountMasterService.updateTransactionLimit(accountCreation);
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

		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error ");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	//Added by Abhishek to update accountTransactionlimitAPi 14/04/2023-End
	
	//Added by SagarK to added accountTyeTierLimit APi 14/05/2023-Start
	@RequestMapping(value = "/tierAccountTxnLimitBasedOnType", method = RequestMethod.POST)
	public ResponseEntity<?> processTotierAccountTxnLimitBasedOnTypelist(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			AccountTypeTierBasedLimit accountTypeTierBasedLimit = new ObjectMapper().readValue(decrypt, AccountTypeTierBasedLimit.class);
			
			List<CumulativelimitResponse> cumulativeLimitlist = approveUpgradeTierService.getAccountTypeTierBasedLimit(accountTypeTierBasedLimit);
			if (cumulativeLimitlist != null && cumulativeLimitlist.size() > 0) 
			{
				processResponse.setMessage("Data Fetched Successfully.");
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setCumulativeLimitlist(cumulativeLimitlist);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Data Not Found.");
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
	
	
}


