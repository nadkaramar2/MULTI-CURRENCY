package ams.cms.api.controller;

import java.sql.Time;
import java.util.Date;
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

import ams.cms.api.handler.ExternalTransactionHandler;
import ams.cms.api.handler.TransactionHandlerAPI;
import ams.cms.txn.handler.TransactionValidator;
import ams.cms.api.model.AccountMaster;
import ams.cms.model.AccountTypeMaster;
import ams.cms.api.model.DynamicQrGeneration;
import ams.cms.api.model.RefundRequest;
import ams.cms.api.model.RefundResponse;
import ams.cms.api.model.ReverseTransactionRequest;
import ams.cms.api.service.CustomerIdService;
import ams.cms.api.service.QrCodeService;
import ams.cms.api.service.TransactionPostingIF;
import ams.cms.api.utitlity.Utils;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.config.TransactionConfig;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.config.TransactionPostingConfig;
import ams.cms.handler.FeeTypeHandler;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.notification.EmailTemplate;
import ams.cms.notification.email.EmailService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.PayloadReqRes;

@RestController
//@RequestMapping("/txn")
public class TransactionControllerApi 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(TransactionControllerApi.class);
	
	@Autowired
	private EncryptDecryptConfig encryptDecryptData;
	
	@Autowired
	private CustomerIdService customerIdService; 
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private TransactionIdCreationConfigDao transactionIdCreationConfigDao; 
	
	@Autowired
	private TransactionHandlerAPI transactionHandlerAPI;
	
	@Autowired
	private TransactionValidator transactionValidator;
	
	//added by ankit
	@Autowired
	private GLAccountTypeMasterService glAccountTypeService;
	
	@Autowired
	private GLAccountStatementService glAccountStatementService;
	
	@Autowired
	private AccountTypeMasterService accountTypeMasterService;
	
	@Autowired
	private EmailService emailService;
	//added by ankit
	
	@Autowired
	private QrCodeService qrCodeService;
	
	@Autowired
	private TierAccountMasterService tierAccountMasterService;
	
	@Autowired
	private ExternalTransactionHandler externalTransactionHandler;
	
	@Autowired
	private ApiSecretKeyUtility apiSecretKeyUtility;
	
	@Autowired
	private	AppInfo appInfo;
	
	@Autowired
	private TransactionPostingIF transactionPostingIF;
	
	@Autowired
	private FeeTypeHandler feeTypeHandler;
	
	@RequestMapping(value="/txn/checkRecipientAccountNo", method = RequestMethod.POST)
	public ResponseEntity<?> checkRecipientAccountNo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			AccountTranMaster accountTranMaster = new ObjectMapper().readValue(decrypt, AccountTranMaster.class);

			AccountMaster accountMaster = new AccountMaster();
			accountMaster.setStrAccountNumber(accountTranMaster.getReceipentAccountNo());
			AccountMaster accountMasters = accountMasterService.getAccountDetails(accountMaster);
			if (accountMasters != null) 
			{
				String tranCategory = Utils.getTranCategoryType(accountTranMaster.getStrTran_type());
				String accountTypeCategory = accountMasters.getStrAccountTypeCategory();
				if (accountTypeCategory.equalsIgnoreCase(tranCategory))
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Account Number Matched.");
					processResponse.setAccountNumber(accountMasters.getStrAccountNumber());
					
					System.out.println("Account Number Matched."+accountTypeCategory);

					StringBuilder accountHolderName = new StringBuilder();
					accountHolderName.append(accountMasters.getStrFirstName());
					if (accountMasters.getStrMiddleName()!=null) 
					{
						accountHolderName.append(" ");
						accountHolderName.append(accountMasters.getStrMiddleName());
					}
					if (accountMasters.getStrLastName()!=null) 
					{
						accountHolderName.append(" ");
						accountHolderName.append(accountMasters.getStrLastName());
					}
					processResponse.setAccountHolderName(accountHolderName.toString());
					
					processResponse.setMessage("Data Fetched Successfully.");
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Transaction Not Allowed For This Account.");
				}
			} 
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Account Not Matched.");
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
	
	@RequestMapping(value="/txn/checkSenderAccountBalance",method=RequestMethod.POST) 
	public ResponseEntity<?> processToAccountBalanceInfo(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			AccountTranMaster accountTranMaster = new ObjectMapper().readValue(decrypt, AccountTranMaster.class);
			
			accountTranMaster.setStrTo_account_number(accountTranMaster.getSenderAccountNo());
			AccountTranMaster accTranMaster = accountTranMasterService.getAccountInfo(accountTranMaster);

			if (accTranMaster!=null) 
			{
				Double txnAmount = Double.parseDouble(accountTranMaster.getStrTransaction_amount());			    
				if("C".equalsIgnoreCase(accTranMaster.getStrAccountCategory())) 
				{
					Double balanceLimits = Double.parseDouble(accTranMaster.getStrAvailableCreditLimit());
					if (balanceLimits <= txnAmount)
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Credit Limits Breached.");
					}
				}
				else 
				{
					Double balanceAmount = Double.parseDouble(accTranMaster.getStrClosingBalance());
					if(balanceAmount <= txnAmount)
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Insufficient Balance.");
					}
				}
				if (!"E0000".equalsIgnoreCase(processResponse.getCode()))
				{
					processResponse.setCode("S0000");
					processResponse.setStatus("Success");
					processResponse.setMessage("Data Fetched Successfully.");	
				}			    
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Account not found.");
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
	
	@RequestMapping(value="/txn/performTxn", method=RequestMethod.POST)
	public ResponseEntity<?> performTxn(HttpServletRequest request, @RequestBody PayloadReqRes req) 
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
			amsLogger.writeInfoLog("PARTICIPANT-ID::"+participantId); 
			amsLogger.writeInfoLog("Inside performTxn PayloadReq::"+req);
			String decrypt = encryptDecryptData.decryptPayloadReqRes(req);
			
			amsLogger.writeInfoLog("Inside performTxn decrypt::"+decrypt);
			
			AccountTranMaster accountTranMaster = new ObjectMapper().readValue(decrypt, AccountTranMaster.class);
			accountTranMaster.setStrParticipantId(participantId);
			amsLogger.writeInfoLog("Inside performTxn accountTranMaster::"+accountTranMaster);
			if(!accountTranMaster.getSenderAccountNo().equalsIgnoreCase(accountTranMaster.getReceipentAccountNo()))
			{
				String custId =	accountTranMaster.getStrCustId();
				amsLogger.writeInfoLog("Inside performTxn custId::"+custId);
				CustomerIdCreation customerIdCreation = new CustomerIdCreation();
				customerIdCreation.setStrCustId(custId);
				
				
				String existingPIN = customerIdService.getCustomerPIN(customerIdCreation);
				amsLogger.writeInfoLog("Inside performTxn existingPIN::"+existingPIN);
				if (existingPIN != null)
				{
					if (existingPIN.equals(ams.cms.utility.Utils.generateHash(accountTranMaster.getStrPIN()))) 
					{
						AccountCreation senderAccountInfo = new AccountCreation();						
						senderAccountInfo.setStrAccountNumber(accountTranMaster.getSenderAccountNo());
						senderAccountInfo.setStrCustId(accountTranMaster.getStrCustId());						
						
						senderAccountInfo =	accountMasterService.getSenderAccountInformation(senderAccountInfo);						
						amsLogger.writeInfoLog("Inside performTxn senderAccountInfo::"+senderAccountInfo);
						if (senderAccountInfo!=null && senderAccountInfo.getStrID() != null) 
						{
							String txnAmount = accountTranMaster.getStrTransaction_amount();
							amsLogger.writeInfoLog("Inside performTxn ::: txnAmount=["+txnAmount+"]");
							
							TransactionConfig transactionConfig = new TransactionConfig();
							transactionConfig.setAccountCreation(senderAccountInfo);
							transactionConfig.setTxnAmount(txnAmount);
							transactionConfig.setAccountTranType("WDL");
							
							//Added by Pankaj Pawar [Start]
							AccountCreation recipientAccountInfoIns = new AccountCreation();
							recipientAccountInfoIns.setStrAccountNumber(accountTranMaster.getReceipentAccountNo());
							
							AccountCreation recipientAccountInfo = accountMasterService.getRecipientAccountInformationWithoutCustId(recipientAccountInfoIns);
							if (recipientAccountInfo != null && recipientAccountInfo.getStrID() != null) 
							{
								if ("Active".equalsIgnoreCase(recipientAccountInfo.getStrStatus())) 
								{
									processResponse = processClosedLoopTransaction(processResponse, accountTranMaster, transactionConfig, recipientAccountInfo, senderAccountInfo, txnAmount, null);
								}
								else 
								{
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage("Recipient Account Not Active.");
								}
							}
							else 
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Recipient Data not Found.");
							}
						}
						else
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Sender Data not Found.");
						}
					}
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Entered PIN is Incorrect.");
					}					
				}
				else
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("PIN Not Found Against this cust Id.");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Transaction Not Allowed for Same Account");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error - "+e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("Inside performTxn processResponse::"+processResponse);
		return ResponseEntity.ok(encryptDecryptData.encryptPayloadReqRes(req, processResponse));
	}
	
	private void updateQrCodeFieldsAfterTxn(AccountTranMaster accountTranMaster, TransactionPostingConfig transactionPostingConfig) 
	{
		try 
		{
			DynamicQrGeneration dynamicQrGeneration = new DynamicQrGeneration();
			dynamicQrGeneration.setPaidByAccountNumber(transactionPostingConfig.getFromAccount().getStrAccountNumber());
			dynamicQrGeneration.setPaidByAccountType(transactionPostingConfig.getFromAccount().getStrAccountType());
			dynamicQrGeneration.setTranId(transactionPostingConfig.getTxnId());
			dynamicQrGeneration.setPaymentReceivedDate(transactionPostingConfig.getTxnDate());
			dynamicQrGeneration.setPaymentReceivedTime(transactionPostingConfig.getTxnTime());
			dynamicQrGeneration.setRefNo(accountTranMaster.getDynamicQrRefNo());
			
			dynamicQrGeneration.setResponseCode("00");
			int count =	qrCodeService.updateQrCodeFieldsAfterTxn(dynamicQrGeneration);
			amsLogger.writeInfoLog("Inside performTxn count::["+count+"]");
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	//Added replica of perform txn for montra [Note : need to add cid(Montra id) validation changes- cid means coutry Id] End
	
	//added by ankit on 15-04-2023 -start-
	@SuppressWarnings("null")
	@RequestMapping(value="/txn/refundTxn", method=RequestMethod.POST)
	public ResponseEntity<?> refundRequest(HttpServletRequest request,@RequestBody PayloadReqRes req)
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
			RefundRequest refundRequest = new ObjectMapper().readValue(decrypt, RefundRequest.class);
		
			List<AccountCreation> verifyMerchantForRefundRequest = accountMasterService.verifyMerchantForRefundRequest(refundRequest);
		
			if(verifyMerchantForRefundRequest !=null && verifyMerchantForRefundRequest.size()==0) 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Merchant Not Found");
			}
			else
			{
				if (verifyMerchantForRefundRequest !=null && !verifyMerchantForRefundRequest.get(0).getStrStatus().equals("Active")) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Merchant Not Active");
				}
				else
				{
					String strMerchantAccountNo = refundRequest.getStrMerchantAccountNo();
					List<AccountTranMaster> verifyTransactionForRefund = accountTranMasterService.verifyTransactionForRefund(refundRequest);
					if (!strMerchantAccountNo.equals(verifyTransactionForRefund.get(0).getStrTo_account_number())) 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Transaction Not Found under Merchant");
					}
					else
					{
						if(verifyTransactionForRefund.size() == 0)
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Transaction Not Valid");
						}
						else
						{
							AccountTranMaster accountTranMaster = verifyTransactionForRefund.get(0);
							
							String strFrom_account_number = accountTranMaster.getStrFrom_account_number();
							AccountCreation accountCreation = new AccountCreation();
							accountCreation.setStrAccountNumber(strFrom_account_number);
							
							List<AccountCreation> accountInfoByAccountNumber = accountMasterService.getAccountInfoByAccountNumber(accountCreation);
							if(accountInfoByAccountNumber.size() == 0) 
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Payer Account Not Found");
							}
							else
							{
								RefundResponse mapDataToRefundResponse = mapDataToRefundResponse(accountInfoByAccountNumber.get(0),verifyTransactionForRefund.get(0),verifyMerchantForRefundRequest.get(0));
								processResponse.setCode("S0000");
								processResponse.setStatus("Success");
								processResponse.setMessage("Refund Request Is Valid");
								processResponse.setRefundResponse(mapDataToRefundResponse);
							}
						}
					}
				}
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
	//added by ankit on 15-04-2023 -end-
	
	//added by ankit on 15-04-2023
	private RefundResponse mapDataToRefundResponse(AccountCreation fromAccountInfo, AccountTranMaster accountTranMaster, AccountCreation toAccountInfo)
	{
		String strFromAccountHolderName = fromAccountInfo.getStrAccountHolderName();
		String strFromAccountNumber = fromAccountInfo.getStrAccountNumber();
		String strFromAccountType = fromAccountInfo.getStrAccountType();
		String strFromAccountCustID = fromAccountInfo.getStrCustId();
		
		Date strLocal_tran_date = accountTranMaster.getStrLocal_tran_date();
		Time strLocal_tran_time = accountTranMaster.getStrLocal_tran_time();
		String strTxnAmount = accountTranMaster.getStrTransaction_amount();
		String strTxn_id = accountTranMaster.getStrTxn_id();
		String strTran_type = accountTranMaster.getStrTran_type();
		
		String strToAccountHolderName = toAccountInfo.getStrAccountHolderName();
		String strToAccountType = toAccountInfo.getStrAccountType();
		String strToAccountNumber = toAccountInfo.getStrAccountNumber();
		String strToAccountCustId = toAccountInfo.getStrCustId();
		
		RefundResponse refundResponse = new RefundResponse();
		refundResponse.setStrFromAccountName(strFromAccountHolderName);
		refundResponse.setStrFromAccountCustID(strFromAccountCustID);
		refundResponse.setStrFromAccountNumber(strFromAccountNumber);
		refundResponse.setStrFromAccountType(strFromAccountType);
		
		refundResponse.setStrDateOfTransaction(strLocal_tran_date);
		refundResponse.setStrTimeOfTransaction(strLocal_tran_time);
		refundResponse.setStrTxnAmount(strTxnAmount);
		refundResponse.setStrTxnId(strTxn_id);
		
		refundResponse.setStrToAccountCustID(strToAccountCustId);
		refundResponse.setStrToAccountName(strToAccountHolderName);
		refundResponse.setStrToAccountNumber(strToAccountNumber);
		refundResponse.setStrToAccountType(strToAccountType);
		refundResponse.setStrTransactionType(strTran_type);
		return refundResponse;
	}
	//added by ankit on 15-04-2023 -end-

	//added by ankit on 16-04-2023
	@RequestMapping(value="/txn/reverseTxn", method=RequestMethod.POST)
	public ResponseEntity<?> reverseTransactionRequest(HttpServletRequest request,@RequestBody PayloadReqRes req) 
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
			ReverseTransactionRequest reverseTransactionRequest = new ObjectMapper().readValue(decrypt, ReverseTransactionRequest.class);
			
			String strMerchantAccountType = reverseTransactionRequest.getStrMerchantAccountType();
			String strMerchantAccountNumber = reverseTransactionRequest.getStrMerchantAccountNumber();
			Double strTransactionAmount = reverseTransactionRequest.getStrTransactionAmount();
			String strCustAccountType = reverseTransactionRequest.getStrCustAccountType();
			String strCustAccountNumber = reverseTransactionRequest.getStrCustAccountNumber();
			
			AccountCreation fromAccountCreation = new AccountCreation();
			fromAccountCreation.setStrAccountNumber(strMerchantAccountNumber);
			fromAccountCreation.setStrAccountType(strMerchantAccountType);
			
			List<AccountCreation> fromAccountInfoAndStatus = accountMasterService.getAccountInfoAndStatus(fromAccountCreation);
			
			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster.setStrAccountType(strMerchantAccountType);
			
			List<AccountTypeMaster> fromAccountTypeStatus = accountTypeMasterService.getOnlyAccountStatus(accountTypeMaster);
			int verifyPin = verifyPin(reverseTransactionRequest);
			if(verifyPin >0)
			{
				if(fromAccountTypeStatus.size() > 0 && fromAccountTypeStatus.get(0).getStrStatus().equals("Active")) 
				 {
					if (fromAccountInfoAndStatus.size() > 0 && fromAccountInfoAndStatus.get(0).getStrStatus().equals("Active"))
					{
						String strClosingBalance = fromAccountInfoAndStatus.get(0).getStrClosingBalance();
						Double closingBalance = Double.parseDouble(strClosingBalance);
						if (Double.compare(closingBalance, strTransactionAmount) >= 0) 
						{ 
							AccountCreation toAccountCreation = new AccountCreation();
							toAccountCreation.setStrAccountNumber(strCustAccountNumber);
							toAccountCreation.setStrAccountType(strCustAccountType);
							List<AccountCreation> toAccountInfoDetails = accountMasterService.getAccountInfoAndStatus(toAccountCreation);
						
								if(toAccountInfoDetails.size() > 0)
								{
									String strAccountType = toAccountInfoDetails.get(0).getStrAccountType();
									accountTypeMaster.setStrAccountType(strAccountType);
									List<AccountTypeMaster> toAccountTypeStatus = accountTypeMasterService.getOnlyAccountStatus(accountTypeMaster);
									if(toAccountTypeStatus.size() > 0)
									{	
										if(toAccountTypeStatus.size() > 0 && toAccountTypeStatus.get(0).getStrStatus().equals("Active"))
										{
											AccountTranMaster accountTranMaster = mapReverseRequestToAccountTranMaster(fromAccountCreation, reverseTransactionRequest);
											
											AccountTranMaster addedTranMasterData = addTranMasterData(accountTranMaster);
											
											updateMerchantClosingBalanceAndStatement(fromAccountInfoAndStatus.get(0), reverseTransactionRequest, addedTranMasterData);
											
											updateCustomerClosingBalanceAndStatement(toAccountInfoDetails.get(0), reverseTransactionRequest, addedTranMasterData);
											
											reduceGLAccountBalanceAndAddStatementSender(fromAccountInfoAndStatus.get(0), reverseTransactionRequest, addedTranMasterData);
											
											addGLAccountBalanceAndAddStatementRecipient(toAccountInfoDetails.get(0), reverseTransactionRequest, addedTranMasterData);
											
											//sendRefundMail(toAccountCreation, accountTranMaster);
											sendRefundMail(toAccountInfoDetails.get(0), accountTranMaster);
											
											processResponse.setCode("S0000");
											processResponse.setStatus("Success");
											processResponse.setMessage("Transaction Reversed Succesfully amount Rs."+strTransactionAmount);
										}
										else 
										{
											processResponse.setCode("E0000");
											processResponse.setStatus("Failed");
											processResponse.setMessage("To Account Type Is Inactive");
										}
									}
									else
									{
										processResponse.setCode("E0000");
										processResponse.setStatus("Failed");
										processResponse.setMessage("To Account Type Not Found");
									}
								}
								else
								{
									processResponse.setCode("E0000");
									processResponse.setStatus("Failed");
									processResponse.setMessage("Customer Account No Not Found");
								}
							}
							else
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Not Enough Balance For Transaction");
							}
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Merchant Account Not Found or Inactive");
						}
					} 
					else
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Account Type is Inactive");
					}
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Pin Is Wrong");
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
	//added by ankit on 16-04-2023
			
	public int updateMerchantClosingBalanceAndStatement(AccountCreation accountCreationDetails, ReverseTransactionRequest reverseTransactionRequest,AccountTranMaster accountTranMaster) 
	{
		String strTxn_id = accountTranMaster.getStrTxn_id();
		String strClosingBalance = accountCreationDetails.getStrClosingBalance();
		Double strTransactionAmount = reverseTransactionRequest.getStrTransactionAmount();
		String strMerchantAccountNumber = reverseTransactionRequest.getStrMerchantAccountNumber();
		String strMerchantAccountType = reverseTransactionRequest.getStrMerchantAccountType();
		accountCreationDetails.setStrAccountType(strMerchantAccountType);
		accountCreationDetails.setStrAccountNumber(strMerchantAccountNumber);
		
		double closingBalance = Double.parseDouble(strClosingBalance);
		Double newClosingBalance = closingBalance - strTransactionAmount;
		String strNewClosingBalance = ams.cms.utility.Utils.decimalFormat.format(newClosingBalance);
		accountCreationDetails.setStrClosingBalance(strNewClosingBalance);
		
		try
		{
			int updateOnlyBalance = accountMasterService.updateOnlyBalance(accountCreationDetails);
			
			AccountTranMaster senderAccountTranMaster = new AccountTranMaster();
			senderAccountTranMaster.setSenderAccountNo(strMerchantAccountNumber);;
			senderAccountTranMaster.setStrSenderAccountType(strMerchantAccountType);
			senderAccountTranMaster.setStrTxn_id(strTxn_id);
			senderAccountTranMaster.setStrSenderClosingBalance(strNewClosingBalance);
			senderAccountTranMaster.setStrTransaction_amount(String.valueOf(strTransactionAmount));
			senderAccountTranMaster.setStrTran_type("REV");
			
			transactionHandlerAPI.addSenderAccountStatement(senderAccountTranMaster);
			return updateOnlyBalance;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	public int updateCustomerClosingBalanceAndStatement(AccountCreation accountCreationDetails, ReverseTransactionRequest reverseTransactionRequest,AccountTranMaster accountTranMaster) 
	{
		String strTxn_id = accountTranMaster.getStrTxn_id();
		String strClosingBalance = accountCreationDetails.getStrClosingBalance();
		Double strTransactionAmount = reverseTransactionRequest.getStrTransactionAmount();
		
		String strCustomerAccountNumber = reverseTransactionRequest.getStrCustAccountNumber();
		String strCustomerAccountType = reverseTransactionRequest.getStrCustAccountType();
		
		accountCreationDetails.setStrAccountType(strCustomerAccountType);
		accountCreationDetails.setStrAccountNumber(strCustomerAccountNumber);
		
		/*
		String strCustomerAccountNumber = reverseTransactionRequest.getStrMerchantAccountNumber();
		String strCustomerAccountType = reverseTransactionRequest.getStrMerchantAccountType();
		accountCreationDetails.setStrAccountType(strCustomerAccountNumber);
		accountCreationDetails.setStrAccountNumber(strCustomerAccountType);
		*/
		
		double closingBalance = Double.parseDouble(strClosingBalance);
		Double newClosingBalance = closingBalance + strTransactionAmount;
		String strNewClosingBalance = ams.cms.utility.Utils.decimalFormat.format(newClosingBalance);
		accountCreationDetails.setStrClosingBalance(strNewClosingBalance);
		
		try 
		{
			int updateOnlyBalance = accountMasterService.updateOnlyBalance(accountCreationDetails);
			AccountTranMaster receiverAccountTranMaster = new AccountTranMaster();
			
			receiverAccountTranMaster.setReceipentAccountNo(strCustomerAccountNumber);;
			receiverAccountTranMaster.setStrReceipentAccountType(strCustomerAccountType);
			receiverAccountTranMaster.setStrTxn_id(strTxn_id);
			receiverAccountTranMaster.setStrReceipentClosingBalance(strNewClosingBalance);
			receiverAccountTranMaster.setStrTransaction_amount(String.valueOf(strTransactionAmount));
			receiverAccountTranMaster.setStrTran_type("REV");
			
			transactionHandlerAPI.addRecipientAccountStatement(receiverAccountTranMaster);
			return updateOnlyBalance;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}		
		return 0;
	}
	
	public AccountTranMaster addTranMasterData(AccountTranMaster accountTranMaster) 
	{	
		try
		{
			String transactionId = transactionIdCreationConfigDao.getTransactionId();
			accountTranMaster.setStrTxn_id(transactionId);
			accountTranMaster.setSenderAccountNo(accountTranMaster.getStrFrom_account_number());
			accountTranMaster.setReceipentAccountNo(accountTranMaster.getStrTo_account_number());
			accountTranMaster.setStrTran_type(accountTranMaster.getStrTran_type());
			transactionHandlerAPI.addTransactionMasterData(accountTranMaster);
			return accountTranMaster;
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public String reduceGLAccountBalanceAndAddStatementSender(AccountCreation accountCreation, ReverseTransactionRequest refundTransactionRequest,AccountTranMaster accountTranMaster)
	{
		try 
		{
			String strAccountType = accountCreation.getStrAccountType();
			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster.setStrAccountType(strAccountType);
			List<AccountTypeMaster> glAccontNoAndType = accountTypeMasterService.getGLAccontNoAndType(accountTypeMaster);
			if(glAccontNoAndType.size()>0)
			{
				String strGLAccountNumber = glAccontNoAndType.get(0).getStrGLAccountNumber();
				if(strGLAccountNumber == null || strGLAccountNumber.isEmpty()) 
				{
				}
				else
				{
					String fetchedGLAccountNo = glAccontNoAndType.get(0).getStrGLAccountNumber().trim();
					String fetchedGLAccountType = glAccontNoAndType.get(0).getStrGLAccountType();
					GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
					glAccountTypeMaster.setStrAccountNumber(fetchedGLAccountNo);
					glAccountTypeMaster.setStrGLAccountType(fetchedGLAccountType);
					Double strTransactionAmount = refundTransactionRequest.getStrTransactionAmount();
					
					//using already present method 
					//String closingBalanceOfGlAccount = glAccountTypeService.getClosingBalanceOfGlAccount(glAccountTypeMaster);
					
					//added these lines by ankit on 04-05-2023
					List<GLAccountTypeMaster> closingBalanceAndDescriptionOfGlAccount = glAccountTypeService.getClosingBalanceAndDescriptionOfGlAccount(glAccountTypeMaster);
					String closingBalanceOfGlAccount = closingBalanceAndDescriptionOfGlAccount.get(0).getStrClosingBalance();
					String strGLAccountDescription = closingBalanceAndDescriptionOfGlAccount.get(0).getStrGLAccountDescription();
					//added by ankit lines on 04-05-2023
					
					double closingBalance = Double.parseDouble(closingBalanceOfGlAccount);
					Double newClosingBalance = closingBalance - strTransactionAmount;
					String strNewClosingBalance = ams.cms.utility.Utils.decimalFormat.format(newClosingBalance);
					glAccountTypeMaster.setStrClosingBalance(strNewClosingBalance);
					
					//updating the new Closing Balance reusing the method
					glAccountTypeService.updateGLAccountTypeDetails(glAccountTypeMaster);
					
					GLAccountStatement glAccountStatement = new GLAccountStatement();
					glAccountStatement.setStrAccountNumber(fetchedGLAccountNo);
					glAccountStatement.setStrGLAccountType(fetchedGLAccountType);
					glAccountStatement.setStrAmount(String.valueOf(strTransactionAmount));
					
					//added these four lines 
					glAccountStatement.setStrClosingBalance(strNewClosingBalance);
					glAccountStatement.setTransactionDate(new Date());
					glAccountStatement.setStrTranType("REV");
					glAccountStatement.setStrRef(strGLAccountDescription);
					//added these four lines
					
					String transactionId = accountTranMaster.getStrTxn_id();
					glAccountStatement.setStrTxnId(transactionId);
					glAccountStatement.setStrCreated_by("System");
					glAccountStatement.setStrTranMode("Debit");
					glAccountStatementService.addGlAccountStatementData(glAccountStatement);
				}
			}
		}
		catch (Exception e) 
		{
				amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public String addGLAccountBalanceAndAddStatementRecipient(AccountCreation accountCreation, ReverseTransactionRequest refundTransactionRequest, AccountTranMaster accountTranMaster)
	{
		try
		{
			String strAccountType = accountCreation.getStrAccountType();
			AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
			accountTypeMaster.setStrAccountType(strAccountType);
			List<AccountTypeMaster> glAccontNoAndType = accountTypeMasterService.getGLAccontNoAndType(accountTypeMaster);
			
			if(glAccontNoAndType.size()>0)
			{
				String strGLAccountNumber = glAccontNoAndType.get(0).getStrGLAccountNumber();
				if(strGLAccountNumber == null || strGLAccountNumber.isEmpty()) 
				{
					
				}
				else
				{
					String fetchedGLAccountNo = glAccontNoAndType.get(0).getStrGLAccountNumber().trim();
					String fetchedGLAccountType = glAccontNoAndType.get(0).getStrGLAccountType();
					GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
					glAccountTypeMaster.setStrAccountNumber(fetchedGLAccountNo);
					glAccountTypeMaster.setStrGLAccountType(fetchedGLAccountType);
					Double strTransactionAmount = refundTransactionRequest.getStrTransactionAmount();
					
					//using already present method 
					//String closingBalanceOfGlAccount = glAccountTypeService.getClosingBalanceOfGlAccount(glAccountTypeMaster);
					
					//added these lines by ankit on 04-05-2023
					List<GLAccountTypeMaster> closingBalanceAndDescriptionOfGlAccount = glAccountTypeService.getClosingBalanceAndDescriptionOfGlAccount(glAccountTypeMaster);
					String closingBalanceOfGlAccount = closingBalanceAndDescriptionOfGlAccount.get(0).getStrClosingBalance();
					String strGLAccountDescription = closingBalanceAndDescriptionOfGlAccount.get(0).getStrGLAccountDescription();
					//added by ankit lines on 04-05-2023
					
					double closingBalance = Double.parseDouble(closingBalanceOfGlAccount);
					Double newClosingBalance = closingBalance + strTransactionAmount;
					String strNewClosingBalance = ams.cms.utility.Utils.decimalFormat.format(newClosingBalance);
					glAccountTypeMaster.setStrClosingBalance(strNewClosingBalance);
					
					//updating the new Closing Balance reusing the method
					glAccountTypeService.updateGLAccountTypeDetails(glAccountTypeMaster);
					
					GLAccountStatement glAccountStatement = new GLAccountStatement();
					glAccountStatement.setStrAccountNumber(fetchedGLAccountNo);
					glAccountStatement.setStrGLAccountType(fetchedGLAccountType);
					glAccountStatement.setStrAmount(String.valueOf(strTransactionAmount));
					String transactionId = accountTranMaster.getStrTxn_id();
					glAccountStatement.setStrTxnId(transactionId);
					glAccountStatement.setStrCreated_by("System");
					glAccountStatement.setStrTranMode("Credit");
					
					//added these four lines 
					glAccountStatement.setStrClosingBalance(strNewClosingBalance);
					glAccountStatement.setTransactionDate(new Date());
					glAccountStatement.setStrTranType("REV");
					glAccountStatement.setStrRef(strGLAccountDescription);
					//added these four lines 
					
					glAccountStatementService.addGlAccountStatementData(glAccountStatement);
				}
			}
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	
	public int verifyPin(ReverseTransactionRequest refundTransactionRequest) 
	{
		String strMerchantPin = refundTransactionRequest.getStrMerchantPin();
		String strMerchantCustId = refundTransactionRequest.getStrMerchantCustId();
		CustomerIdCreation customerIdCreation = new CustomerIdCreation();
		customerIdCreation.setStrCustId(strMerchantCustId);
		try
		{
			String generatePin = ams.cms.utility.Utils.generateHash(strMerchantPin);
			String customerPIN = customerIdService.getCustomerPIN(customerIdCreation);
			if(generatePin.equals(customerPIN))
			{
				return 1;
			}	
		}
		catch(Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return 0;
	}
	
	@SuppressWarnings("null")
	public String sendRefundMail(AccountCreation accountCreation,AccountTranMaster accountTranMaster) 
	{
		try
		{
			String strCustId = accountCreation.getStrCustId();
			CustomerIdCreation customerIdCreation = new CustomerIdCreation();
			customerIdCreation.setStrCustId(strCustId);

			List<CustomerIdCreation> emailOfCustomer = customerIdService.getEmailOfCustomer(customerIdCreation);

			if (emailOfCustomer.size() > 0)
			{
				String strEmailID = emailOfCustomer.get(0).getStrEmailID();
				if (strEmailID != null || strEmailID.isEmpty())
				{
					EmailTemplate emailTemplate = new EmailTemplate();
					String stringSenderEmail = "contactus@AMStechnologies.com";
					emailTemplate.setStrFrom(stringSenderEmail);
					emailTemplate.setStrTo(strEmailID);
					emailTemplate.setStrSubject("Refund Approval");
					emailTemplate.setStrText("Dear" + accountCreation.getStrAccountHolderName()
							+ "Your transaction Bearing TransactionID " + accountTranMaster.getStrTxn_id()
							+ " has been reversed Succesfully bearing amount "
							+ accountTranMaster.getStrTransaction_amount() + " on date "
							+ accountTranMaster.getStrLocal_tran_date() + " at time "
							+ accountTranMaster.getStrLocal_tran_time());
					emailService.sendSimpleMessage(emailTemplate);
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return "Y";
	}

	public AccountCreation mapRefundRequestToAccountCreation(ReverseTransactionRequest refundTransactionRequest)
	{
		String strMerchantAccountNumber = refundTransactionRequest.getStrMerchantAccountNumber();
		String strMerchantAccountType = refundTransactionRequest.getStrMerchantAccountType();
		String strMerchantCustId = refundTransactionRequest.getStrMerchantCustId();
		AccountCreation accountCreation = new AccountCreation();
		accountCreation.setStrAccountNumber(strMerchantAccountNumber);
		accountCreation.setStrAccountType(strMerchantAccountType);
		accountCreation.setStrCustId(strMerchantCustId);
		return accountCreation;
	}
	
	public AccountTranMaster mapReverseRequestToAccountTranMaster(AccountCreation accountCreation, ReverseTransactionRequest reverseTransactionRequest)
	{
		String strCustAccountNumber = reverseTransactionRequest.getStrCustAccountNumber();
		String strMerchantAccountNumber = reverseTransactionRequest.getStrMerchantAccountNumber();
		Double strTransactionAmount = reverseTransactionRequest.getStrTransactionAmount();
		String strTransactionType = reverseTransactionRequest.getStrTransactionType();
		AccountTranMaster accountTranMaster = new AccountTranMaster();
		accountTranMaster.setStrFrom_account_number(strMerchantAccountNumber);
		accountTranMaster.setStrTo_account_number(strCustAccountNumber);
		accountTranMaster.setStrTransaction_amount(String.valueOf(strTransactionAmount));
		accountTranMaster.setStrTran_type(strTransactionType);
		return accountTranMaster;
	}
	
	public String verifyMerchant(AccountCreation accountCreationDetails, ReverseTransactionRequest reverseTransactionRequest) 
	{
		Double strTransactionAmount = reverseTransactionRequest.getStrTransactionAmount();
		String strStatus = accountCreationDetails.getStrStatus();
		if(!strStatus.equals("Active"))
		{
			return "Merchant Account is Not Active";
		}
		else
		{
			String strClosingBalance = accountCreationDetails.getStrClosingBalance();
			double closingBalance = Double.parseDouble(strClosingBalance);
			if(Double.compare(closingBalance, strTransactionAmount) == -1) 
			{
				return "You dont Have Sufficient Balance";
			}
			else
			{
				return "Y";
			}
		}
	}

	public String receiverUpdateBalanceAndEntryInStatement(ReverseTransactionRequest refundTransactionRequest, AccountTranMaster accountTranMaster) 
	{
		try {
			String strCustAccountNumber = refundTransactionRequest.getStrCustAccountNumber();
			String strCustAccountType = refundTransactionRequest.getStrCustAccountType();
			
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrAccountType(strCustAccountType);
			accountCreation.setStrAccountNumber(strCustAccountNumber);
			
			List<AccountCreation> receiverInfromation = accountMasterService.getReceiverInfromation(accountCreation);
			AccountCreation receiverAccountCreation = receiverInfromation.get(0);
			String strClosingBalance = receiverAccountCreation.getStrClosingBalance();
			Double strTransactionAmount = refundTransactionRequest.getStrTransactionAmount();
			
			Double closingBalance = Double.parseDouble(strClosingBalance);
			Double newStrClosingBalance = closingBalance + strTransactionAmount;
			receiverAccountCreation.setStrClosingBalance(String.valueOf(newStrClosingBalance));
			accountMasterService.updateOnlyBalance(receiverAccountCreation);
			
			String strTxn_id = accountTranMaster.getStrTxn_id();
			
			AccountTranMaster receiverAccountTranMaster = new AccountTranMaster();
			receiverAccountTranMaster.setStrAccountType(strCustAccountType);
			receiverAccountTranMaster.setStrAccountNumber(strCustAccountNumber);
			receiverAccountTranMaster.setStrTxn_id(strTxn_id);
			receiverAccountTranMaster.setStrClosingBalance(String.valueOf(newStrClosingBalance));
			receiverAccountTranMaster.setStrAccountType(strCustAccountType);
			transactionHandlerAPI.addRecipientAccountStatement(receiverAccountTranMaster);
			
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	//-------------------- part6 End --{not in use}--------------------
	private ProcessResponse processClosedLoopTransaction(ProcessResponse processResponse, AccountTranMaster accountTranMaster, TransactionConfig transactionConfig, AccountCreation recipientAccountInfo, AccountCreation senderAccountInfo, String txnAmount, String montraTxnId) 
	{
		try 
		{
			accountTranMaster.setAccountCreation(recipientAccountInfo);
			transactionConfig.setAccountTranMaster(accountTranMaster);
			
			transactionConfig = transactionValidator.txnValidate(transactionConfig);
			amsLogger.writeInfoLog("Inside performTxn transactionConfig::"+transactionConfig);
			amsLogger.writeInfoLog("PARTICIPANT-ID-CloseLoop::"+accountTranMaster.getStrParticipantId()); 
			
			//Added for Validating to account cumulative balance limit by Pankaj [start] 
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
			{
				if (!"C".equalsIgnoreCase(recipientAccountInfo.getAccountCategoryType())) 
				{
					TransactionConfig toTransactionConfig = new TransactionConfig();
					toTransactionConfig.setCode("S0000");
					toTransactionConfig.setAccountCreation(recipientAccountInfo);
					toTransactionConfig.setTxnAmount(txnAmount);
					
					toTransactionConfig = transactionValidator.validateCummulativeBalanceLimit(toTransactionConfig); //Validate to Account cumulative balance
					amsLogger.writeInfoLog("Inside performTxn toTransactionConfig::"+toTransactionConfig);
					if(!"S0000".equalsIgnoreCase(toTransactionConfig.getCode()))
					{
						transactionConfig.setCode("E0000");
						transactionConfig.setMessage(toTransactionConfig.getMessage());									 
					}
				}
			}
			//Added for Validating to account cumulative balance limit by Pankaj [end]
			
			if ("S0000".equalsIgnoreCase(transactionConfig.getCode())) 
			{
				String txnId =  transactionIdCreationConfigDao.getTransactionId();
				accountTranMaster.setStrTxn_id(txnId);
				
				//Added changes for fee and vat related Start
				TransactionPostingConfig transactionPostingConfig = new TransactionPostingConfig();
				transactionPostingConfig.setTxnId(txnId);
				
				if (accountTranMaster.getFeeType() != null && accountTranMaster.getFeeType().trim().length() > 0) 
				{
					transactionPostingConfig.setFeeType(accountTranMaster.getFeeType());
					transactionPostingConfig.setFeeApplicableTo(accountTranMaster.getFeeApplicableTo());
					
					transactionPostingConfig = feeTypeHandler.getFeeAndVatGLAccounts(transactionPostingConfig);
				}
				
				transactionPostingConfig.setTxnDate(Utils.getCurrentDate());
				transactionPostingConfig.setTxnTime(Utils.getFormattedCurrentTime());
				
				//transactionPostingConfig.setFromAccount(senderAccountInfo);
				//transactionPostingConfig.setToAccount(recipientAccountInfo);				
				
				GLAccountTypeMaster senderLinkGLAccountType = new GLAccountTypeMaster();
				senderLinkGLAccountType.setStrGLAccountType(senderAccountInfo.getStrGLAccountType());
				senderLinkGLAccountType.setStrAccountNumber(senderAccountInfo.getStrGLAccountNo());
				senderLinkGLAccountType.setStrGLAccountDescription(senderAccountInfo.getStrGLAccountDescription());
				senderLinkGLAccountType.setStrClosingBalance(senderAccountInfo.getStrGLAccountBalance());
				
				transactionPostingConfig.setFromLinkGLAccount(senderLinkGLAccountType);
				
				GLAccountTypeMaster recipientLinkGLAccountType = new GLAccountTypeMaster();
				recipientLinkGLAccountType.setStrGLAccountType(recipientAccountInfo.getStrGLAccountType());
				recipientLinkGLAccountType.setStrAccountNumber(recipientAccountInfo.getStrGLAccountNo());
				recipientLinkGLAccountType.setStrGLAccountDescription(recipientAccountInfo.getStrGLAccountDescription());
				recipientLinkGLAccountType.setStrClosingBalance(recipientAccountInfo.getStrGLAccountBalance());
				
				transactionPostingConfig.setToLinkGLAccount(recipientLinkGLAccountType);
 				
				transactionPostingIF.createTransactionPostingData(transactionPostingConfig);
				//Added changes for fee and vat related End
				
				if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
				{
					if (!"C".equalsIgnoreCase(senderAccountInfo.getAccountCategoryType())) 
					{
						transactionConfig = externalTransactionHandler.updateLimitValues(transactionConfig);																	
						tierAccountMasterService.updateCummAvailableBalance(transactionConfig.getTierAccountMaster());
					}
				}
				
				transactionHandlerAPI.updateSenderAccountMaster(accountTranMaster, senderAccountInfo);
				
				transactionHandlerAPI.updateSenderLinkedGLAccountMaster(accountTranMaster, senderAccountInfo);
				
				transactionHandlerAPI.addSenderAccountStatement(accountTranMaster);
				
				transactionHandlerAPI.updateRecipientAccountMaster(accountTranMaster);								
				
				transactionHandlerAPI.addRecipientAccountStatement(accountTranMaster);	
				
				//Added auth code changes Start
				String authCode = ams.cms.utility.Utils.getAlphaNumericString();
				accountTranMaster.setStrAuthCode(authCode);
				accountTranMaster.setStrSrcTxnId(montraTxnId);//Added montra txn id entry
				//Added auth code changes End
				amsLogger.writeInfoLog("PARTICIPANT-ID-TransactionEntry::"+accountTranMaster.getStrParticipantId()); 
				
				transactionHandlerAPI.addTransactionMasterData(accountTranMaster);
				
				if ("C".equalsIgnoreCase(accountTranMaster.getAccountCategoryType())) 
				{
					transactionHandlerAPI.addCreditCardRelatedEntry(accountTranMaster);
				}
				try 
				{
					if(accountTranMaster!=null && accountTranMaster.getDynamicQrRefNo() != null && accountTranMaster.getDynamicQrRefNo().trim().length() > 0 )
					{
						DynamicQrGeneration dynamicQrGeneration = new DynamicQrGeneration();
						dynamicQrGeneration.setPaidByAccountNumber(senderAccountInfo.getStrAccountNumber());
						dynamicQrGeneration.setPaidByAccountType(senderAccountInfo.getStrAccountType());
						dynamicQrGeneration.setResponseCode(accountTranMaster.getStrResponseCode());
						dynamicQrGeneration.setTranId(txnId);
						dynamicQrGeneration.setPaymentReceivedDate(Utils.getCurrentDateFormat());
						dynamicQrGeneration.setPaymentReceivedTime(Utils.getFormattedCurrentTime());
						dynamicQrGeneration.setRefNo(accountTranMaster.getDynamicQrRefNo());
						int count =	qrCodeService.updateQrCodeFieldsAfterTxn(dynamicQrGeneration);
						amsLogger.writeInfoLog("Inside performTxn count::["+count+"]");
					}	
				}	
				catch (Exception e)
				{
					amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
				}
				
				processResponse.setCode("S0000");
				processResponse.setStatus("Success");
				processResponse.setMessage("Transaction Successful.");	
				
				Date switchTxnDate = accountTranMaster.getSwitchTxDate();
				
				if (montraTxnId!=null)
				{
					processResponse.setCid(accountTranMaster.getCid());
					processResponse.setCustId(accountTranMaster.getStrCustId());
					processResponse.setMontraTxnId(montraTxnId);
					
					processResponse.setAmsTransactionId(accountTranMaster.getStrTxn_id());
					processResponse.setAuthCode(authCode);
					
					processResponse.setTransactionDate(ams.cms.utility.Utils.simpleDateFormat4.format(switchTxnDate));
					processResponse.setTransactionTime(ams.cms.utility.Utils.simpleTimeFormat1.format(switchTxnDate));
				}
				else 
				{
					processResponse.setTransactionId(accountTranMaster.getStrTxn_id());
					processResponse.setTransactionDate(ams.cms.utility.Utils.simpleDateTimeFormat.format(switchTxnDate));
				}
				
				processResponse.setFromAccountNo(accountTranMaster.getSenderAccountNo());
				processResponse.setToAccountNo(accountTranMaster.getReceipentAccountNo());
				processResponse.setResponseCode(accountTranMaster.getStrResponseCode());
				processResponse.setCurrentAvailableBalance(accountTranMaster.getStrSenderClosingBalance()); //Closing balance of Sender.
			}
			else
			{
				processResponse.setCode(transactionConfig.getCode());
				processResponse.setStatus("Failed");
				processResponse.setMessage(transactionConfig.getMessage());
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
}
	
