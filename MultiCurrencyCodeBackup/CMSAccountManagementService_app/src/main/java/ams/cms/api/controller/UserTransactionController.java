package ams.cms.api.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.api.handler.UserTransactionHandler;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.DenominationMaster;
import ams.cms.api.model.UserTransactionModel;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.MontraAccountMaster;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.services.MontraAccountMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.txn.handler.TransactionValidator;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;
import ams.cms.utility.Utils;

@RestController
//@RequestMapping("/userTxn")
public class UserTransactionController
{
	private AMSLogger amsLogger = AMSLogger.getInstance(UserTransactionController.class);
	
	@Autowired
	private UserTransactionHandler userTransactionHandler;
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private CustomerIDCreationService customerIDCreationService;
	
	@Autowired
	private TransactionIdCreationConfigDao transactionIdCreationConfigDao;
	
	@Autowired
	private ExternalTransactionHandler externalTransactionHandler;
	
	@Autowired
	private TierAccountMasterService tierAccountMasterService;
	
	@Autowired
	private TransactionValidator transactionValidator;
	
	@Autowired
	private MontraAccountMasterService montraAccountMasterService;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;

	@Autowired
	private AppInfo appInfo;
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@RequestMapping(value="/userTxn/checkUserAccount", method = RequestMethod.POST)
	public ResponseEntity<?> checkUserAccount(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			amsLogger.writeInfoLog("Inside checkUserAccount decrypt::["+decrypt+"]");
			UserTransactionModel userTxnRequest = new ObjectMapper().readValue(decrypt, UserTransactionModel.class);
			
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setTxnAmount(userTxnRequest.getUserTxnAmount());
			transactionConfig.setUserTxnReq(userTxnRequest);
			transactionConfig.setCode("S0000");
			
			transactionConfig = userTransactionHandler.validatedTransaction(transactionConfig);
			if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
				if(userTxnRequest.getUserTxnAmount() != null && userTxnRequest.getUserTxnAmount().trim().length() > 0)
				{
					if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
					{
						AccountMaster accountMaster = new AccountMaster();
						accountMaster.setStrAccountNumber(userTxnRequest.getUserAccountNumber());
						transactionConfig.setAccountMasterFromAccount(accountMaster);
					}
					userTransactionHandler.validateTxnAmount(transactionConfig);
					if("S0000".equalsIgnoreCase(transactionConfig.getCode()) && "WDL".equalsIgnoreCase(transactionConfig.getUserTxnReq().getTranType()))
					{
						transactionConfig = userTransactionHandler.checkUserAccountTypeCategory(transactionConfig);
						if("S0000".equalsIgnoreCase(transactionConfig.getCode()) )
						{
							userTransactionHandler.getAccountHolderName(transactionConfig);
							if (transactionConfig.getAccountHolderName() != null && transactionConfig.getAccountHolderName().trim().length() > 0)
							{
								processResponse.setAccountHolderName(transactionConfig.getAccountHolderName());
							}
						}
					}
				}
				else 
				{
					transactionConfig = userTransactionHandler.checkUserAccountTypeCategory(transactionConfig); //Check User(Prepaid Customer) during deposit from Agent
					if("S0000".equalsIgnoreCase(transactionConfig.getCode()) )
					{
						userTransactionHandler.getAccountHolderName(transactionConfig);
						if (transactionConfig.getAccountHolderName()!=null && transactionConfig.getAccountHolderName().trim().length() > 0)
						{
							processResponse.setAccountHolderName(transactionConfig.getAccountHolderName());
						}
					}
				}
			}
			
			if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
				processResponse.setStatus("Success");
				processResponse.setMessage("Data Fetched Successfully.");
				userTxnRequest = transactionConfig.getUserTxnReq();
				
				processResponse.setAccount_Type(userTxnRequest.getUserAccountType());
				processResponse.setAccountNumber(userTxnRequest.getUserAccountNumber());
				processResponse.setCust_id(userTxnRequest.getUserCustId());
			}
			else 
			{
				processResponse.setStatus("Failed");
			}
			processResponse.setCode(transactionConfig.getCode());
			processResponse.setMessage(transactionConfig.getMessage());
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
	
	@RequestMapping(value="/userTxn/sendUserOTP", method = RequestMethod.POST)
	public ResponseEntity<?> sendUserOTP(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			amsLogger.writeInfoLog("Inside sendUserOTP decrypt::["+decrypt+"]");
			UserTransactionModel userTxnRequest = new ObjectMapper().readValue(decrypt, UserTransactionModel.class);
			
			TransactionConfig transactionConfig = new TransactionConfig();			
			transactionConfig.setUserTxnReq(userTxnRequest);
			transactionConfig.setCode("S0000");
			
			transactionConfig = userTransactionHandler.verifyUserPIN(transactionConfig);
			if("S0000".equalsIgnoreCase(transactionConfig.getCode()) )
			{
				transactionConfig = userTransactionHandler.validatedTransaction(transactionConfig);
				if("S0000".equalsIgnoreCase(transactionConfig.getCode()) )
				{
					transactionConfig = userTransactionHandler.sendUserOTP(transactionConfig);
					if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
					{
						processResponse.setStatus("Success");
						transactionConfig.setMessage("OTP Sent on your registered mobile number.");
					}
				}
			}
			
			if(!"S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
				processResponse.setStatus("Failed");
			}
			
			processResponse.setCode(transactionConfig.getCode());
			processResponse.setMessage(transactionConfig.getMessage());
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
	
	@RequestMapping(value="/userTxn/resendUserOTP", method = RequestMethod.POST)
	public ResponseEntity<?> resendUserOTP(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			amsLogger.writeInfoLog("Inside resendUserOTP decrypt::["+decrypt+"]");
			UserTransactionModel userTxnRequest = new ObjectMapper().readValue(decrypt, UserTransactionModel.class);
			
			TransactionConfig transactionConfig = new TransactionConfig();			
			transactionConfig.setUserTxnReq(userTxnRequest);
			transactionConfig.setCode("S0000");
			
			CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(userTxnRequest.getUserCustId());
			//customerIdCreation.setStrParticipantID(participantId); 
			
			customerIdCreation = customerIDCreationService.getCustomerIdInfo(customerIdCreation);			
			if (customerIdCreation!=null && customerIdCreation.getStrMobileNo()!=null && customerIdCreation.getStrMobileNo().trim().length() > 0) 
			{
				transactionConfig.getUserTxnReq().setStrMobileNo(customerIdCreation.getStrMobileNo());
				transactionConfig = userTransactionHandler.sendUserOTP(transactionConfig);
				if ("S0000".equalsIgnoreCase(transactionConfig.getCode()))
				{
					transactionConfig.setMessage("OTP Sent on your registered mobile number.");
					processResponse.setStatus("Success");
				}
			}
			else 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage("Issue to retrieving customer mobile no for otp.");
			}
			
			if (!"S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
				processResponse.setStatus("Failed");
			}
			processResponse.setMessage(transactionConfig.getMessage());
			processResponse.setCode(transactionConfig.getCode());
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
	
	@RequestMapping(value="/userTxn/validateDenomination", method = RequestMethod.POST)
	public ResponseEntity<?> validateDenominationAmount(@RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			DenominationMaster denominationReq = new ObjectMapper().readValue(decrypt, DenominationMaster.class);
			
			TransactionConfig transactionConfig = new TransactionConfig();
			transactionConfig.setDenominationMaster(denominationReq);
			transactionConfig.setCode("S0000");
			
			transactionConfig = userTransactionHandler.validateDenominationNotes(transactionConfig);
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Successfully Matched.");
			}
			else
			{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage(transactionConfig.getMessage());
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
	
	//Added by Pankaj Pawar Start	
	@RequestMapping(value="/userTxn/PerformTransaction", method = RequestMethod.POST)
	public ResponseEntity<?> PerformUserTransaction(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			amsLogger.writeInfoLog("Inside PerformUserTransaction decrypt::["+decrypt+"]");
			UserTransactionModel userTxnRequest = new ObjectMapper().readValue(decrypt, UserTransactionModel.class);

			TransactionConfig transactionConfig = new TransactionConfig();			
			transactionConfig.setUserTxnReq(userTxnRequest);
			transactionConfig.setCode("S0000");
			transactionConfig.setTxnAmount(transactionConfig.getUserTxnReq().getUserTxnAmount());
			
			transactionConfig = userTransactionHandler.getAccountInformation(transactionConfig);//Also validate account HERE
			if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
				transactionConfig = userTransactionHandler.validateTxnAmount(transactionConfig);
				//Added for Validating to account cumulative balance limit by Pankaj [start] 
				if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
				{
					AccountCreation toAccountCreation = new AccountCreation();
					toAccountCreation.setStrAccountNumber(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
					toAccountCreation.setStrClosingBalance(transactionConfig.getAccountMasterToAccount().getStrClosingBalance());
					toAccountCreation.setStrPreCredAmount(transactionConfig.getAccountMasterToAccount().getStrPreCredAmount());
					
					TransactionConfig toTransactionConfig = new TransactionConfig();
					toTransactionConfig.setAccountCreation(toAccountCreation);
					toTransactionConfig.setTxnAmount(transactionConfig.getTxnAmount());
					toTransactionConfig.setCode("S0000");
					
					toTransactionConfig = transactionValidator.validateCummulativeBalanceLimit(toTransactionConfig); //Validate to Account cumulative balance
					if(!"S0000".equalsIgnoreCase(toTransactionConfig.getCode()))
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage(toTransactionConfig.getMessage());									 
					}
				}
				//Added for Validating to account cumulative balance limit by Pankaj [end]
				if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
				{
					if ("WDL".equalsIgnoreCase(transactionConfig.getUserTxnReq().getTranType())) 
					{
						transactionConfig.getUserTxnReq().setUserCustId(transactionConfig.getAccountMasterFromAccount().getStrCustId());
					}
					else
					{
						transactionConfig.getUserTxnReq().setUserCustId(transactionConfig.getAccountMasterToAccount().getStrCustId());
					}
					
					transactionConfig = userTransactionHandler.validateOtp(transactionConfig);
					if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
					{
						String tranId = transactionIdCreationConfigDao.getTransactionId();
						transactionConfig.setTxnId(tranId);
						
						//Added By Pankaj Start
						if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
						{
							transactionConfig = externalTransactionHandler.updateLimitValues(transactionConfig);
							tierAccountMasterService.updateCummAvailableBalance(transactionConfig.getTierAccountMaster());
						}
						//Added By Pankaj End
						//Added by Sunny Soni for saving denomination Records Start
						transactionConfig = updateValues(transactionConfig);
						if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
						{
							userTransactionHandler.saveDenominationMasterData(transactionConfig);
							SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM YYYY");
							SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd MMM YYYY hh:mm a");
							
							StringBuilder bodyMsg = new StringBuilder("Dear ");
							StringBuilder responseMsg = new StringBuilder("");
							if ("WDL".equalsIgnoreCase(transactionConfig.getUserTxnReq().getTranType())) 
							{
								bodyMsg.append(transactionConfig.getUserTxnReq().getStrAccountHolderName());
								bodyMsg.append(",");
								bodyMsg.append("<br/><br/>"); 
								bodyMsg.append("Your account has been successfully debited Rs."+transactionConfig.getUserTxnReq().getUserTxnAmount()+" at Agent Outlet ");
								bodyMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountType());
								bodyMsg.append(" ");
								bodyMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
								bodyMsg.append(" ");
								bodyMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountHolderName());
								bodyMsg.append(" on "+simpleDateFormat2.format(new Date())+" at "+Utils.getFormattedCurrentTime());
								
								transactionConfig.setStrEmail(transactionConfig.getAccountMasterFromAccount().getStrEmailID());
								
								responseMsg.append("Debited successfully amount Rs. ");
								responseMsg.append(transactionConfig.getUserTxnReq().getUserTxnAmount());
								responseMsg.append(" Withdrawn by Customer bearing account number ");
								responseMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountType());
								responseMsg.append(" ");
								responseMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
							}
							else 
							{
								bodyMsg.append(transactionConfig.getUserTxnReq().getStrToAccountHolderName());
								bodyMsg.append(",");
								bodyMsg.append("<br/><br/>"); 
								bodyMsg.append("Your account has been successfully deposited Rs."+transactionConfig.getUserTxnReq().getUserTxnAmount()+" at Agent Outlet ");
								bodyMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountType());
								bodyMsg.append(" ");
								bodyMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
								bodyMsg.append(" ");
								bodyMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountHolderName());
								bodyMsg.append(" on "+simpleDateFormat2.format(new Date())+" at "+Utils.getFormattedCurrentTime());
								
								transactionConfig.setStrEmail(transactionConfig.getAccountMasterToAccount().getStrEmailID());
								
								responseMsg.append("Credited successfully amount Rs. ");
								responseMsg.append(transactionConfig.getUserTxnReq().getUserTxnAmount());
								responseMsg.append(" Deposited by Customer bearing account number ");
								responseMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountType());
								responseMsg.append(" ");
								responseMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
							}
							bodyMsg.append( "<br/><br/><br/>");
							bodyMsg.append( "Powered by AMS Technologies Pvt Ltd");
							transactionConfig.setMailMessage(bodyMsg.toString());
							userTransactionHandler.sendEmail(transactionConfig);
							
							transactionConfig.setMessage(responseMsg.toString());
							processResponse.setTransactionId(tranId);
							processResponse.setTransactionDateTime(simpleDateFormat3.format(new Date()));
							processResponse.setFromAccount(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
							processResponse.setToAccount(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
							processResponse.setCurrentAvailableBalance(transactionConfig.getAccountMasterFromAccount().getStrClosingBalance());
							processResponse.setStatus("Success");
						}
						//Added by Sunny Soni for saving denomination Records End
					}
				}
			}
			if (!"S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
				processResponse.setStatus("Failed");
			}
			processResponse.setMessage(transactionConfig.getMessage());
			processResponse.setCode(transactionConfig.getCode());
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
	
	//Agent cash withdrawal or deposit replica changes Start
	@RequestMapping(value="/NGN/userTxn/PerformAgentTxn", method = RequestMethod.POST)
	public ResponseEntity<?> PerformAgentTransaction(HttpServletRequest request, @RequestBody PayloadReqRes req)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			ProcessResponse	processResps = apiSecretKeyUtility.validateRequestAPI(request, req);
			amsLogger.writeInfoLog("Inside processToGetBasicInformation processResp2::"+processResps);
			if ("E0000".equalsIgnoreCase(processResps.getCode()))
			{
				return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResps));
			}
			String decrypt = appInfo.getDecryptedPayload();
			
			String participantId = appInfo.getStrParticipantId();
			
			amsLogger.writeInfoLog("Inside PerformUserTransaction decrypt::["+decrypt+"]");
			UserTransactionModel userTxnRequest = new ObjectMapper().readValue(decrypt, UserTransactionModel.class);
			
			String cid = userTxnRequest.getCid();
			cid = (cid != null && cid.trim().length() > 0) ? cid.trim() : "";
			
			String strCustId = userTxnRequest.getStrCustId();
			strCustId = (strCustId != null && strCustId.trim().length() > 0) ? strCustId.trim() : "";
			
			String montraTxnId = userTxnRequest.getMontraTxnId();
			montraTxnId = (montraTxnId != null && montraTxnId.trim().length() > 0) ? montraTxnId.trim() : "";
			
			String fromAccountNo = userTxnRequest.getFromAccountNo();
			fromAccountNo = (fromAccountNo != null && fromAccountNo.trim().length() > 0) ? fromAccountNo.trim() : "";
			
			String toAccountNo = userTxnRequest.getToAccountNo();
			toAccountNo = (toAccountNo != null && toAccountNo.trim().length() > 0) ? toAccountNo.trim() : "";
			userTxnRequest.setToAccountNumber(toAccountNo);
			
			String strTran_type = userTxnRequest.getStrTran_type();
			strTran_type = (strTran_type != null && strTran_type.trim().length() > 0) ? strTran_type.trim() : "";
			userTxnRequest.setTranType(strTran_type);
			
			String userTxnAmount = userTxnRequest.getUserTxnAmount();
			userTxnAmount = (userTxnAmount != null && userTxnAmount.trim().length() > 0) ? userTxnAmount.trim() : "";
			
			String secretCode = userTxnRequest.getSecretCode();
			secretCode = (secretCode != null && secretCode.trim().length() > 0) ? secretCode.trim() : "";

			TransactionConfig transactionConfig = new TransactionConfig();	
			transactionConfig.setCode("S0000");
			transactionConfig.setUserTxnReq(userTxnRequest);
			transactionConfig.setTxnAmount(userTxnAmount);
			
			MontraAccountMaster montraAccountMaster = new MontraAccountMaster();
			montraAccountMaster.setCid(cid);
			montraAccountMaster.setStrCustId(strCustId);
			
			processResponse.setStrCustId(strCustId);
			processResponse.setCid(cid);
			processResponse.setMontraTxnId(montraTxnId);		
			
			ProcessResponse processresp = montraAccountMasterService.validateMontraIdAndCustId(montraAccountMaster);
			if (processresp != null && !"S0000".equalsIgnoreCase(processresp.getCode())) 
			{
				transactionConfig.setCode("E0000");
				transactionConfig.setMessage(processresp.getMessage());
			}
			
			if("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				if (montraTxnId != null && montraTxnId.trim().length() > 0) 
				{
					AccountTranMaster accountTranMasterInst = new AccountTranMaster(); 
					accountTranMasterInst.setStrSrcTxnId(montraTxnId); //Added montra Txn Id
					
					AccountTranMaster accmTransMst = accountTranMasterService.getAccountTranMaster(accountTranMasterInst);	
					amsLogger.writeInfoLog("--->>> Inside performTxn accmTransMst::["+accmTransMst+"]");
					if (accmTransMst == null) 
					{
						transactionConfig = userTransactionHandler.validateSecretCode(transactionConfig);//<---- Validating Secret Code
						if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
						{
							transactionConfig = userTransactionHandler.getAccountInformation(transactionConfig);//Also validate account HERE
							if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
							{
								transactionConfig = userTransactionHandler.validateTxnAmount(transactionConfig);
								//Added for Validating to account cumulative balance limit by Pankaj [start] 
								if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
								{
									AccountCreation toAccountCreation = new AccountCreation();
									toAccountCreation.setStrAccountNumber(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
									toAccountCreation.setStrClosingBalance(transactionConfig.getAccountMasterToAccount().getStrClosingBalance());
									toAccountCreation.setStrPreCredAmount(transactionConfig.getAccountMasterToAccount().getStrPreCredAmount());
									
									TransactionConfig toTransactionConfig = new TransactionConfig();
									toTransactionConfig.setAccountCreation(toAccountCreation);
									toTransactionConfig.setTxnAmount(transactionConfig.getTxnAmount());
									toTransactionConfig.setCode("S0000");
									
									toTransactionConfig = transactionValidator.validateCummulativeBalanceLimit(toTransactionConfig); //Validate to Account cumulative balance
									if(!"S0000".equalsIgnoreCase(toTransactionConfig.getCode()))
									{
										transactionConfig.setCode("E0000");
										transactionConfig.setMessage(toTransactionConfig.getMessage());									 
									}
								}
								//Added for Validating to account cumulative balance limit by Pankaj [end]
								if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
								{
									if ("WDL".equalsIgnoreCase(transactionConfig.getUserTxnReq().getTranType())) 
									{
										transactionConfig.getUserTxnReq().setUserCustId(transactionConfig.getAccountMasterFromAccount().getStrCustId());
									}
									else
									{
										transactionConfig.getUserTxnReq().setUserCustId(transactionConfig.getAccountMasterToAccount().getStrCustId());
									}
									
									//transactionConfig = userTransactionHandler.validateSecretCode(transactionConfig);//<---- Validating Secret Code
									if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
									{
										String tranId = transactionIdCreationConfigDao.getTransactionId();
										transactionConfig.setTxnId(tranId);
										
										//Added By Pankaj Start
										if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
										{
											transactionConfig = externalTransactionHandler.updateLimitValues(transactionConfig);
											tierAccountMasterService.updateCummAvailableBalance(transactionConfig.getTierAccountMaster());
										}
										//Added By Pankaj End
										//Added by Sunny Soni for saving denomination Records Start
										transactionConfig.setMontraTxnId(montraTxnId);//Added montra Txn Id
										
										transactionConfig = updateValues(transactionConfig);
										if("S0000".equalsIgnoreCase(transactionConfig.getCode()))
										{
											DenominationMaster denominationMaster = transactionConfig.getUserTxnReq().getDenominationData();
											if (denominationMaster!=null)
											{
												transactionConfig.setParticipantId(participantId);
												userTransactionHandler.saveDenominationMasterData(transactionConfig);
											}
											SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd MMM YYYY");
											//SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("dd MMM YYYY hh:mm a");
											
											StringBuilder bodyMsg = new StringBuilder("Dear ");
											StringBuilder responseMsg = new StringBuilder("");
											if ("WDL".equalsIgnoreCase(transactionConfig.getUserTxnReq().getTranType())) 
											{
												bodyMsg.append(transactionConfig.getUserTxnReq().getStrAccountHolderName());
												bodyMsg.append(",");
												bodyMsg.append("<br/><br/>"); 
												bodyMsg.append("Your account has been successfully debited Rs."+userTxnAmount+" at Agent Outlet ");
												bodyMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountType());
												bodyMsg.append(" ");
												bodyMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
												bodyMsg.append(" ");
												bodyMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountHolderName());
												bodyMsg.append(" on "+simpleDateFormat2.format(new Date())+" at "+Utils.getFormattedCurrentTime());
												
												transactionConfig.setStrEmail(transactionConfig.getAccountMasterFromAccount().getStrEmailID());
												
												responseMsg.append("Debited successfully amount Rs. ");
												responseMsg.append(transactionConfig.getUserTxnReq().getUserTxnAmount());
												responseMsg.append(" Withdrawn by Customer bearing account number ");
												responseMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountType());
												responseMsg.append(" ");
												responseMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
											}
											else 
											{
												bodyMsg.append(transactionConfig.getUserTxnReq().getStrToAccountHolderName());
												bodyMsg.append(",");
												bodyMsg.append("<br/><br/>"); 
												bodyMsg.append("Your account has been successfully deposited Rs."+transactionConfig.getUserTxnReq().getUserTxnAmount()+" at Agent Outlet ");
												bodyMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountType());
												bodyMsg.append(" ");
												bodyMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
												bodyMsg.append(" ");
												bodyMsg.append(transactionConfig.getAccountMasterFromAccount().getStrAccountHolderName());
												bodyMsg.append(" on "+simpleDateFormat2.format(new Date())+" at "+Utils.getFormattedCurrentTime());
												
												transactionConfig.setStrEmail(transactionConfig.getAccountMasterToAccount().getStrEmailID());
												
												responseMsg.append("Credited successfully amount Rs. ");
												responseMsg.append(transactionConfig.getUserTxnReq().getUserTxnAmount());
												responseMsg.append(" Deposited by Customer bearing account number ");
												responseMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountType());
												responseMsg.append(" ");
												responseMsg.append(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
											}
											//bodyMsg.append( "<br/><br/><br/>");
											//bodyMsg.append( "Powered by AMS Technologies Pvt Ltd");
											
											transactionConfig.setMailMessage(bodyMsg.toString());
											
											transactionConfig.setParticipantId(participantId);
											
											userTransactionHandler.sendEmail(transactionConfig);
											
											//transactionConfig.setMessage(responseMsg.toString());
											transactionConfig.setMessage("Transaction Successfull.");
											
											processResponse.setAmsTransactionId(tranId);
											
											Date currentDate = Utils.getCurrentDate();
											processResponse.setTransactionDate(Utils.simpleDateFormat4.format(currentDate));
											processResponse.setTransactionTime(Utils.simpleTimeFormat1.format(currentDate));
											
											processResponse.setFromAccountNo(transactionConfig.getAccountMasterFromAccount().getStrAccountNumber());
											processResponse.setToAccountNo(transactionConfig.getAccountMasterToAccount().getStrAccountNumber());
											processResponse.setCurrentAvailableBalance(transactionConfig.getAccountMasterFromAccount().getStrClosingBalance());
											processResponse.setStatus("Success");
											
											processResponse.setStrTransaction_amount(userTxnAmount);
											
											processResponse.setAuthCode(transactionConfig.getAuthCode());
											processResponse.setResponseCode(transactionConfig.getResponseCode());
										}
										//Added by Sunny Soni for saving denomination Records End
									}
								}
							}
						}
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Duplicate Montra txn id");
					}
				}
				else 
				{
					transactionConfig.setCode("E0000");
					transactionConfig.setMessage("Montra txn id can not be blank");
				}
			}
			
			if (!"S0000".equalsIgnoreCase(transactionConfig.getCode()))
			{
				processResponse.setStatus("Failed");
			}
			processResponse.setMessage(transactionConfig.getMessage());
			processResponse.setCode(transactionConfig.getCode());
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
	//Agent cash withdrawal or deposit replica changes End [Note: Montra id i.e cid validation and secret code changes]

	private TransactionConfig updateValues(TransactionConfig transactionConfig)
	{
		try
		{
			//transactionConfig.setTxnAmount(transactionConfig.getUserTxnReq().getUserTxnAmount());
			
			userTransactionHandler.reduceAccountBalanceWithLimits(transactionConfig); 
			
			AccountMaster accountMasterFromAccount = transactionConfig.getAccountMasterFromAccount();			
			transactionConfig.setAccountNo(accountMasterFromAccount.getStrAccountNumber());
			transactionConfig.setAccountTranType("WDL");
			transactionConfig.setStrNaration("Amount Transfer Successfully.");
			transactionConfig.setAccountType(accountMasterFromAccount.getStrAccountType());
			transactionConfig.setAccountClosingBalance(accountMasterFromAccount.getStrClosingBalance());
			userTransactionHandler.addAccountStatementData(transactionConfig);		
			
			userTransactionHandler.increaseAccountBalance(transactionConfig);
			
			AccountMaster accountMasterToAccount = transactionConfig.getAccountMasterToAccount();
			transactionConfig.setAccountNo(accountMasterToAccount.getStrAccountNumber());
			transactionConfig.setAccountTranType("DPT");
			transactionConfig.setStrNaration("Amount Credited Successfully.");
			transactionConfig.setAccountType(accountMasterToAccount.getStrAccountType());
			transactionConfig.setAccountClosingBalance(accountMasterToAccount.getStrClosingBalance());			
			userTransactionHandler.addAccountStatementData(transactionConfig);
			
			userTransactionHandler.addTranMasterRequestEntry(transactionConfig);
			
			transactionConfig.setUserLinkedGLAccountType(accountMasterFromAccount.getStrGLAccountType());
			transactionConfig.setAccountTranType("WDL");			
			userTransactionHandler.performGlAccountTransaction(transactionConfig); 
			
			transactionConfig.setUserLinkedGLAccountType(accountMasterToAccount.getStrGLAccountType());
			transactionConfig.setAccountTranType("DPT");			
			userTransactionHandler.performGlAccountTransaction(transactionConfig);
			
			transactionConfig.setCode("S0000");
		}
		catch(Exception e)
		{
			transactionConfig.setCode("E0000");
			transactionConfig.setMessage("Internal server error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return transactionConfig;
	}
	
	//Added by Pankaj Pawar End
}
