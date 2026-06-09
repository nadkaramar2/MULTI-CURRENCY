package ams.cms.api.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import ams.cms.api.dao.BulkTransferDao;
import ams.cms.api.model.AccountMaster;
import ams.cms.api.model.BulkTransfer;
import ams.cms.api.model.BulkTransferDto;
import ams.cms.api.model.BulkTransferExcelReqRes;
import ams.cms.api.model.BulkTransferFromAccounts;
import ams.cms.api.model.BulkTransferToAccounts;
import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.api.service.BulkTransferService;
import ams.cms.config.AppInfo;
import ams.cms.config.CommonConstants;
import ams.cms.config.TransactionIdCreationConfigDao;
import ams.cms.constants.TransactionType;
import ams.cms.dao.AccountMasterDao;
import ams.cms.dao.AccountTypeMasterDao;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountStatement;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.CustomerIdCreation;
import ams.cms.model.GLAccountStatement;
import ams.cms.model.GLAccountTypeMaster;
import ams.cms.model.TierAccountMaster;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountStatementService;
import ams.cms.services.AccountTranMasterService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.CustomerIDCreationService;
import ams.cms.services.GLAccountStatementService;
import ams.cms.services.GLAccountTypeMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.txn.handler.GLTransactionHandler;
import ams.cms.txn.handler.JournalTransferHandlerImpl;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.Utils;

@Component
public class BulkTransferHandlerImpl implements BulkTransferHandler 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(BulkTransferHandlerImpl.class);

	@Autowired
	private	TransactionIdCreationConfigDao transactionIdCreationConfigDao;

	@Autowired
	private AccountMasterService accountMasterService;

	@Autowired
	private BulkTransferDao bulkTransferDao;

	@Autowired
	private	CustomerIDCreationService customerMasterService;

	@Autowired
	private TierAccountMasterService tierAccountMasterService;
	
	@Autowired
	private GLAccountStatementService glAccountStatementService;

	@Autowired
	private GLAccountTypeMasterService glAccountTypeMasterService;
	
	@Autowired
	private BulkTransferService bulkTransferService;
	
	@Autowired
	private AccountTranMasterService accountTranMasterService;
	
	@Autowired
	private AccountStatementService accountStatementService;
	
	@Autowired
	private AppInfo appInfo;
	
	private static final String ID_COLUMN_HEADER = "id";
    private static final String FROM_ACCOUNT_NUMBER_COLUMN_HEADER = "senderaccountno";
    private static final String FROM_ACCOUNT_TYPE_COLUMN_HEADER = "senderaccounttype";
    private static final String FROM_ACCOUNT_NAME_COLUMN_HEADER = "senderaccountname";
    private static final String TO_ACCOUNT_NUMBER_COLUMN_HEADER = "receiveraccountno";
    private static final String TO_ACCOUNT_TYPE_COLUMN_HEADER = "receiveraccounttype";
    private static final String TO_ACCOUNT_NAME_COLUMN_HEADER = "receiveraccountname";
    private static final String AMOUNT = "transferamount";
	
	// checking only Sender Account No
	@Override
	public ProcessResponse bulkTransferOneToMany(BulkTransferDto bulkTransferDto) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		List<BulkTransferToAccounts> bulkTrasnferToAccounts = bulkTransferDto.getBulkTransferToAccounts();

		Double strFromTransactionAmount = bulkTransferDto.getStrTransferAmount();

		try 
		{
			String strFromAccountNo = bulkTransferDto.getStrFromAccountNo();
			String strFromAccountType = bulkTransferDto.getStrFromAccountType();
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrAccountNumber(strFromAccountNo);
			accountCreation.setStrAccountType(strFromAccountType);

			List<AccountCreation> fromAccountDetails = accountMasterService.findByAccountNumber(accountCreation);
			Double wholeTransactionAmount = 0.0;

			if (fromAccountDetails.size() == 0) 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Please Check Sender Account Number");
			} 
			else
			{
				// account active check
				if (fromAccountDetails.get(0).getStrStatus() != null && !fromAccountDetails.get(0).getStrStatus().equals("Active")) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Sender Account is Not Active");
				} 
				else 
				{
					for (BulkTransferToAccounts bulkTransferToAccount : bulkTrasnferToAccounts) 
					{
						Double currentToTransactionAmount = bulkTransferToAccount.getStrTransactionAmount();
						wholeTransactionAmount = wholeTransactionAmount + currentToTransactionAmount;
					}
					String strFromAccountClosingBalance = fromAccountDetails.get(0).getStrClosingBalance();
					Double fromAccountClosingBalance = Double.valueOf(strFromAccountClosingBalance);

					if (Double.compare(strFromTransactionAmount, wholeTransactionAmount) == 0) 
					{
						if (Double.compare(fromAccountClosingBalance, wholeTransactionAmount) >= 0)
						{
							addBulkTrasnferOneToMany(bulkTransferDto);
							processResponse.setCode("S0000");
							processResponse.setStatus("Success");
							processResponse.setMessage("Sucessfully Fully Added Bulk Entries");
						} 
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("failed");
							processResponse.setMessage("Not Enough Closing Balance in Sender Account");
						}
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Total Transaction Amount Balance Mismatch");
					}
				}
			}
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	//Checking only Receivers Account No
	@SuppressWarnings("null")
	@Override
	public ProcessResponse bulkTransferManyToOne(BulkTransferDto bulkTransferDto) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		double strTransferAmount = bulkTransferDto.getStrTransferAmount();
		List<BulkTransferFromAccounts> bulkTrasnferFromAccounts = bulkTransferDto.getBulkTransferFromAccounts();
		String strToAccountNo = bulkTransferDto.getStrToAccountNo();
		String strToAccountType = bulkTransferDto.getStrToAccountType();
		double strTotalCalculatedTransferAmount = 0.0;
		
		try
		{	
			AccountCreation accountCreation = new AccountCreation();
			accountCreation.setStrAccountNumber(strToAccountNo);
			accountCreation.setStrAccountType(strToAccountType);
			List<AccountCreation> toAccountNoDetails = accountMasterService.findByAccountNumber(accountCreation);
			
			if (toAccountNoDetails !=null && toAccountNoDetails.size() == 0) 
			{
				processResponse.setCode("E0000");
				processResponse.setStatus("Failed");
				processResponse.setMessage("Please Check Account No");
			}
			else 
			{
				if (toAccountNoDetails !=null && toAccountNoDetails.get(0).getStrStatus() != null && !toAccountNoDetails.get(0).getStrStatus().equals("Active")) 
				{
					processResponse.setCode("E0000");
					processResponse.setStatus("Failed");
					processResponse.setMessage("Account is Not Active");
				}
				else 
				{
					for (BulkTransferFromAccounts bulkTransferFromAccount : bulkTrasnferFromAccounts) 
					{
						Double currentToTransactionAmount = bulkTransferFromAccount.getStrTransactionAmount();
						strTotalCalculatedTransferAmount = strTotalCalculatedTransferAmount + currentToTransactionAmount;
					}
											
					if(Double.compare(strTotalCalculatedTransferAmount, strTransferAmount) ==0)
					{
						String strCustId = toAccountNoDetails.get(0).getStrCustId();
						CustomerIdCreation customerIdCreation = new CustomerIdCreation();
						customerIdCreation.setStrCustId(strCustId);
						
						// using the already defined method
						CustomerIdCreation customerInformationByCustId = customerMasterService.getCustomerInformationByCustId(customerIdCreation);
						String strActiveTier = customerInformationByCustId.getStrActiveTier();

						TierAccountMaster tierAccountMaster = new TierAccountMaster();
						tierAccountMaster.setStrAccountNo(strToAccountNo);
						tierAccountMaster.setStrAccountType(strToAccountType);
						tierAccountMaster.setStrActiveTier(strActiveTier);
						tierAccountMaster.setStrCustId(strCustId);
						TierAccountMaster tierInfoByCustID = tierAccountMasterService.getTierInfoByCustID(tierAccountMaster);

						String strCurrentCumulativeBalanceLimit = tierInfoByCustID.getStrCumulativeBalanceLimit();
						double cumulativeBalance = Double.parseDouble(strCurrentCumulativeBalanceLimit);
						
						double closingBalanceOfRecipient = Double.parseDouble(toAccountNoDetails.get(0).getStrClosingBalance());
						Double strPreCredAmount = toAccountNoDetails.get(0).getStrPreCredAmount();
						Double totalTransactionAmount = strTransferAmount + strPreCredAmount + closingBalanceOfRecipient;
						
						if (Double.compare(cumulativeBalance, totalTransactionAmount) >= 0)
						{		
							addbulkTransferManyToOne(bulkTransferDto);
							
							processResponse.setCode("S0000");
							processResponse.setStatus("Success");
							processResponse.setMessage("Sucessfully added the Bulk Transfer Entries");
						} 
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Cumulative Balance Limit Reached");
						}
					}
					else 
					{
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Total Transaction Amount Mismatch");
					}
				}
			}	
		} 
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");			
			processResponse.setMessage("Internal Server Error");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}	
		return processResponse;
	}
	
	// ---------verify Bulk Transfer Process  START---------------
	@Override
	public ProcessResponse verifyBulkTransferEntries(BulkTransfer bulkTransfer)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try
		{
			String bulkMode = bulkTransfer.getStrBulkMode();
			if ("OTM".equalsIgnoreCase(bulkMode)) 
			{
				processResponse = verifyBulkTransferOTMEntries(bulkTransfer);
			}
			else if ("MTO".equalsIgnoreCase(bulkMode)) 
			{
				processResponse = verifyBulkTransferMTOEntries(bulkTransfer);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("No Bulk Mode found!!");
			}
		} 
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
		}
		amsLogger.writeInfoLog("verifyBulkTransferEntries processResponse::"+processResponse);
		return processResponse;
	}
	// ---------verify Bulk Transfer Process  End---------------

	// -------- Authorize / Approve Bulk Transfer Process Start ------------- 
	@Override
	public ProcessResponse approveBulkTransfers(BulkTransfer bulkTransfer) 
	{
		bulkTransfer.setStrStatus("Pending");
		bulkTransfer.setStrIsVerified("Y");

		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String bulkMode = bulkTransfer.getStrBulkMode();
			amsLogger.writeInfoLog("approveBulkTransfer bulkMode=["+bulkMode+"]");
			
			if ("OTM".equalsIgnoreCase(bulkMode)) 
			{
				processResponse = approveOTMBulkTransferEntries(bulkTransfer);
			}
			else if ("MTO".equalsIgnoreCase(bulkMode)) 
			{
				processResponse = approveMTOBulkTransferEntries(bulkTransfer);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("No Bulk Mode found!!");
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	// -------- Authorize / Approve Bulk Transfer Process End -------------
	
	// -------- Reject Bulk Transfer Process Start -------------
	@Override
	public ProcessResponse rejectBulkTransfersWithReason(BulkTransfer bulkTransfer) 
	{
		bulkTransfer.setStrStatus("Pending");
		bulkTransfer.setStrIsVerified("Y");

		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			String bulkMode = bulkTransfer.getStrBulkMode();
			amsLogger.writeInfoLog("approveBulkTransfer bulkMode=["+bulkMode+"]");
			
			if ("OTM".equalsIgnoreCase(bulkMode)) 
			{
				processResponse = rejectOTMBulkTransferEntries(bulkTransfer);
			}
			else if ("MTO".equalsIgnoreCase(bulkMode)) 
			{
				processResponse = rejectMTOBulkTransferEntries(bulkTransfer);
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("No Bulk Mode found!!");
			}
		} 
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	
	}
	// -------- Reject Bulk Transfer Process End -------------
	
	// -------- Save Bulk Transfer Upload Data Start -------------
	@Override
	public ProcessResponse saveBulkTransferExcelData(BulkTransferDto bulkTransfer) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			processResponse.setCode("S0000");
			processResponse.setStatus("Success");
			processResponse.setMessage("Successfully saved all bulk transfer data.");
			
			List<BulkTransferExcelReqRes> strListBulkTransferExcelReqRes = bulkTransfer.getStrListBulkTransferExcelReqRes();
			
			List<BulkTransfer> bulkTransfers = getBulkTransferListData(strListBulkTransferExcelReqRes, bulkTransfer.getStrMakerId());
			if (bulkTransfers!=null && bulkTransfers.size() > 0) 
			{
				bulkTransferService.batchEntryOfBulkTransfer(bulkTransfers);
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setStatus("Failed");
			processResponse.setMessage("Internal Server Error occurs!!!");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	// -------- Save Bulk Transfer Upload Data End -------------
	
	private List<BulkTransfer> getBulkTransferListData(List<BulkTransferExcelReqRes> strListBulkTransferExcelReqRes, String makerId) 
	{
		List<BulkTransfer> bulkTransfersList = new ArrayList<BulkTransfer>();
		try 
		{
			String preTxnId = transactionIdCreationConfigDao.getPreTransactionId();
			
			for(BulkTransferExcelReqRes bulkTransferExcelReqRes : strListBulkTransferExcelReqRes) 
			{
				BulkTransfer bulkTransfer = new BulkTransfer();
				
				bulkTransfer.setStrPreTransactionId(preTxnId);
				
				bulkTransfer.setStrFromAccountNo(bulkTransferExcelReqRes.getStrFromAccountNo());
				bulkTransfer.setStrFromAccountType(bulkTransferExcelReqRes.getStrFromAccountType());
				bulkTransfer.setStrFromAccountName(bulkTransferExcelReqRes.getStrFromAccountName());
				
				bulkTransfer.setStrToAccountNo(bulkTransferExcelReqRes.getStrToAccountNo());
				bulkTransfer.setStrToAccountType(bulkTransferExcelReqRes.getStrToAccountType());
				bulkTransfer.setStrToAccountName(bulkTransferExcelReqRes.getStrToAccountName());
				
				bulkTransfer.setStrAmount(bulkTransferExcelReqRes.getStrAmount());
				bulkTransfer.setStrBulkMode(bulkTransferExcelReqRes.getStrBulkMode());
				
				if (makerId == null) 
				{
					bulkTransfer.setStrMakerId("System");
				}
				
				bulkTransfer.setStrMakerId(makerId);				
				
				bulkTransfer.setStrBulkRequestDate(Utils.getCurrentFormattedDate());
				bulkTransfer.setStrBulkRequestTime(Utils.getCurrentSqlTime());
				
				bulkTransfer.setStrStatus("pending");
				bulkTransfer.setStrIsVerified("N");
				bulkTransfer.setStrNarration(bulkTransferExcelReqRes.getStrNarration());
				bulkTransfersList.add(bulkTransfer);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return bulkTransfersList;
	}
	
	
	private void addBulkTrasnferOneToMany(BulkTransferDto bulkTransferDto) 
	{
		try
		{
			List<AccountCreation> updatableToAccountDataList = new ArrayList<AccountCreation>();
			List<BulkTransferToAccounts> bulkTrasnferToAccounts = bulkTransferDto.getBulkTransferToAccounts();

			String preTxnId = transactionIdCreationConfigDao.getPreTransactionId();
			
			String strFromAccountNo = bulkTransferDto.getStrFromAccountNo();
			String strFromAccountType = bulkTransferDto.getStrFromAccountType();
			
			AccountMaster fromAccountMaster = new AccountMaster();
			fromAccountMaster.setStrAccountNumber(strFromAccountNo);
			fromAccountMaster.setStrAccountType(strFromAccountType);
			
			fromAccountMaster = accountMasterService.getAccountDetails(fromAccountMaster);
			String strFromAccountName = fromAccountMaster.getStrAccountHolderName(); 
			
			if (bulkTransferDto.getStrIsVerified()!=null && bulkTransferDto.getStrIsVerified().trim().length() > 0) 
			{
				bulkTransferDto.setStrIsVerified(bulkTransferDto.getStrIsVerified().trim());
			}
			else 
			{
				bulkTransferDto.setStrIsVerified("N");
			}
			
			double fromTransferAmount = 0d;
			List<BulkTransfer> bulkTransfers = new ArrayList<BulkTransfer>();
			for (BulkTransferToAccounts bulkTransferToAccount : bulkTrasnferToAccounts) 
			{
				String strToAccountNo = bulkTransferToAccount.getStrToAccountNo();
				String strToAccountType = bulkTransferToAccount.getStrToAccountType();
				Double strTransactionAmount = bulkTransferToAccount.getStrTransactionAmount();

				BulkTransfer bulkTransfer = new BulkTransfer();
				bulkTransfer.setStrPreTransactionId(preTxnId);
				
				bulkTransfer.setStrAmount(strTransactionAmount);				
				bulkTransfer.setStrMakerId("6");
				
				bulkTransfer.setStrFromAccountNo(strFromAccountNo);
				bulkTransfer.setStrFromAccountType(strFromAccountType);
				bulkTransfer.setStrFromAccountName(strFromAccountName);
				
				AccountMaster toAccountMaster = new AccountMaster();
				toAccountMaster.setStrAccountNumber(strToAccountNo);
				toAccountMaster.setStrAccountType(strToAccountType);
				
				toAccountMaster = accountMasterService.getAccountDetails(toAccountMaster);
				String strToAccountName = toAccountMaster.getStrAccountHolderName(); 
				
				bulkTransfer.setStrToAccountNo(strToAccountNo);
				bulkTransfer.setStrToAccountType(strToAccountType);
				bulkTransfer.setStrToAccountName(strToAccountName);
				
				bulkTransfer.setStrMakerId(bulkTransferDto.getStrMakerId()); 
				
				bulkTransfer.setStrStatus("pending");
				bulkTransfer.setStrIsVerified(bulkTransferDto.getStrIsVerified().trim());
				bulkTransfer.setStrBulkMode("OTM");
				
				bulkTransfers.add(bulkTransfer);
				//get update list verified bulk transfer Data Start
				if ("Y".equalsIgnoreCase(bulkTransferDto.getStrIsVerified().trim())) 
				{
					String updatablePreCredAmount = accountMasterService.getUpdatablePreCredAmount(String.valueOf(toAccountMaster.getStrPreCredAmount()), String.valueOf(strTransactionAmount));
					if (updatablePreCredAmount!=null) 
					{
						AccountCreation toAccountCreation = new AccountCreation();
						toAccountCreation.setStrAccountNumber(toAccountMaster.getStrAccountNumber());
						toAccountCreation.setStrAccountType(toAccountMaster.getStrAccountType());
						toAccountCreation.setStrPreCredAmount(Double.parseDouble(updatablePreCredAmount));
						
						updatableToAccountDataList.add(toAccountCreation);
						
						fromTransferAmount = fromTransferAmount + strTransactionAmount;
					}
				}
				//get update list verified bulk transfer Data Start
			}
			//update verified bulk transfer Data Start
			if ("Y".equalsIgnoreCase(bulkTransferDto.getStrIsVerified().trim())) 
			{
				String updatableEarMarkAmount = accountMasterService.getUpdatableEarMarkAmount(Utils.decimalFormat.format(fromAccountMaster.getStrEarMarkAmount()), Utils.decimalFormat.format(fromTransferAmount));
				if (updatableEarMarkAmount!=null) 
				{
					AccountCreation fromAccountCreation = new AccountCreation();
					fromAccountCreation.setStrAccountNumber(fromAccountMaster.getStrAccountNumber());
					fromAccountCreation.setStrAccountType(fromAccountMaster.getStrAccountType());
					fromAccountCreation.setStrEarMarkAmount(Double.parseDouble(updatableEarMarkAmount));
					
					accountMasterService.updateEarMarkAmountOfCustomerAccount(fromAccountCreation);
					
					if (updatableToAccountDataList!=null && updatableToAccountDataList.size() > 0) 
					{
						accountMasterService.batchEntryOfPredCredUpdates(updatableToAccountDataList);
					}
				}
			}
			//update verified bulk transfer Data End
			
			bulkTransferService.batchEntryOfBulkTransfer(bulkTransfers);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void addbulkTransferManyToOne(BulkTransferDto bulkTransferDto) 
	{
		try 
		{
			List<BulkTransferFromAccounts> bulkTrasnferFromAccounts = bulkTransferDto.getBulkTransferFromAccounts();
			List<AccountCreation> updatableFromAccountDataList = new ArrayList<AccountCreation>();
			// common data
			String strToAccountNo = bulkTransferDto.getStrToAccountNo();
			String strToAccountType = bulkTransferDto.getStrToAccountType();
			
			AccountMaster toAccountMaster = new AccountMaster();
			toAccountMaster.setStrAccountNumber(strToAccountNo);
			toAccountMaster.setStrAccountType(strToAccountType);
			
			toAccountMaster = accountMasterService.getAccountDetails(toAccountMaster);
			String strToAccountName = toAccountMaster.getStrAccountHolderName(); 
			
			String preTxnId = transactionIdCreationConfigDao.getPreTransactionId();
			
			if (bulkTransferDto.getStrIsVerified()!=null && bulkTransferDto.getStrIsVerified().trim().length() > 0) 
			{
				bulkTransferDto.setStrIsVerified(bulkTransferDto.getStrIsVerified().trim());
			}
			else 
			{
				bulkTransferDto.setStrIsVerified("N");
			}

			double toTransferAmount = 0d;
			
			List<BulkTransfer> bulkTransfers = new ArrayList<BulkTransfer>();
			for (BulkTransferFromAccounts bulkTransferFromAccount : bulkTrasnferFromAccounts) 
			{
				String strCurrentFromAccountNo = bulkTransferFromAccount.getStrFromAccountNo();
				String strCurrentFromAccountType = bulkTransferFromAccount.getStrFromAccountType();
				Double strCurrentTransactionAmount = bulkTransferFromAccount.getStrTransactionAmount();

				BulkTransfer bulkTransfer = new BulkTransfer();
				bulkTransfer.setStrPreTransactionId(preTxnId);
				
				bulkTransfer.setStrAmount(strCurrentTransactionAmount);
				
				AccountMaster fromAccountMaster = new AccountMaster();
				fromAccountMaster.setStrAccountNumber(strCurrentFromAccountNo);
				fromAccountMaster.setStrAccountType(strCurrentFromAccountType);
				
				fromAccountMaster = accountMasterService.getAccountDetails(fromAccountMaster);
				String strFromAccountName = fromAccountMaster.getStrAccountHolderName();
				
				bulkTransfer.setStrFromAccountNo(strCurrentFromAccountNo);
				bulkTransfer.setStrFromAccountType(strCurrentFromAccountType);
				bulkTransfer.setStrFromAccountName(strFromAccountName);				
				
				bulkTransfer.setStrToAccountNo(strToAccountNo);
				bulkTransfer.setStrToAccountType(strToAccountType);
				bulkTransfer.setStrToAccountName(strToAccountName);
				
				bulkTransfer.setStrMakerId(bulkTransferDto.getStrMakerId()); 
				bulkTransfer.setStrStatus("Pending");
				
				bulkTransfer.setStrIsVerified(bulkTransferDto.getStrIsVerified().trim());
				bulkTransfer.setStrBulkMode("MTO");
				bulkTransfers.add(bulkTransfer);
				
				//get update list verified bulk transfer Data Start
				if ("Y".equalsIgnoreCase(bulkTransferDto.getStrIsVerified().trim())) 
				{
					String updatableEarMarkAmount = accountMasterService.getUpdatableEarMarkAmount(String.valueOf(fromAccountMaster.getStrEarMarkAmount()), String.valueOf(strCurrentTransactionAmount));
					if (updatableEarMarkAmount!=null)
					{
						AccountCreation fromAccountCreation = new AccountCreation();
						fromAccountCreation.setStrAccountNumber(fromAccountMaster.getStrAccountNumber());
						fromAccountCreation.setStrAccountType(fromAccountMaster.getStrAccountType());
						fromAccountCreation.setStrEarMarkAmount(Double.parseDouble(updatableEarMarkAmount));
						
						updatableFromAccountDataList.add(fromAccountCreation);
						
						toTransferAmount = toTransferAmount + strCurrentTransactionAmount;
					}
				}
				//get update list verified bulk transfer Data Start
			}
			//update verified bulk transfer Data Start
			if ("Y".equalsIgnoreCase(bulkTransferDto.getStrIsVerified().trim())) 
			{
				String updatablePreCredAmount = accountMasterService.getUpdatablePreCredAmount(Utils.decimalFormat.format(toAccountMaster.getStrPreCredAmount()), Utils.decimalFormat.format(toTransferAmount));
				if (updatablePreCredAmount!=null) 
				{
					AccountCreation toAccountCreation = new AccountCreation();
					toAccountCreation.setStrAccountNumber(toAccountMaster.getStrAccountNumber());
					toAccountCreation.setStrAccountType(toAccountMaster.getStrAccountType());					
					toAccountCreation.setStrPreCredAmount(Double.parseDouble(updatablePreCredAmount));
					
					accountMasterService.updatePreCredAmountOfCustomerAccount(toAccountCreation);
					
					if (updatableFromAccountDataList!=null && updatableFromAccountDataList.size() > 0) 
					{
						accountMasterService.batchEntryOfEarMarkUpdates(updatableFromAccountDataList);
					}
				}
			}	
			//update verified bulk transfer Data Start
			bulkTransferService.batchEntryOfBulkTransfer(bulkTransfers);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}		
	}

	public BulkTransfer mapBulkTransferDtoToBulkTransfer(BulkTransferDto bulkTransferDto) {
		BulkTransfer bulkTransfer = new BulkTransfer();
		bulkTransfer.setStrTransactionId(bulkTransferDto.getStrTransactionId());
		return bulkTransfer;
	}

	// this is get bulk Transfer Data by Transaction Id
	@Override
	public List<BulkTransfer> getBulkTransfer(BulkTransferDto bulkTransferDto) {
		BulkTransfer bulkTransfer = mapBulkTransferDtoToBulkTransfer(bulkTransferDto);
		 //using find All by example bulkTransferDao.findAllByExample(bulkTransfer);
		List<BulkTransfer> findByTransactionId = bulkTransferDao.findByTransactionId(bulkTransfer);
		return findByTransactionId;
	}

	@Override
	public List<BulkTransfer> findAll() {
		List<BulkTransfer> findAll = bulkTransferDao.findAll();
		BulkTransfer bulkTransfer = findAll.get(0);
		Double strAmount = bulkTransfer.getStrAmount();
		System.out.println(strAmount);
		return bulkTransferDao.findAll();
	}

	// for generating pre Transaction Serial Id
	public String generatePreTransactionSerialId()
	{
		List<BulkTransfer> findOneByPreTransactionSerialId = bulkTransferDao.findOneByPreTransactionSerialId();
		if (findOneByPreTransactionSerialId != null && findOneByPreTransactionSerialId.size() > 0) 
		{
			String strPreTransactionId = findOneByPreTransactionSerialId.get(0).getStrPreTransactionId();
			Integer preTransactionId = Integer.valueOf(strPreTransactionId);
			Integer newPreTransactionId = preTransactionId + 1;
			String strTransactionId = String.valueOf(newPreTransactionId);
			return strTransactionId;
		} 
		else 
		{
			Integer initialValue = 00000000000001;
			String strInitialValue = String.valueOf(initialValue);
			return strInitialValue;
		}
	}

	@Override
	public List<BulkTransfer> findAllByPreTransactionId(BulkTransferDto bulkTransferDto) {

		try {
			BulkTransfer bulkTransfer = new BulkTransfer();
			bulkTransfer.setStrPreTransactionId(bulkTransferDto.getStrPreTransactionId());
			bulkTransfer.setStrStatus("Pending");
			
			List<BulkTransfer> findAllByPreTransactionId = bulkTransferDao.findAllByPreTransactionId(bulkTransfer);
			return findAllByPreTransactionId;
		} catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return null;
	}

	@Override
	public List<BulkTransfer> getPreTransactionIdByTxnAmount(BulkTransfer bulkTransfer)
	{
		return bulkTransferDao.getPreTransactionIdByTxnAmount(bulkTransfer);
	}

	@Override
	public List<BulkTransfer> getAuthorizeBlukTransferList(BulkTransfer bulkTransfer) 
	{
		return bulkTransferDao.getAuthorizeBlukTransferList(bulkTransfer);
	}
	
	private ProcessResponse verifyBulkTransferOTMEntries(BulkTransfer bulkTransfer) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		//processResponse.setMessage("Sucessfully Verified the Bulk Entries.");
		
		double fromTransferAmount = 0d;		
		String strFromAccountNumber = null;
		
		List<BulkTransfer> updatableBulkTransferList = new ArrayList<BulkTransfer>();
		List<BulkTransfer> updatableRejectedBulkTransferList = new ArrayList<BulkTransfer>();
		List<AccountCreation> updatableToAccountDataList = new ArrayList<AccountCreation>();
		
		try
		{
			String preTxnId = bulkTransfer.getStrPreTransactionId();
			bulkTransfer.setStrStatus("pending");
			
			String makerId = bulkTransfer.getStrMakerId();
			amsLogger.writeInfoLog("verifyBulkTransferOTMEntries makerId=["+makerId+"]");
			
			amsLogger.writeInfoLog("verifyBulkTransferOTMEntries bulkTransfer=["+bulkTransfer+"]");
			List<CustomerByAccountResponse> customerAccountList = getToAccountBulkDataList(bulkTransfer);			
			if (customerAccountList!=null && customerAccountList.size() > 0)
			{
				CustomerByAccountResponse fromCustomerByAccountResp = customerAccountList.get(0);
				strFromAccountNumber = fromCustomerByAccountResp.getStrFromAccountNumber();
				
				AccountCreation accountCreation = new AccountCreation();
				accountCreation.setStrAccountNumber(strFromAccountNumber);
				accountCreation.setStrAccountType(fromCustomerByAccountResp.getStrFromAccountType());
				
				Double fromAccountEarMarkAmount = 0d;
				CustomerByAccountResponse customerByAccountRes = getAccountInfoByAccountNo(accountCreation);	
				if (customerByAccountRes!=null) 
				{
					fromAccountEarMarkAmount = customerByAccountRes.getStrEarMarkAmount();
					
					customerByAccountRes.setTransferAmount(bulkTransfer.getBulkTransferAmount());
					processResponse = validateFromAccounts(customerByAccountRes);
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						//For each loop Start----------------------------------------------------------------------------
						for(CustomerByAccountResponse customerByAccountResponse: customerAccountList) 
						{
							boolean isAccountAlreadyUsed = false;
							String strToAccountNo = customerByAccountResponse.getStrAccountNumber(); 
							
							BulkTransfer bulkTransferInstance = new BulkTransfer();
							bulkTransferInstance.setStrMakerId(makerId);
							
							if (strFromAccountNumber.equalsIgnoreCase(strToAccountNo)) 
							{
								processResponse.setCode("E0000");
								processResponse.setMessage("From Account No and To Account No Can not be Same!!"); 
								isAccountAlreadyUsed = true;
							}
							if (!isAccountAlreadyUsed) 
							{
								processResponse = validateToAccounts(customerByAccountResponse);
							}
							
							if("S0000".equalsIgnoreCase(processResponse.getCode())) 
							{
								bulkTransferInstance.setStrPreTransactionId(preTxnId);
								bulkTransferInstance.setStrToAccountNo(strToAccountNo);
								updatableBulkTransferList.add(bulkTransferInstance);
								
								String toAccountPreCredAmount = customerByAccountResponse.getStrPreCredAmount();
								amsLogger.writeInfoLog("To_Account Number["+strToAccountNo +"] To_Account PreCredAmount::["+toAccountPreCredAmount+"]");
								
								String toAccountTransferAmount = customerByAccountResponse.getTransferAmount();
								amsLogger.writeInfoLog("To_Account Number["+strToAccountNo +"] To_Account TransferAmount::["+toAccountTransferAmount+"]");
								
								String updatablePreCredAmount = accountMasterService.getUpdatablePreCredAmount(toAccountPreCredAmount, toAccountTransferAmount);
								amsLogger.writeInfoLog("To_Account Number["+strToAccountNo +"] To_Account updatablePreCredAmount::["+updatablePreCredAmount+"]");

								if (updatablePreCredAmount!=null) 
								{
									AccountCreation toAccountCreation = new AccountCreation();
									toAccountCreation.setStrAccountNumber(strToAccountNo);
									toAccountCreation.setStrAccountType(customerByAccountResponse.getStrAccountType());
									toAccountCreation.setStrPreCredAmount(Double.parseDouble(updatablePreCredAmount));
									
									updatableToAccountDataList.add(toAccountCreation);
									
									fromTransferAmount = fromTransferAmount + Double.parseDouble(toAccountTransferAmount);
								}
							}
							else 
							{
								bulkTransferInstance.setStrRejectedReason(processResponse.getMessage());
								bulkTransferInstance.setStrStatus("rejected");
								bulkTransferInstance.setStrPreTransactionId(preTxnId);
								bulkTransferInstance.setStrToAccountNo(customerByAccountResponse.getStrAccountNumber());
								bulkTransferInstance.setStrMakerId(makerId);
								updatableRejectedBulkTransferList.add(bulkTransferInstance);
							}
						}
						//For each loop End----------------------------------------------------------------------------
						
						//Updating Records --------------------------------------- START
						if (updatableBulkTransferList!=null && updatableBulkTransferList.size() > 0) 
						{
							bulkTransferService.batchEntryOfToAccountUpdateBulkTransfer(updatableBulkTransferList);
						}
						if (updatableRejectedBulkTransferList!=null && updatableRejectedBulkTransferList.size() > 0) 
						{
							bulkTransferService.batchEntryOfRjectedToAccountBulkTransfer(updatableRejectedBulkTransferList);
						}
						
						String updatableEarMarkAmount = accountMasterService.getUpdatableEarMarkAmount(Utils.decimalFormat.format(fromAccountEarMarkAmount), Utils.decimalFormat.format(fromTransferAmount));
						amsLogger.writeInfoLog("From Account Number=["+strFromAccountNumber+"] From Account No updatableEarMarkAmount=["+updatableEarMarkAmount+"]");
						if (updatableEarMarkAmount != null) 
						{
							accountCreation.setStrEarMarkAmount(Double.parseDouble(updatableEarMarkAmount));
							accountMasterService.updateEarMarkAmountOfCustomerAccount(accountCreation);
							
							if (updatableToAccountDataList != null && updatableToAccountDataList.size() > 0) 
							{
								accountMasterService.batchEntryOfPredCredUpdates(updatableToAccountDataList);
							}
						}
						//Updating Records --------------------------------------- END
					}
					else
					{
						bulkTransfer.setStrRejectedReason(processResponse.getMessage());
						bulkTransfer.setStrStatus("rejected");
						bulkTransfer.setStrPreTransactionId(preTxnId);
						bulkTransferService.updateRejectReason(bulkTransfer);
					}
				}
				else 
				{
					bulkTransfer.setStrRejectedReason("No From Account Found!!");
					bulkTransfer.setStrStatus("rejected");
					bulkTransfer.setStrPreTransactionId(preTxnId);
					bulkTransferService.updateRejectReason(bulkTransfer);
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("Issue occurs while retrieving data!!");
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("--->> verifyBulkTransferOTMEntries processResponse::"+processResponse);
		if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
		{
			processResponse.setMessage("Sucessfully Verified the Bulk Entries.");
		}
		return processResponse;
	}

	private ProcessResponse verifyBulkTransferMTOEntries(BulkTransfer bulkTransfer) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		//processResponse.setMessage("Sucessfully Verified the Bulk Entries");
		
		double toTransferAmount = 0d;
		String strToAccountNumber = null;
		
		List<BulkTransfer> updatableBulkTransferList = new ArrayList<BulkTransfer>();
		List<BulkTransfer> updatableRejectedBulkTransferList = new ArrayList<BulkTransfer>();		
		List<AccountCreation> updatableFromAccountDataList = new ArrayList<AccountCreation>();
		
		try
		{
			String preTxnId = bulkTransfer.getStrPreTransactionId();
			bulkTransfer.setStrStatus("pending");
			
			List<CustomerByAccountResponse> customerAccountList = getFromAccountBulkDataList(bulkTransfer);			
			if (customerAccountList!=null && customerAccountList.size() > 0)
			{
				CustomerByAccountResponse toCustomerByAccountResp = customerAccountList.get(0);
				strToAccountNumber = toCustomerByAccountResp.getStrToAccountNumber();
				
				AccountCreation accountCreation = new AccountCreation();
				accountCreation.setStrAccountNumber(strToAccountNumber);
				accountCreation.setStrAccountType(toCustomerByAccountResp.getStrToAccountType());
				
				CustomerByAccountResponse customerByAccountRes = getAccountInfoByAccountNo(accountCreation);				
				if (customerByAccountRes!=null) 
				{
					customerByAccountRes.setTransferAmount(bulkTransfer.getBulkTransferAmount());
					processResponse = validateToAccounts(customerByAccountRes);//<--- Verify Single To Account
					if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
					{
						//For each loop Start----------------------------------------------------------------------------
						for(CustomerByAccountResponse customerByAccountResponse: customerAccountList) 
						{
							boolean isAccountAlreadyUsed = false;
							String strFromAccountNumber = customerByAccountResponse.getStrAccountNumber(); 
							
							BulkTransfer bulkTransferInstance = new BulkTransfer();
							if (strFromAccountNumber.equalsIgnoreCase(strToAccountNumber)) 
							{
								processResponse.setCode("E0000");
								processResponse.setMessage("From Account No and To Account No Can not be Same!!"); 
								isAccountAlreadyUsed = true;
							}
							if (!isAccountAlreadyUsed) 
							{
								processResponse = validateFromAccounts(customerByAccountResponse); //<--- Verify Multiple From Accounts
							}
							
							if("S0000".equalsIgnoreCase(processResponse.getCode())) 
							{
								bulkTransferInstance.setStrPreTransactionId(preTxnId);
								bulkTransferInstance.setStrFromAccountNo(strFromAccountNumber);
								updatableBulkTransferList.add(bulkTransferInstance);
								
								String updatableEarMarkAmount = accountMasterService.getUpdatableEarMarkAmount(String.valueOf(customerByAccountResponse.getStrEarMarkAmount()), customerByAccountResponse.getTransferAmount());

								if (updatableEarMarkAmount!=null)
								{
									AccountCreation fromAccountCreation = new AccountCreation();
									fromAccountCreation.setStrAccountNumber(customerByAccountResponse.getStrAccountNumber());
									fromAccountCreation.setStrAccountType(customerByAccountResponse.getStrAccountType());
									fromAccountCreation.setStrEarMarkAmount(Double.parseDouble(updatableEarMarkAmount));
									updatableFromAccountDataList.add(fromAccountCreation);
									
									toTransferAmount = toTransferAmount + Double.parseDouble(customerByAccountResponse.getTransferAmount());
								}
							}
							else 
							{
								bulkTransferInstance.setStrRejectedReason(processResponse.getMessage());
								bulkTransferInstance.setStrStatus("rejected");
								bulkTransferInstance.setStrPreTransactionId(preTxnId);
								bulkTransferInstance.setStrFromAccountNo(customerByAccountResponse.getStrAccountNumber());
								updatableRejectedBulkTransferList.add(bulkTransferInstance);
							}
						}
						//For each loop Start----------------------------------------------------------------------------
						
						//Updating Records --------------------------------------- START
						if (updatableBulkTransferList!=null && updatableBulkTransferList.size() > 0) 
						{
							bulkTransferService.batchEntryOfFromAccountUpdateBulkTransfer(updatableBulkTransferList);
						}
						if (updatableRejectedBulkTransferList!=null && updatableRejectedBulkTransferList.size() > 0) 
						{
							bulkTransferService.batchEntryOfRjectedFromAccountBulkTransfer(updatableRejectedBulkTransferList);
						}
						
						String updatablePreCredAmount = accountMasterService.getUpdatablePreCredAmount(Utils.decimalFormat.format(customerByAccountRes.getStrPreCredAmount()), Utils.decimalFormat.format(toTransferAmount));
						if (updatablePreCredAmount!=null) 
						{
							accountCreation.setStrPreCredAmount(Double.parseDouble(updatablePreCredAmount));
							accountMasterService.updatePreCredAmountOfCustomerAccount(accountCreation);
							
							if (updatableFromAccountDataList!=null && updatableFromAccountDataList.size() > 0) 
							{
								accountMasterService.batchEntryOfEarMarkUpdates(updatableFromAccountDataList);
							}
						}
						//Updating Records --------------------------------------- End
					}
					else
					{
						bulkTransfer.setStrRejectedReason(processResponse.getMessage());
						bulkTransfer.setStrStatus("rejected");
						bulkTransfer.setStrPreTransactionId(preTxnId);
						bulkTransferService.updateRejectReason(bulkTransfer);
					}
				}
				else
				{
					bulkTransfer.setStrRejectedReason("No To Account Found!!");
					bulkTransfer.setStrStatus("rejected");
					bulkTransfer.setStrPreTransactionId(preTxnId);
					bulkTransferService.updateRejectReason(bulkTransfer);
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("Issue occurs while retrieving data");
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("****>>>> verifyBulkTransferMTOEntries processResponse::"+processResponse);
		if ("S0000".equalsIgnoreCase(processResponse.getCode())) 
		{
			processResponse.setMessage("Sucessfully Verified the Bulk Entries.");
		}
		return processResponse;
	}
	
	private ProcessResponse rejectOTMBulkTransferEntries(BulkTransfer bulkTransfer) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			List<BulkTransfer> updatableBulkTransferDataList =  new ArrayList<BulkTransfer>();
			List<AccountCreation> updatableToAccountMasterList = new ArrayList<AccountCreation>();
			List<AccountCreation> updatableFromAccountMasterList = new ArrayList<AccountCreation>();

			List<AccountTranMaster> insertionsToAccountTranMasterList =  new ArrayList<AccountTranMaster>();
			
			List<CustomerByAccountResponse> customerAccountList = getToAccountBulkDataList(bulkTransfer);
			if (customerAccountList!=null && customerAccountList.size() > 0)
			{
				amsLogger.writeInfoLog("rejectOTMBulkTransferEntries customerAccountList size=["+customerAccountList.size()+"]");
				CustomerByAccountResponse fromCustomerByAccountResp = customerAccountList.get(0);
				String strFromAccountNumber = fromCustomerByAccountResp.getStrFromAccountNumber();
				
				AccountCreation fromAccountCreation = new AccountCreation();
				fromAccountCreation.setStrAccountNumber(strFromAccountNumber);
				fromAccountCreation.setStrAccountType(fromCustomerByAccountResp.getStrFromAccountType());
				
				CustomerByAccountResponse fromCustomerAccountInfo = getAccountInfoByAccountNo(fromAccountCreation);				
				if (fromCustomerAccountInfo!=null) 
				{
					String updatableFromAcEarAmount = Utils.getReduceAmount(String.valueOf(fromCustomerAccountInfo.getStrEarMarkAmount()), bulkTransfer.getBulkTransferAmount());
					fromAccountCreation.setStrEarMarkAmount(Double.parseDouble(updatableFromAcEarAmount));
					updatableFromAccountMasterList.add(fromAccountCreation);	//<---------FROM A/C 1 List----------------->
					
					String preTxnId = bulkTransfer.getStrPreTransactionId();
					String tranId = transactionIdCreationConfigDao.getTransactionId();
					amsLogger.writeInfoLog("rejectOTMBulkTransferEntries tranId::["+tranId+"]");
					
					for(CustomerByAccountResponse customerAccountResp: customerAccountList) 
					{
						customerAccountResp.setTxnId(tranId);
						String toAccountNumber = customerAccountResp.getStrAccountNumber();
						
						String updatablePreCredAmount = Utils.getReduceAmount(customerAccountResp.getStrPreCredAmount(), customerAccountResp.getTransferAmount());
						
						AccountCreation toAccountCreationData = new AccountCreation();						
						toAccountCreationData.setStrAccountNumber(toAccountNumber);
						toAccountCreationData.setStrAccountType(customerAccountResp.getStrAccountType());
						toAccountCreationData.setStrPreCredAmount(Double.parseDouble(updatablePreCredAmount));						
						updatableToAccountMasterList.add(toAccountCreationData);
						
						customerAccountResp.setStrFromAccountTran(strFromAccountNumber);
						customerAccountResp.setStrToAccountTran(toAccountNumber);
						customerAccountResp.setResponseCode("06");
						
						AccountTranMaster accountTranMaster = getAccountTranMasterEntry(customerAccountResp);
						insertionsToAccountTranMasterList.add(accountTranMaster);//<---------2 List----------------->
						
						BulkTransfer updatableBulkTrsfr = new BulkTransfer();
						updatableBulkTrsfr.setStrPreTransactionId(preTxnId);
						updatableBulkTrsfr.setStrTransactionId(tranId);
						updatableBulkTrsfr.setStrCheckerId(bulkTransfer.getStrCheckerId());						
						updatableBulkTrsfr.setStrFromAccountNo(strFromAccountNumber);
						updatableBulkTrsfr.setStrToAccountNo(toAccountNumber);
						updatableBulkTrsfr.setStrStatus("rejected");
						updatableBulkTrsfr.setStrRejectedReason(bulkTransfer.getStrRejectedReason()); 
						
						updatableBulkTransferDataList.add(updatableBulkTrsfr);
						//-- Final Bulk Transfer Updatable List Start
					}
					
					if (updatableFromAccountMasterList!=null && updatableFromAccountMasterList.size() > 0) 
					{
						accountMasterService.batchEntryOfEarMarkUpdates(updatableFromAccountMasterList);
					}
					if (updatableToAccountMasterList!=null && updatableToAccountMasterList.size() > 0) 
					{
						accountMasterService.batchEntryOfPredCredUpdates(updatableToAccountMasterList);
					}
					
					insertAccountTranMasterEntries(processResponse, insertionsToAccountTranMasterList);
					
					updateRejectBulkTransferListData(processResponse, updatableBulkTransferDataList);
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	private ProcessResponse rejectMTOBulkTransferEntries(BulkTransfer bulkTransfer) 
	{

		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			List<BulkTransfer> updatableBulkTransferDataList =  new ArrayList<BulkTransfer>();
			List<AccountCreation> updatableToAccountMasterList = new ArrayList<AccountCreation>();
			List<AccountCreation> updatableFromAccountMasterList = new ArrayList<AccountCreation>();

			List<AccountTranMaster> insertionsToAccountTranMasterList =  new ArrayList<AccountTranMaster>();
			
			List<CustomerByAccountResponse> customerAccountList = getToAccountBulkDataList(bulkTransfer);
			if (customerAccountList!=null && customerAccountList.size() > 0)
			{
				amsLogger.writeInfoLog("rejectMTOBulkTransferEntries customerAccountList size=["+customerAccountList.size()+"]");
				CustomerByAccountResponse fromCustomerByAccountResp = customerAccountList.get(0);
				String toAccountNumber = fromCustomerByAccountResp.getStrToAccountNumber();
				
				AccountCreation toAccountCreationData = new AccountCreation();
				
				toAccountCreationData.setStrAccountNumber(toAccountNumber);
				toAccountCreationData.setStrAccountType(fromCustomerByAccountResp.getStrToAccountType());
				
				CustomerByAccountResponse toCustomerAccountInfo = getAccountInfoByAccountNo(toAccountCreationData);				
				if (toCustomerAccountInfo!=null) 
				{
					String updatablePreCredAmount = Utils.getReduceAmount(toCustomerAccountInfo.getStrPreCredAmount(), bulkTransfer.getBulkTransferAmount());					
					toAccountCreationData.setStrPreCredAmount(Double.parseDouble(updatablePreCredAmount));					
					updatableToAccountMasterList.add(toAccountCreationData);				
					
					String preTxnId = bulkTransfer.getStrPreTransactionId();
					String tranId = transactionIdCreationConfigDao.getTransactionId();
					amsLogger.writeInfoLog("rejectMTOBulkTransferEntries tranId::["+tranId+"]");
					
					for(CustomerByAccountResponse customerAccountResp: customerAccountList) 
					{
						customerAccountResp.setTxnId(tranId);
						String strFromAccountNumber = customerAccountResp.getStrAccountNumber();
						
						String updatableEarMarkAmount = Utils.getReduceAmount(String.valueOf(customerAccountResp.getStrEarMarkAmount()), customerAccountResp.getTransferAmount());
						
						AccountCreation fromAccountCreation = new AccountCreation();
						fromAccountCreation.setStrAccountNumber(strFromAccountNumber);
						fromAccountCreation.setStrAccountType(customerAccountResp.getStrAccountType());						
						fromAccountCreation.setStrEarMarkAmount(Double.parseDouble(updatableEarMarkAmount));
						
						updatableFromAccountMasterList.add(fromAccountCreation);	//<---------FROM A/C 1 List----------------->
						
						customerAccountResp.setStrFromAccountTran(strFromAccountNumber);
						customerAccountResp.setStrToAccountTran(toAccountNumber);
						customerAccountResp.setResponseCode("06");
						
						AccountTranMaster accountTranMaster = getAccountTranMasterEntry(customerAccountResp);
						insertionsToAccountTranMasterList.add(accountTranMaster);//<---------2 List----------------->
						
						BulkTransfer updatableBulkTrsfr = new BulkTransfer();
						updatableBulkTrsfr.setStrPreTransactionId(preTxnId);
						updatableBulkTrsfr.setStrTransactionId(tranId);
						updatableBulkTrsfr.setStrCheckerId(bulkTransfer.getStrCheckerId());						
						updatableBulkTrsfr.setStrFromAccountNo(strFromAccountNumber);
						updatableBulkTrsfr.setStrToAccountNo(toAccountNumber);
						updatableBulkTrsfr.setStrStatus("rejected");
						updatableBulkTrsfr.setStrRejectedReason(bulkTransfer.getStrRejectedReason());
						
						updatableBulkTransferDataList.add(updatableBulkTrsfr);
					}
					
					if (updatableFromAccountMasterList!=null && updatableFromAccountMasterList.size() > 0) 
					{
						accountMasterService.batchEntryOfEarMarkUpdates(updatableFromAccountMasterList);
					}
					if (updatableToAccountMasterList!=null && updatableToAccountMasterList.size() > 0) 
					{
						accountMasterService.batchEntryOfPredCredUpdates(updatableToAccountMasterList);
					}
					
					insertAccountTranMasterEntries(processResponse, insertionsToAccountTranMasterList);
					
					updateRejectBulkTransferListData(processResponse, updatableBulkTransferDataList);
				}
			}
			
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private ProcessResponse approveOTMBulkTransferEntries(BulkTransfer bulkTransfer) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		double trnsfrAmount = 0d;
		try 
		{
			processResponse.setCode("S0000");
			processResponse.setMessage("Sucessfully Approved the Bulk Entries");
			
			List<AccountCreation> updatableFromAccountMasterList = new ArrayList<AccountCreation>();
			List<GLAccountTypeMaster> updatableFromGLAccountTypeMasterList = new ArrayList<GLAccountTypeMaster>();
			
			List<AccountStatement> insertionsFromAccountStatementList = new ArrayList<AccountStatement>();
			List<GLAccountStatement> insertionsFromGLAccountStatementList = new ArrayList<GLAccountStatement>();
			
			List<AccountCreation> updatableToAccountMasterList = new ArrayList<AccountCreation>();
			List<GLAccountTypeMaster> updatableToGLAccountTypeMasterList = new ArrayList<GLAccountTypeMaster>();
			
			List<AccountStatement> insertionsToAccountStatementList = new ArrayList<AccountStatement>();
			List<GLAccountStatement> insertionsToGLAccountStatementList = new ArrayList<GLAccountStatement>();
			
			List<AccountTranMaster> insertionsToAccountTranMasterList =  new ArrayList<AccountTranMaster>();		
			
			List<BulkTransfer> updatableBulkTransferDataList =  new ArrayList<BulkTransfer>();
			
			bulkTransfer.setStrStatus("pending");
			String preTxnId = bulkTransfer.getStrPreTransactionId();
			
			List<CustomerByAccountResponse> customerAccountList = getToAccountBulkDataList(bulkTransfer);			
			
			if (customerAccountList!=null && customerAccountList.size() > 0)
			{
				CustomerByAccountResponse fromCustomerByAccountResp = customerAccountList.get(0);
				String strFromAccountNumber = fromCustomerByAccountResp.getStrFromAccountNumber();
				String strFromAccountType = fromCustomerByAccountResp.getStrFromAccountType();
				amsLogger.writeInfoLog("Inside approveOTMBulkTransferEntries strFromAccountNumber=["+strFromAccountNumber+"] strFromAccountType=["+strFromAccountType+"]");
				
				AccountCreation fromAccountCreation = new AccountCreation();
				fromAccountCreation.setStrAccountNumber(strFromAccountNumber);
				fromAccountCreation.setStrAccountType(strFromAccountType);
				
				CustomerByAccountResponse fromCustomerAccountInfo = getAccountInfoByAccountNo(fromAccountCreation);				
				if (fromCustomerAccountInfo!=null) 
				{
					String fromAcEarAmount = String.valueOf(fromCustomerAccountInfo.getStrEarMarkAmount());
					String fromAcClosingBal = fromCustomerAccountInfo.getStrClosingBalance();
					
					String fromAcLinkedGLBalance = fromCustomerAccountInfo.getGlAccountBalance();
					String fromAcLinkedGLAccNo = fromCustomerAccountInfo.getGlAccountNo().trim();
					String fromAcLinkedGLAccType = fromCustomerAccountInfo.getGlAccountType().trim();
					
					amsLogger.writeInfoLog("Inside approveOTMBulkTransferEntries fromAcEarAmount=["+fromAcEarAmount+"] fromAcClosingBal=["+fromAcClosingBal+"]");
					amsLogger.writeInfoLog("Inside approveOTMBulkTransferEntries fromAcLinkedGLBalance=["+fromAcLinkedGLBalance+"] fromAcLinkedGLAccNo=["+fromAcLinkedGLAccNo+"]fromAcLinkedGLAccType=["+fromAcLinkedGLAccType+"]");
					for(CustomerByAccountResponse customerAccountResp: customerAccountList) 
					{
						String tranId = transactionIdCreationConfigDao.getTransactionId();
						amsLogger.writeInfoLog("Inside approveOTMBulkTransferEntries tranId::["+tranId+"] preTxnId=["+preTxnId+"]");
						
						customerAccountResp.setTxnId(tranId);
						
						String custTrnsfrAmount = customerAccountResp.getTransferAmount();
						amsLogger.writeInfoLog("approveOTMBulkTransferEntries transfer Amount::["+custTrnsfrAmount+"]");
						
						//From A/C Start--------------------------------------------------------------------------------------------------
						AccountCreation fromAccountCreationData = new AccountCreation();
						
						amsLogger.writeInfoLog("Inside For Loop strFromAccountNumber=["+strFromAccountNumber+"]strFromAccountType=["+strFromAccountType+"]");
						fromAccountCreationData.setStrAccountNumber(strFromAccountNumber);
						fromAccountCreationData.setStrAccountType(strFromAccountType);
						
						amsLogger.writeInfoLog("Inside For Loop Before fromAcEarAmount=["+fromAcEarAmount+"] fromAcClosingBal=["+fromAcClosingBal+"]");
						fromAcEarAmount = Utils.getReduceAmount(fromAcEarAmount, custTrnsfrAmount);						
						fromAcClosingBal =  Utils.getReduceAmount(fromAcClosingBal, custTrnsfrAmount);
						amsLogger.writeInfoLog("Inside For Loop Before fromAcClosingBal=["+fromAcClosingBal+"] fromAcEarAmount=["+fromAcEarAmount+"]");
						
						fromAccountCreationData.setStrEarMarkAmount(Double.parseDouble(fromAcEarAmount));
						fromAccountCreationData.setStrClosingBalance(fromAcClosingBal);
						
						//<---------FROM A/C 1 List----------------->	Start
						updatableFromAccountMasterList.add(fromAccountCreationData);
						//<---------FROM A/C 1 List----------------->	End
						
						amsLogger.writeInfoLog("Inside For Loop Before fromAcLinkedGLBalance=["+fromAcLinkedGLBalance+"]");
						fromAcLinkedGLBalance = Utils.getReduceAmount(fromAcLinkedGLBalance, custTrnsfrAmount);
						
						GLAccountTypeMaster fromGLAccountTypeMaster = new GLAccountTypeMaster();						
						fromGLAccountTypeMaster.setStrAccountNumber(fromAcLinkedGLAccNo);
						fromGLAccountTypeMaster.setStrGLAccountType(fromAcLinkedGLAccType);						
						
						amsLogger.writeInfoLog("Inside For Loop After fromAcLinkedGLBalance=["+fromAcLinkedGLBalance+"]");
						fromGLAccountTypeMaster.setStrClosingBalance(fromAcLinkedGLBalance);
						
						//<---------FROM A/C 2 List-----------------> Start
						updatableFromGLAccountTypeMasterList.add(fromGLAccountTypeMaster);
						//<---------FROM A/C 2 List-----------------> End
						
						fromCustomerAccountInfo.setStrClosingBalance(fromAcClosingBal);
						fromCustomerAccountInfo.setTransferAmount(custTrnsfrAmount);
						fromCustomerAccountInfo.setTxnId(tranId);
						
						AccountStatement fromAccountStatement = getDebitAccountStatementEntry(fromCustomerAccountInfo);
						
						//<---------FROM A/C 3 List-----------------> Start
						insertionsFromAccountStatementList.add(fromAccountStatement);
						//<---------FROM A/C 3 List-----------------> End
						
						fromCustomerAccountInfo.setGlAccountAvailableBalance(fromAcLinkedGLBalance);
						GLAccountStatement fromGLAccountStatement = getDebitGLAccountStatement(fromCustomerAccountInfo);
						
						//<---------FROM A/C 4 List-----------------> Start
						insertionsFromGLAccountStatementList.add(fromGLAccountStatement);
						//<---------FROM A/C 4 List-----------------> End
						//From A/C End --------------------------------------------------------------------------------------------------
						
						String toAccountNumber = customerAccountResp.getStrAccountNumber();
						
						AccountCreation toAccountCreation = getToAccountCreationData(customerAccountResp);
						//<---------1 List-----------------> Start
						updatableToAccountMasterList.add(toAccountCreation);
						//<---------1 List-----------------> End
						
						//-------Account Tran Master Entry Start
						customerAccountResp.setStrFromAccountTran(strFromAccountNumber);
						customerAccountResp.setStrToAccountTran(toAccountNumber);
						customerAccountResp.setResponseCode("00");
						AccountTranMaster accountTranMaster = getAccountTranMasterEntry(customerAccountResp);
						insertionsToAccountTranMasterList.add(accountTranMaster);//<---------2 List----------------->
						//-------Account Tran Master Entry End
						
						//------Linked GL Account-------Start						
						GLAccountTypeMaster glAccountTypeMaster = getToLinkedGLaccouAccountTypeMaster(customerAccountResp, updatableToGLAccountTypeMasterList); 
						//------Linked GL Account-------End
						
						//-------Account Statement Entry Start
						customerAccountResp.setStrAvailableBalance(toAccountCreation.getStrClosingBalance());
						AccountStatement accountStatement = getCreditAccountStatementEntry(customerAccountResp);
						insertionsToAccountStatementList.add(accountStatement);//<---------4 List----------------->
						//-------Account Statement Entry End
						
						//----- GLAccount Statement Start
						customerAccountResp.setGlAccountAvailableBalance(glAccountTypeMaster.getStrClosingBalance());
						GLAccountStatement glAccountStatement = getCreditGLAccountStatement(customerAccountResp);
						insertionsToGLAccountStatementList.add(glAccountStatement);//<---------5 List----------------->
						//----- GLAccount Statement End
						
						//-- Final Bulk Transfer Updatable List Start
						BulkTransfer updatableBulkTrsfr = new BulkTransfer();
						updatableBulkTrsfr.setStrTransactionId(tranId);
						updatableBulkTrsfr.setStrCheckerId(bulkTransfer.getStrCheckerId());
						
						updatableBulkTrsfr.setStrFromAccountNo(strFromAccountNumber);
						updatableBulkTrsfr.setStrToAccountNo(toAccountNumber);
						updatableBulkTrsfr.setStrPreTransactionId(preTxnId);
						updatableBulkTrsfr.setStrStatus("approved");
						
						amsLogger.writeInfoLog("approveOTMBulkTransferEntries updatableBulkTrsfr::["+updatableBulkTrsfr+"]");
						updatableBulkTransferDataList.add(updatableBulkTrsfr);//<---------6 List----------------->
						//-- Final Bulk Transfer Updatable List Start
						
						trnsfrAmount = trnsfrAmount + Double.parseDouble(customerAccountResp.getTransferAmount());
					}
					amsLogger.writeInfoLog(">>>approveOTMBulkTransferEntries transfer Amount::["+trnsfrAmount+"]");
					String fromAccountTransferAmount = String.valueOf(trnsfrAmount);
					fromCustomerAccountInfo.setTransferAmount(fromAccountTransferAmount);
					
					amsLogger.writeInfoLog(">>>approveOTMBulkTransferEntries fromCustomerAccountInfo::["+fromCustomerAccountInfo+"]");
					
					//From A/C  Entry Start					
					updateFromAccountListData(processResponse, updatableFromAccountMasterList, updatableFromGLAccountTypeMasterList);	
					updateSinglefromAccountLimit(processResponse, fromCustomerAccountInfo);
					
					insertFromAccountStatementWithGL(processResponse, insertionsFromAccountStatementList, insertionsFromGLAccountStatementList);
					//From A/C  Entry End
					
					//To A/C Entries Start
					updateToAccountListData(processResponse, updatableToAccountMasterList, updatableToGLAccountTypeMasterList);					
					
					insertToAccountStatementWithGL(processResponse, insertionsToAccountStatementList, insertionsToGLAccountStatementList);					
					//To A/C Entries End
					
					//Tran Master Entry Start
					insertAccountTranMasterEntries(processResponse, insertionsToAccountTranMasterList);
					//Tran Master Entry End					
					
					//Bulk Transfer Entry Start
					updateApproveBulkTransferListData(processResponse, updatableBulkTransferDataList);
					//Bulk Transfer Entry Start
				}
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	private ProcessResponse approveMTOBulkTransferEntries(BulkTransfer bulkTransfer) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		processResponse.setCode("S0000");
		processResponse.setMessage("Sucessfully Approved the Bulk Entries");
		
		double trnsfrAmount = 0d;
		List<TierAccountMaster> tier1Limitslist = new ArrayList<>();
		List<TierAccountMaster> tier2Limitslist = new ArrayList<>();
		List<TierAccountMaster> tier3Limitslist = new ArrayList<>();
		List<AccountCreation> accountLimitslist = new ArrayList<AccountCreation>();
		try 
		{
			List<AccountCreation> updatableToAccountMasterList = new ArrayList<AccountCreation>();
			List<GLAccountTypeMaster> updatableToGLAccountTypeMasterList = new ArrayList<GLAccountTypeMaster>();
			
			List<AccountStatement> insertionsToAccountStatementList = new ArrayList<AccountStatement>();
			List<GLAccountStatement> insertionsToGLAccountStatementList = new ArrayList<GLAccountStatement>();
			
			List<AccountCreation> updatableFromAccountMasterList = new ArrayList<AccountCreation>();
			List<GLAccountTypeMaster> updatableFromGLAccountTypeMasterList = new ArrayList<GLAccountTypeMaster>();
			
			List<AccountStatement> insertionsFromAccountStatementList = new ArrayList<AccountStatement>();
			List<GLAccountStatement> insertionsFromGLAccountStatementList = new ArrayList<GLAccountStatement>();
			
			List<AccountTranMaster> insertionsAccountTranMasterList =  new ArrayList<AccountTranMaster>();		
			
			List<BulkTransfer> updatableBulkTransferDataList =  new ArrayList<BulkTransfer>();
			
			bulkTransfer.setStrStatus("pending");
			String preTxnId = bulkTransfer.getStrPreTransactionId();
			
			List<CustomerByAccountResponse> customerAccountList = getFromAccountBulkDataList(bulkTransfer);
			if (customerAccountList!=null && customerAccountList.size() > 0)
			{
				CustomerByAccountResponse toCustomerByAccountResp = customerAccountList.get(0);
				String strToAccountNumber = toCustomerByAccountResp.getStrToAccountNumber();
				
				AccountCreation toAccountCreation = new AccountCreation();
				toAccountCreation.setStrAccountNumber(strToAccountNumber);
				toAccountCreation.setStrAccountType(toCustomerByAccountResp.getStrToAccountType());
				
				CustomerByAccountResponse toCustomerAccountInfo = getAccountInfoByAccountNo(toAccountCreation);				
				if (toCustomerAccountInfo!=null) 
				{
					String toAcPreCredAmount = String.valueOf(toCustomerAccountInfo.getStrPreCredAmount());
					String toAcClosingBal = toCustomerAccountInfo.getStrClosingBalance();
					
					String toAcLinkedGLBalance = toCustomerAccountInfo.getGlAccountBalance();
					String toAcLinkedGLAccNo = toCustomerAccountInfo.getGlAccountNo().trim();
					String toAcLinkedGLAccType = toCustomerAccountInfo.getGlAccountType().trim();
					
					for(CustomerByAccountResponse customerAccountResp: customerAccountList) 
					{
						String tranId = transactionIdCreationConfigDao.getTransactionId();
						amsLogger.writeInfoLog("approveMTOBulkTransferEntries tranId::["+tranId+"]");
						customerAccountResp.setTxnId(tranId);
						
						amsLogger.writeInfoLog("approveMTOBulkTransferEntries customerAccountResp::["+customerAccountResp+"]");
						String custTransferAmount = customerAccountResp.getTransferAmount();
						amsLogger.writeInfoLog("approveMTOBulkTransferEntries transfer Amount::["+customerAccountResp.getTransferAmount()+"]");
						
						toAcClosingBal = Utils.getIncreaseAmount(toAcClosingBal, custTransferAmount);
						toAcPreCredAmount = Utils.getReduceAmount(toAcPreCredAmount, custTransferAmount);
						
						//To A/C Start
						AccountCreation toAccountCreationData = new AccountCreation();
						
						toAccountCreationData.setStrAccountNumber(toAccountCreation.getStrAccountNumber());
						toAccountCreationData.setStrAccountType(toAccountCreation.getStrAccountType());
						
						toAccountCreationData.setStrPreCredAmount(Double.parseDouble(toAcPreCredAmount));
						toAccountCreationData.setStrClosingBalance(toAcClosingBal);
						
						updatableToAccountMasterList.add(toAccountCreationData);	//<---------TO A/C 1 List----------------->					
						
						toAcLinkedGLBalance = Utils.getIncreaseAmount(toAcLinkedGLBalance, custTransferAmount);
						GLAccountTypeMaster toGLAccountTypeMaster = new GLAccountTypeMaster();
						
						toGLAccountTypeMaster.setStrAccountNumber(toAcLinkedGLAccNo);
						toGLAccountTypeMaster.setStrGLAccountType(toAcLinkedGLAccType);						
						toGLAccountTypeMaster.setStrClosingBalance(toAcLinkedGLBalance);
						
						updatableToGLAccountTypeMasterList.add(toGLAccountTypeMaster); //<---------TO A/C 2 List----------------->
						
						toCustomerAccountInfo.setStrAvailableBalance(toAcClosingBal);
						toCustomerAccountInfo.setTransferAmount(custTransferAmount);
						toCustomerAccountInfo.setTxnId(tranId);
						
						AccountStatement toAccountStatement = getCreditAccountStatementEntry(toCustomerAccountInfo);
						insertionsToAccountStatementList.add(toAccountStatement); //<---------TO A/C 3 List----------------->					
						
						toCustomerAccountInfo.setGlAccountAvailableBalance(toAcLinkedGLBalance);
						GLAccountStatement fromGLAccountStatement = getCreditGLAccountStatement(toCustomerAccountInfo);
						
						insertionsToGLAccountStatementList.add(fromGLAccountStatement); //<---------TO A/C 4 List----------------->
						//TO A/C End
						
						//From A/C Start
						String FromAccountNumber = customerAccountResp.getStrAccountNumber();
						
						AccountCreation fromAccountCreation = getFromAccountCreationData(customerAccountResp);
						updatableFromAccountMasterList.add(fromAccountCreation);//<---------1 List----------------->
						
						addFromAccountLimitListData(customerAccountResp, tier1Limitslist, tier2Limitslist, tier3Limitslist, accountLimitslist);//<---------2 List----------------->
						
						//-------Account Tran Master Entry Start
						customerAccountResp.setStrFromAccountTran(FromAccountNumber);
						customerAccountResp.setStrToAccountTran(strToAccountNumber);
						customerAccountResp.setResponseCode("00");
						AccountTranMaster accountTranMaster = getAccountTranMasterEntry(customerAccountResp);
						insertionsAccountTranMasterList.add(accountTranMaster);//<---------3 List----------------->
						//-------Account Tran Master Entry End
						
						//------Linked GL Account-------Start						
						GLAccountTypeMaster glAccountTypeMaster = getFromLinkedGLaccouAccountTypeMaster(customerAccountResp, updatableFromGLAccountTypeMasterList);//<---------4 List-----------------> 
						//------Linked GL Account-------End
						
						//-------Account Statement Entry Start
						customerAccountResp.setStrClosingBalance(fromAccountCreation.getStrClosingBalance());
						AccountStatement accountStatement = getDebitAccountStatementEntry(customerAccountResp);
						insertionsFromAccountStatementList.add(accountStatement);//<---------5 List----------------->
						//-------Account Statement Entry End
						
						//----- GLAccount Statement Start
						customerAccountResp.setGlAccountAvailableBalance(glAccountTypeMaster.getStrClosingBalance());
						GLAccountStatement glAccountStatement = getDebitGLAccountStatement(customerAccountResp);
						insertionsFromGLAccountStatementList.add(glAccountStatement);//<---------6 List----------------->
						//----- GLAccount Statement End
						
						//-- Final Bulk Transfer Updatable List Start
						BulkTransfer updatableBulkTrsfr = new BulkTransfer();
						updatableBulkTrsfr.setStrTransactionId(tranId);
						updatableBulkTrsfr.setStrCheckerId(bulkTransfer.getStrCheckerId());
						
						updatableBulkTrsfr.setStrFromAccountNo(FromAccountNumber);
						updatableBulkTrsfr.setStrToAccountNo(strToAccountNumber);
						updatableBulkTrsfr.setStrPreTransactionId(preTxnId);
						updatableBulkTrsfr.setStrStatus("approved");
						
						updatableBulkTransferDataList.add(updatableBulkTrsfr);//<---------7 List----------------->
						//-- Final Bulk Transfer Updatable List Start
						
						//From A/C End
						
						trnsfrAmount = trnsfrAmount + Double.parseDouble(customerAccountResp.getTransferAmount());
					}
					String fromAccountTransferAmount = String.valueOf(trnsfrAmount);
					toCustomerAccountInfo.setTransferAmount(fromAccountTransferAmount);
					
					//From A/C  Entry Start					
					updateFromAccountListData(processResponse, updatableFromAccountMasterList, updatableFromGLAccountTypeMasterList);	
					updatefromAccountLimitListData(tier1Limitslist, tier2Limitslist, tier3Limitslist, accountLimitslist);
					
					insertFromAccountStatementWithGL(processResponse, insertionsFromAccountStatementList, insertionsFromGLAccountStatementList);
					//From A/C  Entry End
					
					//To A/C Entries Start
					updateToAccountListData(processResponse, updatableToAccountMasterList, updatableToGLAccountTypeMasterList);					
					
					insertToAccountStatementWithGL(processResponse, insertionsToAccountStatementList, insertionsToGLAccountStatementList);					
					//To A/C Entries End
					
					//Tran Master Entry Start
					insertAccountTranMasterEntries(processResponse, insertionsAccountTranMasterList);
					//Tran Master Entry End					
					
					//Bulk Transfer Entry Start
					updateApproveBulkTransferListData(processResponse, updatableBulkTransferDataList);
					//Bulk Transfer Entry Start
				}
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private void addFromAccountLimitListData(CustomerByAccountResponse fromCustomerAccountInfo, List<TierAccountMaster> tier1Limitslist, List<TierAccountMaster> tier2Limitslist, List<TierAccountMaster> tier3Limitslist, List<AccountCreation> accountLimitslist) 
	{
		try 
		{
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				getFromAccountLimitsDataForNigeria(fromCustomerAccountInfo, tier1Limitslist, tier2Limitslist, tier3Limitslist);
			}
			else
			{
				AccountCreation fromAccountLimits = getFromAccountLimitsData(fromCustomerAccountInfo);
				accountLimitslist.add(fromAccountLimits);
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private void updatefromAccountLimitListData(List<TierAccountMaster> tier1Limitslist, List<TierAccountMaster> tier2Limitslist, List<TierAccountMaster> tier3Limitslist, List<AccountCreation> accountLimitslist) 
	{
		try 
		{
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				if (tier1Limitslist!=null && tier1Limitslist.size() > 0) 
				{
					tierAccountMasterService.updatesBatchEntryOfTier1LimitsAccountMasterFields(tier1Limitslist);
				}
				if (tier2Limitslist!=null && tier2Limitslist.size() > 0) 
				{
					tierAccountMasterService.updatesBatchEntryOfTier2LimitsAccountMasterFields(tier2Limitslist);
				}
				if (tier3Limitslist!=null && tier3Limitslist.size() > 0) 
				{
					tierAccountMasterService.updatesBatchEntryOfTier3LimitsAccountMasterFields(tier3Limitslist);
				}
			}
			else
			{
				if (accountLimitslist!=null && accountLimitslist.size() > 0) 
				{
					accountMasterService.updatesBatchEntryOfLimitsAccountMasterFields(accountLimitslist);
				}
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
	}
	
	private List<CustomerByAccountResponse> getToAccountBulkDataList(BulkTransfer bulkTransfer) 
	{
		List<CustomerByAccountResponse> customerAccountList = null;
		try 
		{
			amsLogger.writeInfoLog("Inside getToAccountBulkDataList ApplicationName =["+CommonConstants.applicationName+"]");
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				customerAccountList = bulkTransferService.getToAccountBulkDataList(bulkTransfer); 
			}
			else
			{
				customerAccountList = bulkTransferService.getToAccountBulkDataListForNonNigeria(bulkTransfer);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return customerAccountList;
	}
	
	private List<CustomerByAccountResponse> getFromAccountBulkDataList(BulkTransfer bulkTransfer) 
	{
		List<CustomerByAccountResponse> customerAccountList = null;
		try 
		{
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				customerAccountList = bulkTransferService.getFromAccountBulkDataList(bulkTransfer); 
			}
			else
			{
				customerAccountList = bulkTransferService.getFromAccountBulkDataListForNonNigeria(bulkTransfer);
			}
		}
		catch (Exception e)
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return customerAccountList;
	}
	
	private CustomerByAccountResponse getAccountInfoByAccountNo(AccountCreation accountCreation) 
	{
		CustomerByAccountResponse customerByAccountRes = null;
		try 
		{
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				customerByAccountRes = accountMasterService.getCustomerAccountInfoByAccountNo(accountCreation);
			}
			else 
			{
				customerByAccountRes = accountMasterService.getCustomerAccountInfoByAccountNoForNonNigeria(accountCreation);
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return customerByAccountRes;
	}
	
	private ProcessResponse validateFromAccounts(CustomerByAccountResponse customerByAccountResponses)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			processResponse.setCode("S0000");
			
			if ("active".equalsIgnoreCase(customerByAccountResponses.getStrStatus())) 
			{
				double transferAmount = Double.parseDouble(customerByAccountResponses.getTransferAmount());
				
				double availableBalance = Double.parseDouble(customerByAccountResponses.getStrClosingBalance());			
				double strEarMarkAmount = (customerByAccountResponses.getStrEarMarkAmount()!=null) ? customerByAccountResponses.getStrEarMarkAmount() : 0;			
				
				double actualClosingBalance = availableBalance - strEarMarkAmount;
				
				if (Double.compare(actualClosingBalance, transferAmount) >= 0) 
				{
					if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
					{
						String strActiveTier = customerByAccountResponses.getStrActiveTier();
						
						double currentTierDailyCumulativeLimit = 0.0;
						
						if(strActiveTier.equals("tier1")) 
						{
							currentTierDailyCumulativeLimit = Double.parseDouble(customerByAccountResponses.getStrAvailableTier1DailyCumlimit()) ;
						}
						else if(strActiveTier.equals("tier2"))
						{
							currentTierDailyCumulativeLimit = Double.parseDouble(customerByAccountResponses.getStrAvailableTier2DailyCumlimit()) ;
						}
						else
						{
							currentTierDailyCumulativeLimit = Double.parseDouble(customerByAccountResponses.getStrAvailableTier3DailyCumlimit()) ;
						}
						
						if (!(Double.compare(currentTierDailyCumulativeLimit, transferAmount) >= 0))
						{
							processResponse.setCode("E0000");
							processResponse.setMessage("Daily Cummulative Limit Breached!!");
						}						
					}
					else 
					{
						if ("C".equalsIgnoreCase(customerByAccountResponses.getStrAccountTypeCategory())) 
						{
							double availableCreditLimit = Double.parseDouble(customerByAccountResponses.getStrAvailableCreditLimit());
							if (!((Double.compare(availableCreditLimit, transferAmount) >= 0))) 
							{
								processResponse.setCode("E0000");
								processResponse.setMessage("Credit Limit Breached!!");
							}
						}
						else
						{
							double availableDailyAmount = Double.parseDouble(customerByAccountResponses.getStrAvailableDailyLimit());
							
							if ((Double.compare(availableDailyAmount, transferAmount) >= 0)) 
							{
								double availableMonthlyAmount = Double.parseDouble(customerByAccountResponses.getStrAvailableMonthlyLimit());
								if ((Double.compare(availableMonthlyAmount, transferAmount) >= 0)) 
								{
									double availableYearlyAmount = Double.parseDouble(customerByAccountResponses.getStrAvailableYearlyLimit());
									if (!(Double.compare(availableYearlyAmount, transferAmount) >= 0)) 
									{
										processResponse.setCode("E0000");
										processResponse.setMessage("Yearly Limit Breached!!");
									}
								}
								else
								{
									processResponse.setCode("E0000");
									processResponse.setMessage("Monthly Limit Breached!!");
								}
							}
							else
							{
								processResponse.setCode("E0000");
								processResponse.setMessage("Daily Limit Breached!!");
							}
						}
					}
				}
				else 
				{
					processResponse.setCode("E0000");
					processResponse.setMessage("Insufficient Account Balance!!");
				}
			}
			else
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("From Account is not active!!");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		amsLogger.writeInfoLog("validateFromAccounts processResponse:::"+processResponse);
		return processResponse;
	}

	private ProcessResponse validateToAccounts(CustomerByAccountResponse customerByAccountResponses) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			processResponse.setCode("S0000");
			
			if ("active".equalsIgnoreCase(customerByAccountResponses.getStrStatus())) 
			{
				amsLogger.writeInfoLog("Inside validateToAccounts applicationName=["+CommonConstants.applicationName+"]");
				if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
				{
					double transferAmount = Double.parseDouble(customerByAccountResponses.getTransferAmount());
					
					double availableBalance = Double.parseDouble(customerByAccountResponses.getStrClosingBalance());
					double strPreCredAmount = Double.parseDouble(customerByAccountResponses.getStrPreCredAmount());
					
					double actualClosingBalance = availableBalance + strPreCredAmount + transferAmount;				
					
					String strActiveTier = customerByAccountResponses.getStrActiveTier();
					
					amsLogger.writeInfoLog("Inside validateToAccounts Current Active Tier=["+strActiveTier+"]");
					double cummulativeBalance = 0d; 
					if ("tier1".equalsIgnoreCase(strActiveTier)) 
					{
						cummulativeBalance = Double.parseDouble(customerByAccountResponses.getTier1CummBalance());
					}
					else if ("tier2".equalsIgnoreCase(strActiveTier)) 
					{
						cummulativeBalance = Double.parseDouble(customerByAccountResponses.getTier2CummBalance());
					}
					else
					{
						cummulativeBalance = Double.parseDouble(customerByAccountResponses.getTier3CummBalance());
					}
					
					amsLogger.writeInfoLog("Inside validateToAccounts actualClosingBalance=["+actualClosingBalance+"]cummulativeBalance=["+cummulativeBalance+"]");
					if (actualClosingBalance > cummulativeBalance) 
					{
						processResponse.setCode("E0000");
						processResponse.setMessage("Cummulative Account Balance Breached!!");
					}
				}				
			}
			else 
			{
				processResponse.setCode("E0000");
				processResponse.setMessage("To Account is not active!!");
			}
		}
		catch (Exception e) 
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	
	private AccountStatement getDebitAccountStatementEntry(CustomerByAccountResponse customerByAccountResponses) 
	{
		AccountStatement accountStatement = new AccountStatement();
		try 
		{
			String participantId = appInfo.getStrParticipantId();
			
			amsLogger.writeInfoLog("Inside getDebitAccountStatementEntry customerByAccountResponses["+customerByAccountResponses+"]");
			
			String txnAmount = customerByAccountResponses.getTransferAmount();
			amsLogger.writeInfoLog("Inside getDebitAccountStatementEntry txnAmount["+txnAmount+"]"); 
			
			accountStatement.setStrParticipantId(participantId);
			accountStatement.setStrAccountNumber(customerByAccountResponses.getStrAccountNumber());			
			accountStatement.setStrAccountType(customerByAccountResponses.getStrAccountType());
			
			accountStatement.setStrClosingBalance(customerByAccountResponses.getStrClosingBalance());			
			
			accountStatement.setStrTransactionAmount(txnAmount);			
			accountStatement.setStrTransactionID(customerByAccountResponses.getTxnId());
			
			if (customerByAccountResponses.getTxnType()!=null && customerByAccountResponses.getTxnType().trim().length()>0) 
			{
				accountStatement.setStrTransactionType(customerByAccountResponses.getTxnType().trim());		
			}
			else
			{
				accountStatement.setStrTransactionType("WDL");
			}
			if (customerByAccountResponses.getTxnNarration()!=null && customerByAccountResponses.getTxnNarration().trim().length() > 0)
			{
				accountStatement.setStrNaration(customerByAccountResponses.getTxnNarration().trim());
			}
			else
			{
				accountStatement.setStrNaration("Amount Transfer Successfully.");
			}		
			accountStatement.setStrTransactionMode(TransactionType.MODE.get(accountStatement.getStrTransactionType()));
			accountStatement.setStrIsGLType("N");
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountStatement;
	}

	private AccountStatement getCreditAccountStatementEntry(CustomerByAccountResponse customerByAccountResponses) 
	{
		AccountStatement accountStatement = new AccountStatement();
		try 
		{
			String participantId = appInfo.getStrParticipantId();
			
			amsLogger.writeInfoLog("Inside getCreditAccountStatementEntry customerByAccountResponses["+customerByAccountResponses+"]");
			
			String txnAmount = customerByAccountResponses.getTransferAmount();
			amsLogger.writeInfoLog("Inside getCreditAccountStatementEntry txnAmount["+txnAmount+"]"); 
			
			accountStatement.setStrParticipantId(participantId);
			accountStatement.setStrAccountNumber(customerByAccountResponses.getStrAccountNumber());			
			accountStatement.setStrAccountType(customerByAccountResponses.getStrAccountType());
			
			accountStatement.setStrClosingBalance(customerByAccountResponses.getStrAvailableBalance());			
			
			accountStatement.setStrTransactionAmount(txnAmount);		
			accountStatement.setStrTransactionID(customerByAccountResponses.getTxnId());
			
			if (customerByAccountResponses.getTxnType()!=null && customerByAccountResponses.getTxnType().trim().length()>0) 
			{
				accountStatement.setStrTransactionType(customerByAccountResponses.getTxnType().trim());		
			}
			else
			{
				accountStatement.setStrTransactionType("DPT");
			}
			if (customerByAccountResponses.getTxnNarration()!=null && customerByAccountResponses.getTxnNarration().trim().length() > 0)
			{
				accountStatement.setStrNaration(customerByAccountResponses.getTxnNarration().trim());
			}
			else
			{
				accountStatement.setStrNaration("Amount Credited Successfully.");
			}		
			accountStatement.setStrTransactionMode(TransactionType.MODE.get(accountStatement.getStrTransactionType()));
			accountStatement.setStrIsGLType("N");
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountStatement;
	}
	
	private AccountCreation getFromAccountCreationData(CustomerByAccountResponse customerByAccountResponses) 
	{
		AccountCreation toAccountCreation = new AccountCreation();
		try 
		{
			toAccountCreation.setStrAccountNumber(customerByAccountResponses.getStrAccountNumber());
			toAccountCreation.setStrAccountType(customerByAccountResponses.getStrAccountType());
			
			String updatableClosingBal = Utils.getReduceAmount(customerByAccountResponses.getStrClosingBalance(), customerByAccountResponses.getTransferAmount());
			toAccountCreation.setStrClosingBalance(updatableClosingBal);
			
			String updatableEarMarkAmount = Utils.getReduceAmount(String.valueOf(customerByAccountResponses.getStrEarMarkAmount()), customerByAccountResponses.getTransferAmount());
			toAccountCreation.setStrEarMarkAmount(Double.parseDouble(updatableEarMarkAmount));
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return toAccountCreation;
	}
	private AccountCreation getToAccountCreationData(CustomerByAccountResponse customerByAccountResponses) 
	{
		AccountCreation toAccountCreation = null;
		try 
		{
			String toAccountNumber = customerByAccountResponses.getStrAccountNumber();
			String toAccountType = customerByAccountResponses.getStrAccountType();
			amsLogger.writeInfoLog("Inside getToAccountCreationData To Account Number=["+toAccountNumber+"] to Account Type=["+toAccountType+"]");
			
			toAccountCreation = new AccountCreation();
			toAccountCreation.setStrAccountNumber(toAccountNumber);
			toAccountCreation.setStrAccountType(toAccountType);
			
			String toAccountAvailableBalance = customerByAccountResponses.getStrClosingBalance();
			String toAccountTransferAmount = customerByAccountResponses.getTransferAmount();
			
			amsLogger.writeInfoLog("Inside getToAccountCreationData toAccountAvailableBalance=["+toAccountAvailableBalance+"] toAccountTransferAmount=["+toAccountTransferAmount+"]");
			String updatableClosingBal = Utils.getIncreaseAmount(toAccountAvailableBalance, toAccountTransferAmount);
			
			amsLogger.writeInfoLog("Inside getToAccountCreationData updatableClosingBal=["+updatableClosingBal+"]");
			toAccountCreation.setStrClosingBalance(updatableClosingBal);
			
			String toAccountPreCredAmount = customerByAccountResponses.getStrPreCredAmount();
			amsLogger.writeInfoLog("Inside getToAccountCreationData toAccountPreCredAmount=["+toAccountPreCredAmount+"] toAccountTransferAmount=["+toAccountTransferAmount+"]");
			
			String updatablePreCredAmount = Utils.getReduceAmount(toAccountPreCredAmount, toAccountTransferAmount);			
			amsLogger.writeInfoLog("Inside getToAccountCreationData updatablePreCredAmount=["+updatablePreCredAmount+"]");
			toAccountCreation.setStrPreCredAmount(Double.parseDouble(updatablePreCredAmount));
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return toAccountCreation;
	}
	private AccountCreation getFromAccountLimitsData(CustomerByAccountResponse customerAccountResp) 
	{
		AccountCreation fromAccountLimits = null;
		try 
		{
			fromAccountLimits = new AccountCreation();
			fromAccountLimits.setStrAccountNumber(customerAccountResp.getStrAccountNumber());
			fromAccountLimits.setStrAccountType(customerAccountResp.getStrAccountType());
			
			//For Credit limit categories Start
			if ("C".equalsIgnoreCase(customerAccountResp.getStrAccountTypeCategory())) 
			{
				String updatableCreditLimit = Utils.getIncreaseAmount(customerAccountResp.getStrAvailableCreditLimit(), customerAccountResp.getTransferAmount());
				fromAccountLimits.setStrAvailableCreditLimit(updatableCreditLimit);
				return fromAccountLimits;
			}
			//For Credit limit categories End
			
			String updatableDaliyLimit = Utils.getReduceAmount(customerAccountResp.getStrAvailableDailyLimit(), customerAccountResp.getTransferAmount());
			String updatableMonthlyLimit = Utils.getReduceAmount(customerAccountResp.getStrAvailableMonthlyLimit(), customerAccountResp.getTransferAmount());
			String updatableYearlyLimit = Utils.getReduceAmount(customerAccountResp.getStrAvailableYearlyLimit(), customerAccountResp.getTransferAmount());
			
			fromAccountLimits.setStrAvailableDailyLimit(updatableDaliyLimit);
			fromAccountLimits.setStrAvailableMonthlyLimit(updatableMonthlyLimit); 
			fromAccountLimits.setStrAvailableYearlyLimit(updatableYearlyLimit);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return fromAccountLimits;
	}
	private TierAccountMaster getFromAccountLimitsDataForNigeria(CustomerByAccountResponse customerByAccountResponses) 
	{
		TierAccountMaster fromTierAccountLimits = null;
		try 
		{
			fromTierAccountLimits = getFromAccountLimitsDataForNigeria(customerByAccountResponses, null, null, null);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return fromTierAccountLimits;
	}
	private TierAccountMaster getFromAccountLimitsDataForNigeria(CustomerByAccountResponse customerByAccountResponses, List<TierAccountMaster> tier1Limitslist, List<TierAccountMaster> tier2Limitslist, List<TierAccountMaster> tier3Limitslist) 
	{
		TierAccountMaster fromTierAccountLimits = null;
		try 
		{
			fromTierAccountLimits = new TierAccountMaster();
			fromTierAccountLimits.setStrAccountNo(customerByAccountResponses.getStrAccountNumber());
			fromTierAccountLimits.setStrAccountType(customerByAccountResponses.getStrAccountType());
			
			String currentActiveTier = customerByAccountResponses.getStrActiveTier(); 
			fromTierAccountLimits.setStrActiveTier(currentActiveTier);
			amsLogger.writeInfoLog("getFromAccountLimitsDataForNigeria currentActiveTier=["+currentActiveTier+"]");
			
			if ("tier1".equalsIgnoreCase(currentActiveTier)) 
			{
				String updatableDaliyCummulativeLimit = Utils.getReduceAmount(customerByAccountResponses.getStrAvailableTier1DailyCumlimit(), customerByAccountResponses.getTransferAmount());
				fromTierAccountLimits.setStrAvailableTier1DailyCumlimit(Utils.stringToDouble(updatableDaliyCummulativeLimit));
				if (tier1Limitslist!=null) 
				{
					tier1Limitslist.add(fromTierAccountLimits);
				}
			}
			else if ("tier2".equalsIgnoreCase(currentActiveTier)) 
			{
				String updatableDaliyCummulativeLimit = Utils.getReduceAmount(customerByAccountResponses.getStrAvailableTier2DailyCumlimit(), customerByAccountResponses.getTransferAmount());
				fromTierAccountLimits.setStrAvailableTier2DailyCumlimit(Utils.stringToDouble(updatableDaliyCummulativeLimit));
				if (tier2Limitslist!=null) 
				{
					tier2Limitslist.add(fromTierAccountLimits);
				}
			}
			else
			{
				String updatableDaliyCummulativeLimit = Utils.getReduceAmount(customerByAccountResponses.getStrAvailableTier3DailyCumlimit(), customerByAccountResponses.getTransferAmount());
				fromTierAccountLimits.setStrAvailableTier3DailyCumlimit(Utils.stringToDouble(updatableDaliyCummulativeLimit));
				if (tier3Limitslist!=null) 
				{
					tier3Limitslist.add(fromTierAccountLimits);
				}
			}
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return fromTierAccountLimits;
	}
	
	private GLAccountTypeMaster getFromLinkedGLaccouAccountTypeMaster(CustomerByAccountResponse customerByAccountResponses, List<GLAccountTypeMaster> updatableToGLAccountTypeMasterList) 
	{
		GLAccountTypeMaster resGLAccountTypeMaster = null;
		try 
		{
			
			String glAccountNumber = customerByAccountResponses.getGlAccountNo().trim();
			
			boolean isDataExist = false;
			if (updatableToGLAccountTypeMasterList!=null && updatableToGLAccountTypeMasterList.size()>0) 
			{
				for (GLAccountTypeMaster glaccMaster : updatableToGLAccountTypeMasterList) 
				{
					String existingAcNo = glaccMaster.getStrAccountNumber().trim();
					if (glAccountNumber.equalsIgnoreCase(existingAcNo))
					{
						resGLAccountTypeMaster = glaccMaster;
						String updatableGLclosingBal = Utils.getReduceAmount(glaccMaster.getStrClosingBalance(), customerByAccountResponses.getTransferAmount());
						glaccMaster.setStrClosingBalance(updatableGLclosingBal);
						isDataExist = true;
						break;
					}
				}
			}
			if (!isDataExist) 
			{
				GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
				
				glAccountTypeMaster.setStrAccountNumber(glAccountNumber);
				glAccountTypeMaster.setStrGLAccountType(customerByAccountResponses.getGlAccountType().trim());
				
				String updatableGLclosingBal = Utils.getReduceAmount(customerByAccountResponses.getGlAccountBalance(), customerByAccountResponses.getTransferAmount());
				glAccountTypeMaster.setStrClosingBalance(updatableGLclosingBal);
				
				resGLAccountTypeMaster = glAccountTypeMaster;
				if (updatableToGLAccountTypeMasterList!=null) 
				{
					updatableToGLAccountTypeMasterList.add(glAccountTypeMaster);
				}
			}			
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return resGLAccountTypeMaster;
	}
	private GLAccountTypeMaster getToLinkedGLaccouAccountTypeMaster(CustomerByAccountResponse customerByAccountResponses, List<GLAccountTypeMaster> updatableToGLAccountTypeMasterList) 
	{
		GLAccountTypeMaster resGLAccountTypeMaster = null;
		try 
		{
			String glAccountNumber = customerByAccountResponses.getGlAccountNo().trim();
			
			boolean isDataExist = false;
			if (updatableToGLAccountTypeMasterList!=null && updatableToGLAccountTypeMasterList.size()>0) 
			{
				for (GLAccountTypeMaster glaccMaster : updatableToGLAccountTypeMasterList) 
				{
					String existingAcNo = glaccMaster.getStrAccountNumber().trim();
					if (glAccountNumber.equalsIgnoreCase(existingAcNo))
					{
						resGLAccountTypeMaster = glaccMaster;
						String updatableGLclosingBal = Utils.getIncreaseAmount(glaccMaster.getStrClosingBalance(), customerByAccountResponses.getTransferAmount());
						glaccMaster.setStrClosingBalance(updatableGLclosingBal);
						isDataExist = true;
						break;
					}
				}
			}
			if (!isDataExist) 
			{
				GLAccountTypeMaster glAccountTypeMaster = new GLAccountTypeMaster();
				
				glAccountTypeMaster.setStrAccountNumber(glAccountNumber);
				glAccountTypeMaster.setStrGLAccountType(customerByAccountResponses.getGlAccountType().trim());
				
				String updatableGLclosingBal = Utils.getIncreaseAmount(customerByAccountResponses.getGlAccountBalance(), customerByAccountResponses.getTransferAmount());
				glAccountTypeMaster.setStrClosingBalance(updatableGLclosingBal);
				
				resGLAccountTypeMaster = glAccountTypeMaster;
				if (updatableToGLAccountTypeMasterList!=null) 
				{
					updatableToGLAccountTypeMasterList.add(glAccountTypeMaster);
				}
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return resGLAccountTypeMaster;
	}
	
	private AccountTranMaster getAccountTranMasterEntry(CustomerByAccountResponse customerByAccountResponses) 
	{
		AccountTranMaster accountTranMaster = new AccountTranMaster();
		try 
		{
			String participantId = appInfo.getStrParticipantId();
			
			accountTranMaster.setStrTxn_id(customerByAccountResponses.getTxnId());
			
			accountTranMaster.setStrFrom_account_number(customerByAccountResponses.getStrFromAccountTran());
			accountTranMaster.setStrTo_account_number(customerByAccountResponses.getStrToAccountTran());
			
			Double txnAmountInDouble = Double.parseDouble(customerByAccountResponses.getTransferAmount());
			
			accountTranMaster.setStrTransaction_amount(Utils.decimalFormat.format(txnAmountInDouble));
			accountTranMaster.setSwitchTxDate(new Date());
			
			accountTranMaster.setStrParticipantId(participantId);
			
			accountTranMaster.setStrLocal_tran_date(Utils.getCurrentDate());			
			accountTranMaster.setStrLocal_tran_time(Utils.getFormattedCurrentTime());
			
			accountTranMaster.setStrTran_type("TRF");
			accountTranMaster.setStrResponseCode(customerByAccountResponses.getResponseCode());
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountTranMaster;
	}
	
	private GLAccountStatement getDebitGLAccountStatement(CustomerByAccountResponse customerByAccountResponses) 
	{
		GLAccountStatement gLAccountStatement = new GLAccountStatement();
		try 
		{
			gLAccountStatement.setStrGLAccountType(customerByAccountResponses.getGlAccountType());
			gLAccountStatement.setStrRef(customerByAccountResponses.getGlAccountDescr());		
			
			gLAccountStatement.setStrAccountNumber(customerByAccountResponses.getGlAccountNo());		
			gLAccountStatement.setStrTxnId(customerByAccountResponses.getTxnId());		
			gLAccountStatement.setTransactionDate(Utils.getCurrentDate());
			
			gLAccountStatement.setStrTranType("WDL");		
			gLAccountStatement.setStrTranMode(TransactionType.MODE.get("WDL"));	
			
			Double txnAmountInDouble = Double.parseDouble(customerByAccountResponses.getTransferAmount());
			
			gLAccountStatement.setStrAmount(Utils.decimalFormat.format(txnAmountInDouble));
			gLAccountStatement.setStrClosingBalance(customerByAccountResponses.getGlAccountAvailableBalance());
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return gLAccountStatement;
	}
	
	private GLAccountStatement getCreditGLAccountStatement(CustomerByAccountResponse customerByAccountResponses) 
	{
		GLAccountStatement gLAccountStatement = new GLAccountStatement();
		try 
		{
			gLAccountStatement.setStrGLAccountType(customerByAccountResponses.getGlAccountType());
			gLAccountStatement.setStrRef(customerByAccountResponses.getGlAccountDescr());		
			
			gLAccountStatement.setStrAccountNumber(customerByAccountResponses.getGlAccountNo());		
			gLAccountStatement.setStrTxnId(customerByAccountResponses.getTxnId());		
			gLAccountStatement.setTransactionDate(Utils.getCurrentDate());
			
			gLAccountStatement.setStrTranType("DPT");		
			gLAccountStatement.setStrTranMode(TransactionType.MODE.get("DPT"));	
			
			Double txnAmountInDouble = Double.parseDouble(customerByAccountResponses.getTransferAmount());
			
			gLAccountStatement.setStrAmount(Utils.decimalFormat.format(txnAmountInDouble));
			gLAccountStatement.setStrClosingBalance(customerByAccountResponses.getGlAccountAvailableBalance());
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return gLAccountStatement;
	}
	
	//Bulk Entries into DB START
	ProcessResponse updateFromAccountListData(ProcessResponse processResponse, List<AccountCreation> updatableFromAccountMasterList, List<GLAccountTypeMaster> updatableFromGLAccountTypeMasterList)
	{
		try 
		{
			if (updatableFromAccountMasterList!=null && updatableFromAccountMasterList.size() > 0) 
			{
				accountMasterService.updatesBatchEntryOfFromAccount(updatableFromAccountMasterList);
			}
			if (updatableFromGLAccountTypeMasterList!=null && updatableFromGLAccountTypeMasterList.size() > 0) 
			{
				glAccountTypeMasterService.updatesBatchEntryOfLinkedGLAccount(updatableFromGLAccountTypeMasterList);
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	ProcessResponse updateToAccountListData(ProcessResponse processResponse, List<AccountCreation> updatableToAccountMasterList, List<GLAccountTypeMaster> updatableToGLAccountTypeMasterList)
	{
		try 
		{
			if (updatableToAccountMasterList!=null && updatableToAccountMasterList.size() > 0) 
			{
				accountMasterService.updatesBatchEntryOfToAccount(updatableToAccountMasterList);
			}
			if (updatableToGLAccountTypeMasterList!=null && updatableToGLAccountTypeMasterList.size() > 0) 
			{
				glAccountTypeMasterService.updatesBatchEntryOfLinkedGLAccount(updatableToGLAccountTypeMasterList);
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}

	ProcessResponse insertFromAccountStatementWithGL(ProcessResponse processResponse, List<AccountStatement> insertionsFromAccountStatementList, List<GLAccountStatement> insertionsFromGLAccountStatementList) 
	{
		try 
		{
			if (insertionsFromAccountStatementList !=null && insertionsFromAccountStatementList.size() > 0) 
			{
				accountStatementService.batchEntryOfAccountStatementMaster(insertionsFromAccountStatementList);
			}
			if (insertionsFromGLAccountStatementList !=null && insertionsFromGLAccountStatementList.size() > 0) 
			{
				glAccountStatementService.batchEntryOfGLAccountStatement(insertionsFromGLAccountStatementList);
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}

	ProcessResponse insertToAccountStatementWithGL(ProcessResponse processResponse, List<AccountStatement> insertionsToAccountStatementList, List<GLAccountStatement> insertionsToGLAccountStatementList)
	{
		try 
		{
			if (insertionsToAccountStatementList!=null && insertionsToAccountStatementList.size() > 0) 
			{
				accountStatementService.batchEntryOfAccountStatementMaster(insertionsToAccountStatementList);
			}
			if (insertionsToGLAccountStatementList!=null && insertionsToGLAccountStatementList.size() > 0) 
			{
				glAccountStatementService.batchEntryOfGLAccountStatement(insertionsToGLAccountStatementList);
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}	
		return processResponse;
	}

	ProcessResponse insertAccountTranMasterEntries(ProcessResponse processResponse, List<AccountTranMaster> insertionsToAccountTranMasterList)
	{
		try 
		{
			if (insertionsToAccountTranMasterList!=null && insertionsToAccountTranMasterList.size() > 0) 
			{
				accountTranMasterService.batchEntryOfAccountTranMaster(insertionsToAccountTranMasterList);
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}

	ProcessResponse updateApproveBulkTransferListData(ProcessResponse processResponse, List<BulkTransfer> updatableBulkTransferDataList)
	{
		try 
		{
			if (updatableBulkTransferDataList!=null && updatableBulkTransferDataList.size() > 0) 
			{
				bulkTransferService.batchEntryOfApprovedStatusOfBulkTransferData(updatableBulkTransferDataList);
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	ProcessResponse updateRejectBulkTransferListData(ProcessResponse processResponse, List<BulkTransfer> updatableBulkTransferDataList)
	{
		try 
		{
			if (updatableBulkTransferDataList!=null && updatableBulkTransferDataList.size() > 0) 
			{
				bulkTransferService.batchEntryOfRejectedStatusOfBulkTransferData(updatableBulkTransferDataList);
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	ProcessResponse updateSinglefromAccountLimit(ProcessResponse processResponse, CustomerByAccountResponse fromCustomerAccountInfo)
	{
		try 
		{
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				TierAccountMaster fromTierAccountLimits = getFromAccountLimitsDataForNigeria(fromCustomerAccountInfo);
				amsLogger.writeInfoLog("updateSinglefromAccountLimit fromTierAccountLimits::["+fromTierAccountLimits+"]");
				tierAccountMasterService.updateCummAvailableBalance(fromTierAccountLimits);
			}
			else
			{
				AccountCreation fromAccountLimits = getFromAccountLimitsData(fromCustomerAccountInfo);
				accountMasterService.updateAccountCreationFields(fromAccountLimits);
			}
		}
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return processResponse;
	}
	//Bulk Entries into DB END

	@Override
	public List<BulkTransfer> getAuthorizePreTxnIdBulklist(BulkTransfer bulkTransfer) 
	{
		return bulkTransferDao.getAuthorizePreTxnIdBulklist(bulkTransfer);
	}
	
	@Override
	public ProcessResponse getBulkTransferExcelReqRes(Workbook workbook) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		Row errorRow = null;
		try 
		{
			List<BulkTransferExcelReqRes> bulkTransferExcelReqResList = new ArrayList<BulkTransferExcelReqRes>();
			
			DataFormatter objDefaultFormatter = new DataFormatter();
			String errorMsg = null;
			boolean isErrorOccur = false;
			
			amsLogger.writeInfoLog("workbook sheet::"+workbook.getNumberOfSheets()+"]");
			for (Sheet sheet : workbook)
			{
				Iterator<Row> rowIterator = sheet.iterator();
				Row headerRow = rowIterator.next();
				amsLogger.writeInfoLog("headerRow row num=["+headerRow.getRowNum()+"]");
				
				HashMap<String, AccountMaster> hashMap = new HashMap<String, AccountMaster>();
				while(rowIterator.hasNext()) 
				{
					Row row = rowIterator.next();
					
					//amsLogger.writeInfoLog("Inside While row=["+row+"]");
					amsLogger.writeInfoLog("getRowNum=["+row.getRowNum()+"]");
					// Skip the header row
			        if (row.getRowNum() == headerRow.getRowNum()) 
			        {
			            continue;
			        }
			        errorRow = row;
			        
					BulkTransferExcelReqRes bulkTransfer = new BulkTransferExcelReqRes();
					
					String cell_0 =	objDefaultFormatter.formatCellValue(row.getCell(0)); //Bulk_mode
					amsLogger.writeInfoLog("cell_0=["+cell_0+"]");
					
					if (cell_0!=null && cell_0.trim().length()==0) 
					{
						break;
					}
					bulkTransfer.setStrBulkMode(cell_0);					
					
					String fromAccountNo = objDefaultFormatter.formatCellValue(row.getCell(1)); //From Account No
					amsLogger.writeInfoLog("cell_1=["+fromAccountNo+"]");
					bulkTransfer.setStrFromAccountNo(fromAccountNo);
					
					String toAccountNo = objDefaultFormatter.formatCellValue(row.getCell(2)); //To Account No
					amsLogger.writeInfoLog("cell_2=["+toAccountNo+"]");
					bulkTransfer.setStrToAccountNo(toAccountNo);
					
					String cell_3 =	objDefaultFormatter.formatCellValue(row.getCell(3)); //Transfer Amount
					amsLogger.writeInfoLog("cell_3=["+cell_3+"]");
					bulkTransfer.setStrAmount(Double.parseDouble(cell_3));
					
					String cell_4 =	objDefaultFormatter.formatCellValue(row.getCell(4)); //Narration
					amsLogger.writeInfoLog("cell_4=["+cell_4+"]");
					bulkTransfer.setStrNarration(cell_4);
					
					amsLogger.writeInfoLog("Before errorMsg=["+errorMsg+"]isErrorOccur=["+isErrorOccur+"]");
					
					AccountMaster fromAccountMaster = null;
					if (hashMap!=null && hashMap.get(fromAccountNo)!=null) 
					{
						fromAccountMaster = hashMap.get(fromAccountNo);
					}
					else 
					{
						fromAccountMaster = getAccountInfoObjBasedAcNo(fromAccountNo); 
					}
					if (fromAccountMaster!=null) 
					{
						hashMap.put(fromAccountNo, fromAccountMaster);						
						bulkTransfer.setStrFromAccountName(fromAccountMaster.getStrAccountHolderName());
						bulkTransfer.setStrFromAccountType(fromAccountMaster.getStrAccountType());
					}
					else
					{
						isErrorOccur = true;
						errorMsg = "From Account Not Found.";
						break;
					}
					
					AccountMaster toAccountMaster = null;
					if (hashMap!=null && hashMap.get(toAccountNo)!=null) 
					{
						toAccountMaster = hashMap.get(toAccountNo);
					}
					else
					{
						toAccountMaster = getAccountInfoObjBasedAcNo(toAccountNo); 
					}	
					
					if (toAccountMaster!=null)
					{
						hashMap.put(toAccountNo, toAccountMaster);					
						bulkTransfer.setStrToAccountName(toAccountMaster.getStrAccountHolderName());
						bulkTransfer.setStrToAccountType(toAccountMaster.getStrAccountType());
					}
					else
					{
						isErrorOccur = true;
						errorMsg = "To Account Not Found.";
						break;
					}
					amsLogger.writeInfoLog("Inside While loop bulkTransfer=["+bulkTransfer+"]");
					bulkTransferExcelReqResList.add(bulkTransfer);
				}
				amsLogger.writeInfoLog("After--- errorMsg=["+errorMsg+"]isErrorOccur=["+isErrorOccur+"]");
				if (isErrorOccur)
				{
					break;
				}
			}
			amsLogger.writeInfoLog("Finaly--- errorMsg=["+errorMsg+"]isErrorOccur=["+isErrorOccur+"]");
			if (isErrorOccur) 
			{
				processResponse.setCode("E0000");
				processResponse.setMessage(errorMsg);				
			}
			else
			{
				processResponse.setBulkTransferExcelReqRes(bulkTransferExcelReqResList);
				processResponse.setMessage("Sucessfully Parsed the Excel File");
				processResponse.setCode("S0000");
			}
		}
		catch(IllegalStateException e1) 
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e1.getMessage()+" Please Check the Row " + errorRow.getRowNum() + " in your File");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e1));
		} 
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage("Error occurred while reading the file." + e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));			
		}	
		amsLogger.writeInfoLog("Inside getBulkTransferExcelReqRes processResponse:::----"+processResponse);
		return processResponse;
	}

	/*
	@Override
	public ProcessResponse getBulkTransferExcelReqRes(Workbook workbook) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		Row errorRow = null;
		try 
		{
			List<BulkTransferExcelReqRes> bulkTransferExcelReqResList = new ArrayList<BulkTransferExcelReqRes>();
			
			for (Sheet sheet : workbook)
			{
				Iterator<Row> rowIterator = sheet.iterator();
				Row headerRow = rowIterator.next();

				//List<String> invalidHeaders = validateColumnHeader(headerRow);
				//if (!invalidHeaders.isEmpty())
				//{
					//throw new Exception("Invalid column headers: " + invalidHeaders);
				//}
				

				HashMap<String, String> hashMap = new HashMap<String, String>();
				while(rowIterator.hasNext()) 
				{
					Row row = rowIterator.next();
					// Skip the header row
			        if (row.getRowNum() == headerRow.getRowNum()) 
			        {
			            continue;
			        }
			        errorRow = row;
			        
					BulkTransferExcelReqRes bulkTransfer = new BulkTransferExcelReqRes();
					bulkTransfer.setStrId((int)row.getCell(0).getNumericCellValue());
					bulkTransfer.setStrBulkMode(row.getCell(1).getStringCellValue());
					
					String fromAccountType = row.getCell(2).getStringCellValue();
					String fromAccountNo =  row.getCell(3).getStringCellValue();
					
					bulkTransfer.setStrFromAccountType(fromAccountType);
					bulkTransfer.setStrFromAccountNo(fromAccountNo);
					
					String fromAccountName = ""; 
					if (hashMap!=null && hashMap.get(fromAccountNo)!=null) 
					{
						fromAccountName = hashMap.get(fromAccountNo);
					}
					else 
					{
						fromAccountName = getAccountName(fromAccountNo, fromAccountType); 
					}		
					hashMap.put(fromAccountNo, fromAccountName);
					
					bulkTransfer.setStrFromAccountName(fromAccountName);
					
					String toAccountType = row.getCell(4).getStringCellValue();
					String toAccountNo =  row.getCell(5).getStringCellValue();
					
					bulkTransfer.setStrToAccountType(toAccountType);
					bulkTransfer.setStrToAccountNo(toAccountNo);
					
					String toAccountName = ""; 
					if (hashMap!=null && hashMap.get(toAccountNo)!=null) 
					{
						toAccountName = hashMap.get(toAccountNo);
					}
					else
					{
						toAccountName = getAccountName(toAccountNo, toAccountType); 
					}							
					hashMap.put(toAccountNo, toAccountName);
					
					bulkTransfer.setStrToAccountName(toAccountName);
					
					bulkTransfer.setStrAmount(row.getCell(6).getNumericCellValue());				
					
					bulkTransferExcelReqResList.add(bulkTransfer);
				}
			}
			processResponse.setBulkTransferExcelReqRes(bulkTransferExcelReqResList);
			processResponse.setMessage("Sucessfully Parsed the Excel File");
			processResponse.setCode("S0000");
		}
		catch(IllegalStateException e1) 
		{
			processResponse.setCode("E0000");
			processResponse.setMessage(e1.getMessage()+" Please Check the Row " + errorRow.getRowNum() + " in your File");
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e1));
		} 
		catch (Exception e)
		{
			processResponse.setCode("E0000");
			processResponse.setMessage("Error occurred while reading the file." + e.getMessage());
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));			
		}		
		return processResponse;
	}
	*/
	
	@Override
	public ResponseEntity<?> getAccountLimitInfo(BulkTransferDto bulkTransfer) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		AccountCreation accountCreation = new AccountCreation();
		try {
			accountCreation.setStrAccountType(bulkTransfer.getStrFromAccountType());
			accountCreation.setStrAccountNumber(bulkTransfer.getStrFromAccountNo());
			AccountCreation accountMaster = accountMasterService.getAccountLimitInfoToCompareTransferAmount(accountCreation);
			 String strActiveTier = accountMaster.getStrActiveTier();
				//getStrDailyCumLimit is getting null throwing exception
				if ("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
				{
					 if(strActiveTier.equals("tier1"))
					 {
						 if(bulkTransfer.getStrTransferAmount() <= Double.parseDouble(accountMaster.getStrAvailableTier1DailyCumlimit()))
							{
							 	processResponse.setCode("S0000");
								processResponse.setStatus("Success");
							}
						 else
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Available Tier1 Daily Limit Breached");
							}
					 }
					 else if(strActiveTier.equals("tier2"))
					 {
						 if(bulkTransfer.getStrTransferAmount() <= Double.parseDouble(accountMaster.getStrAvailableTier2DailyCumlimit()))
							{
							 processResponse.setCode("S0000");
							 processResponse.setStatus("Success");
							}
						 else 
							{
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Available Tier2 Daily Limit Breached");
							}
							
					 }
					 else {
						 if(bulkTransfer.getStrTransferAmount() <= Double.parseDouble(accountMaster.getStrAvailableTier3DailyCumlimit()))
							{
								processResponse.setCode("S0000");
								processResponse.setStatus("Success");
								
							}
						 else {
								processResponse.setCode("E0000");
								processResponse.setStatus("Failed");
								processResponse.setMessage("Available Tier3 Daily Limit Breached");
							}
					 }
					
				}
				else 
				{
					if (bulkTransfer.getStrTransferAmount() <= Double.parseDouble((accountMaster.getStrAvailableDailyLimit())))
					{
						if (bulkTransfer.getStrTransferAmount() <= Double.parseDouble((accountMaster.getStrAvailableMonthlyLimit())))
						{
							processResponse.setCode("S0000");
							processResponse.setStatus("Success");
						}
						else 
						{
							processResponse.setCode("E0000");
							processResponse.setStatus("Failed");
							processResponse.setMessage("Monthly Limit Breached!");
						}
					}
					else {
						processResponse.setCode("E0000");
						processResponse.setStatus("Failed");
						processResponse.setMessage("Daily Limit Breached!");
					}
				}
			}
				catch (Exception e) 
				{
				e.printStackTrace();
				processResponse.setCode("E0000");
				processResponse.setMessage(e.getMessage());
			}
		return ResponseEntity.status(HttpStatus.OK)
                .body(processResponse);
		
	}

	@Override
	public ResponseEntity<?> getCumlativeBalanceCompareTransAmt(BulkTransferDto bulkTransfer) 
	{
		ProcessResponse processResponse = new ProcessResponse();
		AccountCreation accountCreation = new AccountCreation();
		try 
		{
			accountCreation.setStrAccountType(bulkTransfer.getStrToAccountType());
			accountCreation.setStrAccountNumber(bulkTransfer.getStrToAccountNo());
			AccountCreation accountMaster = accountMasterService.getCumlativeBalanceCompareTransAmt(accountCreation);
			String strCustId = accountMaster.getStrCustId();
			
			//active tier is fetched null from query
            String strActiveTier = accountMaster.getStrActiveTier();
            
            Double currentTierAvialabeDailyCumulativeBalance = 0.0;
            Double currentTierDailyCumulativeLimit = 0.0;
            Double strCurrentTierCummulativeBalance = 0.0;

	        if(strActiveTier.equals("tier1")) 
	        {
	        	strCurrentTierCummulativeBalance = Double.parseDouble(accountMaster.getStrTier1CummulativeBalance());
	            currentTierAvialabeDailyCumulativeBalance = Double.parseDouble(accountMaster.getStrAvailableTier1DailyCumlimit());
	            currentTierDailyCumulativeLimit = Double.parseDouble(accountMaster.getStrTier1DailyCumlimit());
	        }
	        else if(strActiveTier.equals("tier2"))
	        {
	        	strCurrentTierCummulativeBalance = Double.parseDouble(accountMaster.getStrTier2CummulativeBalance());
	            currentTierAvialabeDailyCumulativeBalance = Double.parseDouble((accountMaster.getStrAvailableTier2DailyCumlimit()));
	            currentTierDailyCumulativeLimit = Double.parseDouble(accountMaster.getStrTier2DailyCumlimit());
	        }else 
	        {
	        	strCurrentTierCummulativeBalance = Double.parseDouble(accountMaster.getStrTier3CummulativeBalance());
	            currentTierAvialabeDailyCumulativeBalance = Double.parseDouble(accountMaster.getStrAvailableTier3DailyCumlimit());
	            currentTierDailyCumulativeLimit = Double.parseDouble(accountMaster.getStrTier3DailyCumlimit());
	        }

	        Double actualCumulativeLimitOfCurrentTier =  currentTierDailyCumulativeLimit - currentTierAvialabeDailyCumulativeBalance;

	        double cumulativeBalance = strCurrentTierCummulativeBalance;

            Double closingBalanceOfRecipient = Double.parseDouble(accountMaster.getStrClosingBalance());
            Double strPreCredAmount = accountMaster.getStrPreCredAmount();
            Double strTransactionAmt = bulkTransfer.getStrTransactionAmount();
        
            Double totalTransactionAmount = strTransactionAmt + strPreCredAmount + closingBalanceOfRecipient;

	        if (Double.compare(cumulativeBalance, totalTransactionAmount) >= 0)
	        {        
	            processResponse.setCode("S0000");
	            processResponse.setMessage("Sucess");
	        } 
	        else 
	        {
	            processResponse.setCode("E0000");
	            processResponse.setMessage("Cumulative Balance Limit Breached");
	        }
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(processResponse);
	}
	
	private String getAccountName(String strFromAccountNo, String strFromAccountType) 
	{
		String strFromAccountName = null;
		try 
		{
			AccountMaster accountMaster = new AccountMaster();
			accountMaster.setStrAccountNumber(strFromAccountNo);
			accountMaster.setStrAccountType(strFromAccountType);
			
			accountMaster = accountMasterService.getAccountDetails(accountMaster);
			strFromAccountName = accountMaster.getStrAccountHolderName();
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return strFromAccountName;
	}
	private AccountMaster getAccountInfoObjBasedAcNo(String accountNo) 
	{
		AccountMaster accountInfoRes = null;
		try 
		{
			AccountMaster accountMaster = new AccountMaster();
			accountMaster.setStrAccountNumber(accountNo);
			
			accountInfoRes = accountMasterService.getAccountDetails(accountMaster);
		}
		catch (Exception e) 
		{
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return accountInfoRes;
	}
	
	private List<String> validateColumnHeader(Row headerRow)
	{
		List<String> invalidHeaders = new ArrayList<>();

		Cell idHeaderCell = headerRow.getCell(0);
		Cell fromAccountTypeHeaderCell = headerRow.getCell(1);
		Cell fromAccountNoHeaderCell = headerRow.getCell(2);
		Cell fromAccountNameHeaderCell = headerRow.getCell(3);
		Cell toAccountTypeHeaderCell = headerRow.getCell(4);
		Cell toAccountNoNoHeaderCell = headerRow.getCell(5);
		Cell toAccountNameHeaderCell = headerRow.getCell(6);
		Cell amountHeaderCell = headerRow.getCell(7);
		
		String id = idHeaderCell.getStringCellValue().toLowerCase().trim().replaceAll("\\s", "");
		String fromAccountNo = fromAccountNoHeaderCell.getStringCellValue().toLowerCase().trim().replaceAll("\\s", "");
		String fromAccountType = fromAccountTypeHeaderCell.getStringCellValue().toLowerCase().trim().replaceAll("\\s", "");
		String fromAccountName = fromAccountNameHeaderCell.getStringCellValue().toLowerCase().trim().replaceAll("\\s", "");
		String toAccountNo = toAccountNoNoHeaderCell.getStringCellValue().toLowerCase().trim().replaceAll("\\s", "");
		String toAccountType = toAccountTypeHeaderCell.getStringCellValue().toLowerCase().trim().replaceAll("\\s", "");
		String toAccountName = toAccountNameHeaderCell.getStringCellValue().toLowerCase().trim().replaceAll("\\s", "");
		String transferAmount = amountHeaderCell.getStringCellValue().toLowerCase().trim().replaceAll("\\s", "");

		if (!ID_COLUMN_HEADER.equals(id))
		  {
			invalidHeaders.add("id");
		  }
		if (!FROM_ACCOUNT_TYPE_COLUMN_HEADER.equals(fromAccountType))
		{
			invalidHeaders.add(fromAccountTypeHeaderCell.toString());
		}
		if (!FROM_ACCOUNT_NUMBER_COLUMN_HEADER.equals(fromAccountNo))
		{
			invalidHeaders.add(fromAccountNoHeaderCell.toString());
		}
		
		if (!FROM_ACCOUNT_NAME_COLUMN_HEADER.equals(fromAccountName))
		{
			invalidHeaders.add(fromAccountNameHeaderCell.toString());
		}
		
		
		if (!TO_ACCOUNT_TYPE_COLUMN_HEADER.equals(toAccountType))
		{
			invalidHeaders.add(toAccountTypeHeaderCell.toString());
		}
		if (!TO_ACCOUNT_NUMBER_COLUMN_HEADER.equals(toAccountNo))
		{
			invalidHeaders.add(toAccountNoNoHeaderCell.toString());
		}
		
		if (!TO_ACCOUNT_NAME_COLUMN_HEADER.equals(toAccountName))
		{
			invalidHeaders.add(toAccountNameHeaderCell.toString());
		}
		
		if (!AMOUNT.equals(transferAmount))
		{
			invalidHeaders.add(amountHeaderCell.toString());
		}

		return invalidHeaders;
	}

	@Override
	public List<BulkTransfer> getPreTxnIdBulklistForVerify(BulkTransfer bulkTransfer) 
	{
		amsLogger.writeInfoLog("getting bulk list for verify......");
		return bulkTransferDao.getPreTxnIdBulklistForVerify(bulkTransfer);
	}
}