package ams.cms.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.CustomerByAccountResponse;
import ams.cms.config.CommonConstants;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountKycDetails;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.TierAccountMaster;
import ams.cms.services.AccountKycDetailsService;
import ams.cms.services.AccountMasterService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.TierAccountMasterService;
import ams.cms.util.ProcessResponse;
import ams.cms.utility.AccountMasterResponse;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/account")
public class AccountMasterController 
{
	private AMSLogger amsLogger = AMSLogger.getInstance(AccountMasterController.class);
	
	private SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");
	
	@Autowired
	private AccountMasterService accountMasterService;
	
	@Autowired
	private AccountTypeMasterService accountTypeMasterService;
	
	@Autowired
	private AccountKycDetailsService accountKycDetailsService;
	
	@Autowired
	private TierAccountMasterService tierAccountMasterService;
	
	@RequestMapping(value = "/saveAccountInfo", method = RequestMethod.POST)
	public ResponseEntity<?> processToSaveAccountInfo(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			Date birthDate = dt.parse(accountCreation.getStrDOB());
			accountCreation.setBirthDate(birthDate);
			accountCreation.setStrDateOfCreation(new Date());
			
			accountCreation = accountMasterService.saveAccountInformation(accountCreation);			
			if (accountCreation.getStrID() != null) 
			{
				//Added Changes for Nigeria based Start
				if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
				{
					AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
					accountTypeMaster.setStrAccountType(accountCreation.getStrAccountType());
					
					boolean isCreditAcType = accountTypeMasterService.isCreditAccounType(accountTypeMaster);					
					if (!isCreditAcType) 
					{
						TierAccountMaster tierAccountMaster = new TierAccountMaster();
						
						tierAccountMaster.setStrCustId(accountCreation.getStrCustId());
						tierAccountMaster.setStrAccountNo(accountCreation.getStrAccountNumber());
						tierAccountMaster.setStrAccountType(accountCreation.getStrAccountType());
						tierAccountMaster.setStrMobileNo(accountCreation.getStrMobileNo());
						tierAccountMaster.setStrCreatedBy(accountCreation.getStrCreatedBy());
						
						tierAccountMasterService.saveTierAccountMaster(tierAccountMaster);
					}
					
				}
				//Added Changes for Nigeria based End
				
				//Added KYC Details Start
				AccountKycDetails accountKycDetails = new AccountKycDetails();
				accountKycDetails = new AccountKycDetails();
				accountKycDetails.setDateOfSubmission(new Date());
				
				accountKycDetails.setStrParticipantID(accountCreation.getStrParticipantID());
				accountKycDetails.setStrAccountType(accountCreation.getStrAccountType());
				accountKycDetails.setStrCustId(accountCreation.getStrCustId());
				
				accountKycDetails.setStrMobileNo(accountCreation.getStrMobileNo());
				
				accountKycDetails.setStrAddressProofDocumentType(accountCreation.getStrAddressProofDocumentId());
				accountKycDetails.setStrAddressDocumentValue(accountCreation.getStrAddressProofDocumentValue());
				accountKycDetails.setStrIdentityProofDocumentType(accountCreation.getStrIdentityProofDocumentId());
				accountKycDetails.setStrIdentityProofDocumentValue(accountCreation.getStrIdentityProofDocumentValue());
				
				if("YES".equalsIgnoreCase(accountCreation.getStrKycUpdateRequired()))
				{
					accountKycDetailsService.updateKycDetail(accountKycDetails);
				}
				else
				{
					accountKycDetailsService.saveAccountKycDetails(accountKycDetails);
				}
				//Added KYC Details End				
				
				AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
				accountTypeMaster.setStrAccountType(accountCreation.getStrAccountType()); //Acount type always save unique
				accountTypeMaster.setStrParticipantId(accountCreation.getStrParticipantID());
				
				accountTypeMaster = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
					
				if (accountTypeMaster.getStrIsAllowWallet().equalsIgnoreCase("Y")) //condition to create wallet based on allow wallet 
				{
					//Added Sub Account Creation Start
					int subAccountresult = accountMasterService.createSubAccountForWallet(accountCreation);
					System.out.println("subAccountresult::"+subAccountresult);
					//Added Sub Account Creation End
				}
				
				//Added for Updating Last Account Number Start				
				accountTypeMaster.setStrLastAccNumber(accountCreation.getStrAccountNumber());
				int i = accountTypeMasterService.updateLastAccountNumber(accountTypeMaster);
				System.out.println("Response of Updated Last Account Number::"+i);
				//Added for Updating Last Account Number End
				
				return ResponseEntity.ok(accountCreation.getStrID());
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
			System.out.println("Exception while storing accouninfo"+e);
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/updateAccountInfo", method = RequestMethod.POST)
	public ResponseEntity<?> processToUpdateAccountInfo(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			int count = accountMasterService.updateAccountCreation(accountCreation);
			if (count > 0) 
			{
				//Added KYC Details for updation Start
				AccountKycDetails accountKycDetails = new AccountKycDetails();
				accountKycDetails = new AccountKycDetails();
				accountKycDetails.setStrParticipantID(accountCreation.getStrParticipantID());
				accountKycDetails.setStrAccountType(accountCreation.getStrAccountType());
				accountKycDetails.setStrCustId(accountCreation.getStrAccountNumber());
								
				accountKycDetails.setStrAddressProofDocumentType(accountCreation.getStrAddressProofDocumentId());
				accountKycDetails.setStrAddressDocumentValue(accountCreation.getStrAddressProofDocumentValue());
				
				accountKycDetails.setStrIdentityProofDocumentType(accountCreation.getStrIdentityProofDocumentId());
				accountKycDetails.setStrIdentityProofDocumentValue(accountCreation.getStrIdentityProofDocumentValue());
			
				int cnt = accountKycDetailsService.updateKycDetailsInfo(accountKycDetails);
				if (cnt > 0) 
				{
					return ResponseEntity.ok(cnt +"");
				}
				//Added KYC Details for updation End
			}
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	@RequestMapping(value = "/updateInstantAccountInfo", method = RequestMethod.POST)
	public ResponseEntity<?> updateInstantAccountInfo(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			Date birthDate = dt.parse(accountCreation.getStrDOB());
			accountCreation.setBirthDate(birthDate);
			
			int count = accountMasterService.updateAccountCreationFromInstanceAccount(accountCreation);
			if (count > 0) 
			{
				//Added KYC Details for updation Start
				AccountKycDetails accountKycDetails = new AccountKycDetails();
				accountKycDetails.setStrParticipantID(accountCreation.getStrParticipantID());
				accountKycDetails.setStrAccountType(accountCreation.getStrAccountType());
				accountKycDetails.setStrCustId(accountCreation.getStrAccountNumber());
				
				accountKycDetails.setStrMobileNo(accountCreation.getStrMobileNo());
								
				accountKycDetails.setStrAddressProofDocumentType(accountCreation.getStrAddressProofDocumentId());
				accountKycDetails.setStrAddressDocumentValue(accountCreation.getStrAddressProofDocumentValue());
				
				accountKycDetails.setStrIdentityProofDocumentType(accountCreation.getStrIdentityProofDocumentId());
				accountKycDetails.setStrIdentityProofDocumentValue(accountCreation.getStrIdentityProofDocumentValue());
			
				//Added for instance account Start
				int cnt = 0;
				if (accountCreation.getStrIsInstantAccount() !=null && accountCreation.getStrIsInstantAccount().trim().equalsIgnoreCase("N"))
				{
					//accountKycDetails = accountKycDetailsService.saveAccountKycDetails(accountKycDetails);
					
					if("YES".equalsIgnoreCase(accountCreation.getStrKycUpdateRequired()))
					{
						cnt = accountKycDetailsService.updateKycDetail(accountKycDetails);
					}
					else
					{
						accountKycDetails = accountKycDetailsService.saveAccountKycDetails(accountKycDetails);
						if (accountKycDetails.getStrID()!=null) 
						{
							cnt = 1;
						}
					}
					
				}
				else 
				{
					cnt = accountKycDetailsService.updateKycDetailsInfo(accountKycDetails);
				}
				//Added for instance account End				
				if (cnt > 0) 
				{
					return ResponseEntity.ok(cnt +"");
				}
				//Added KYC Details for updation End
			}		
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAccountInfoListByParticipantId", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountInfoListByParticipantId(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			List<AccountCreation> accountInfoList = accountMasterService.getAccountInformationList(accountCreation);
			return ResponseEntity.ok(accountInfoList);
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAvailableBalBasedOnAccountTypeAndNumber", method = RequestMethod.POST)
	public ResponseEntity<?> getAvailableBalanceBasedOnAccountTypeAndNumber(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			String balanceAmtStr = accountMasterService.getAvailableBalanceBasedOnAccountTypeAndNumber(accountCreation);
			//if (balanceAmtStr == null)
			if (balanceAmtStr!=null && balanceAmtStr.trim().length() == 0)
			{
				balanceAmtStr = "0";
			}
			return ResponseEntity.ok(balanceAmtStr);
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAccountInfoListByAccountNo", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountInfoListByAccountNo(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			List<AccountCreation> accountInfoList = accountMasterService.getAccountInformationList(accountCreation);
			if (accountInfoList!=null && accountInfoList.size() > 0)
			{
				accountCreation = accountInfoList.get(0);
				//set birth Date in str format Start
				Date birthDate = accountCreation.getBirthDate();
				if (birthDate!=null) {
					String strDate = dt.format(birthDate);
					accountCreation.setStrDOB(strDate);
				}				
				//set birth Date in str format End
				
				AccountTypeMaster accountTypeMaster = new AccountTypeMaster();
				accountTypeMaster.setStrParticipantId(accountCreation.getStrParticipantID());
				accountTypeMaster.setStrAccountType(accountCreation.getStrAccountType());
				boolean isCreditTypeAccount = accountTypeMasterService.isCreditTypeAccount(accountTypeMaster);
				
				accountCreation.setStrIsCreditType(""+isCreditTypeAccount);//Added for specially instant account
				
				//set KYC Details for account wise Start
				AccountKycDetails accountKycDetails = new AccountKycDetails();
				accountKycDetails.setStrCustId(accountCreation.getStrAccountNumber());
				accountKycDetails = accountKycDetailsService.getSingleAccountKycDetail(accountKycDetails);
				
				if (accountKycDetails!=null)
				{
					accountCreation.setStrAddressProofDocumentId(accountKycDetails.getStrAddressProofDocumentType());
					accountCreation.setStrAddressProofDocumentValue(accountKycDetails.getStrAddressDocumentValue());
					
					accountCreation.setStrIdentityProofDocumentId(accountKycDetails.getStrIdentityProofDocumentType());
					accountCreation.setStrIdentityProofDocumentValue(accountKycDetails.getStrIdentityProofDocumentValue());
				}				
				//set KYC Details for account wise End
			}
			return ResponseEntity.ok(accountInfoList);
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByAccountNo::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAccountInfoListBasedOnTypes", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountInfoListBasedOnTypes(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			List<AccountCreation> accountInfoList = accountMasterService.getAccountInformationListByTypes(accountCreation);
			return ResponseEntity.ok(accountInfoList);
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAccountInformation", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountInformation(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			AccountCreation accountInfo = accountMasterService.getAccountInformation(accountCreation);
			return ResponseEntity.ok(accountInfo);
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/updateBalanceRelatedInfo", method = RequestMethod.POST)
	public ResponseEntity<?> updateBalanceRelatedInfo(@RequestBody AccountCreation accountCreation)
	{
		Integer result = null;
		try 
		{
			int count = accountMasterService.updateBalanceRelatedData(accountCreation);
			result = new Integer(count);
			return ResponseEntity.ok(result);
		}
		catch (Exception e) {
			System.out.println("Exception in updateBalanceRelatedInfo::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	
	//getAccountIssueData based on Is_linked_with_card Start
	@RequestMapping(value = "/getIssuedAccount", method = RequestMethod.POST)
	public ResponseEntity<?> getIssuedAccountBasedOnCardLinked(@RequestBody AccountCreation accountCreation)
	{
		Integer result = null;
		try 
		{
			List<AccountCreation> accountInfoList = accountMasterService.getAccountIssueData(accountCreation);
			return ResponseEntity.ok(accountInfoList);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	//getAccountIssueData based on Is_linked_with_card End
	
	@RequestMapping(value = "/getCreditCardOutstanding", method = RequestMethod.POST)
	public ResponseEntity<?> getgetCreditCardOutstandingAccountData(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			AccountCreation accountCreationData = accountMasterService.getOutstandingAccount(accountCreation);
			return ResponseEntity.ok(accountCreationData);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/updateAccountCardLinked", method = RequestMethod.POST)
	public ResponseEntity<?> updateAccountAfterLinkedCard(@RequestBody AccountCreation accountCreation)
	{
		Integer result = null;
		try 
		{
			int count = accountMasterService.updateAccountAfterLinkedCard(accountCreation);
			result = new Integer(count);
			return ResponseEntity.ok(result);
		}
		catch (Exception e) {
			System.out.println("Exception in updateAccountAfterLinkedCard::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(new Integer(0));
	}
	
	@RequestMapping(value = "/isAccountAlreadyExist", method = RequestMethod.POST)
	public ResponseEntity<?> isAccountAlreadyExist(@RequestBody AccountCreation accountCreation)
	{
		Boolean isAccountTypeExist = false;
		try 
		{
			isAccountTypeExist = accountMasterService.isAccountAlreadyExist(accountCreation);
			return ResponseEntity.ok(isAccountTypeExist);
		}
		catch (Exception e) {
			System.out.println("Exception in isAccountAlreadyExist::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(isAccountTypeExist);
	}
	
	@RequestMapping(value = "/isCustomerAccountAlreadyConfigured", method = RequestMethod.POST)
	public ResponseEntity<?> isCustomerAccountAlreadyConfigured(@RequestBody AccountCreation accountCreation)
	{
		Boolean isAccountTypeExist = false;
		try 
		{
			isAccountTypeExist = accountMasterService.isCustomerAccountAlreadyConfigured(accountCreation);
			return ResponseEntity.ok(isAccountTypeExist);
		}
		catch (Exception e) {
			System.out.println("Exception in isCustomerAccountAlreadyConfigured::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(isAccountTypeExist);
	}
	
		@RequestMapping(value = "/getTxn", method = RequestMethod.POST)
	public  ResponseEntity<?> viewAccountMasterDetails(@RequestBody AccountCreation accountCreation) 
	{
		String methodName = "viewAccountMasterDetails";
		try 
		{
			AccountCreation accountCreationData = accountMasterService.viewAccountMasterDetails(accountCreation);
			System.out.println("accountCreationData:::::"+accountCreationData.toString());
			return ResponseEntity.ok(accountCreationData);
			
		} 
		catch (Exception e)
		{
			System.out.println("Exception in updateAccountAfterLinkedCard::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return  ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/updateBalnceLimitValues", method = RequestMethod.POST)
	public ResponseEntity<?> updateBalnceLimitValues(@RequestBody AccountCreation accountCreation)
	{
		Integer result = null;
		try 
		{
			int count = accountMasterService.updateBalnceLimitValues(accountCreation);
			result = new Integer(count);
			return ResponseEntity.ok(result);
		}
		catch (Exception e) {
			System.out.println("Exception in updateBalnceLimitValues::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(new Integer(0));
	}
	
	//created by ankit
	@RequestMapping(value = "/getAccountBalanceAndName", method = RequestMethod.POST)
	public ResponseEntity<List<AccountCreation>> getAccountBalanceAndname(@RequestBody AccountCreation accountCreation)
	{
		try 
		{
			List<AccountCreation> accountBalanceAndName = accountMasterService.getAccountBalanceAndName(accountCreation);
			return ResponseEntity.ok(accountBalanceAndName);
		}
		catch (Exception e) {
			System.out.println("Exception in updateBalnceLimitValues::"+e);
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(null);
	}
	//created by ankit
	
	
	@RequestMapping(value = "/getRegCustWithLinkAccount", method = RequestMethod.POST)
	public ResponseEntity<?> getRegCustWithLinkAccount(@RequestBody AccountCreation accountCreation)
	{
		String result = null;
		try 
		{
			List<AccountCreation> accountInfoList = accountMasterService.getRegCustWithLinkAccount(accountCreation);
			return ResponseEntity.ok(accountInfoList);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(result);
	}
	@RequestMapping(value = "/getAccountInfoForClosingAccount", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountInfoForClosingAccount(@RequestBody AccountCreation accountCreation)
	{
		List<AccountMasterResponse> accountInfoList = null;
		try 
		{
			accountInfoList = accountMasterService.getAccountInformationForClosingAccount(accountCreation);
			if (accountInfoList!=null) 
			{
				return ResponseEntity.ok(accountInfoList);
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		accountInfoList = new ArrayList<AccountMasterResponse>();
		return ResponseEntity.ok(accountInfoList);
	}
	@RequestMapping(value = "/getFullAccountInfoBasedOnAccountNumber", method = RequestMethod.POST)
	public ResponseEntity<?> getFullAccountInfoBasedOnAccountNumber(@RequestBody AccountCreation accountCreation)
	{
		CustomerByAccountResponse customerAccountInfo = null;
		try 
		{
			if("NIGERIA".equalsIgnoreCase(CommonConstants.applicationName)) 
			{
				customerAccountInfo = accountMasterService.getCustomerAccountInfoByAccountNo(accountCreation);
			}
			else 
			{
				customerAccountInfo = accountMasterService.getCustomerAccountInfoByAccountNoForNonNigeria(accountCreation);
			}
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(customerAccountInfo);
	}
	@RequestMapping(value = "/getAccoutBalanceList", method = RequestMethod.POST)
	public ResponseEntity<?> getAccoutBalanceList(@RequestBody AccountCreation accountCreation)
	{
		ProcessResponse processResponse = new ProcessResponse();
		try 
		{
			accountCreation.setDate(Utils.getLocalDate());
			accountCreation.setTime(Utils.getCurrentSqlTime());
			List<AccountCreation> accountInfoList = accountMasterService.getAccoutBalanceList(accountCreation);
			if(accountInfoList != null && accountInfoList.size() > 0)
			{
				processResponse.setCode("S0000");
				processResponse.setStatus("success");
				processResponse.setMessage("Data Fetched Successfully");
				processResponse.setAccountBalanceList(accountInfoList);
			}
			else {
				processResponse.setCode("E0000");
				processResponse.setStatus("failed");
				processResponse.setMessage("Error While Fetching Data");
			}
			return ResponseEntity.ok(processResponse);
		}
		catch (Exception e) {
			amsLogger.writeExceptionLog("Exception occured::"+ExceptionUtils.getStackTrace(e));
		}
		return ResponseEntity.ok(processResponse);
	}
}
