package ams.cms.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.config.CommonConstants;
import ams.cms.model.AccountTransactionLimitation;
import ams.cms.model.AccountTypeMaster;
import ams.cms.model.AccountTypeTierBasedLimit;
import ams.cms.services.AccountTransactionLimitationService;
import ams.cms.services.AccountTypeMasterService;
import ams.cms.services.AccountTypeTierBasedLimitService;
import ams.cms.utility.Utils;


@RestController
@RequestMapping("/accountType")
public class AccountTypeMasterController 
{
	@Autowired
	AccountTypeMasterService accountTypeMasterService;
	
	@Autowired
	AccountTransactionLimitationService accountTransactionLimitationService;
	
	@Autowired
	AccountTypeTierBasedLimitService accountTypeTierBasedLimitService;
	
	
	@RequestMapping(value = "/saveAccountTypeInfo", method = RequestMethod.POST)
	public ResponseEntity<?> processToSaveAccountTypes(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		String result = null;
		try 
		{
			System.out.println("accountTypeMaster::"+accountTypeMaster);
			accountTypeMaster.setCreationDate(new Date());
			accountTypeMasterService.saveAccountTypeMaster(accountTypeMaster);
			
			//Added by Prashant to identify application for nigeria client on 10-May-2023 Start			
			if(!"NIGERIA".equalsIgnoreCase(CommonConstants.applicationName))
			{
				if (!"C".equalsIgnoreCase(accountTypeMaster.getStrAccountTypeCategory())) 
				{
					AccountTransactionLimitation accountTransactionLimitation = new AccountTransactionLimitation();	
					accountTransactionLimitation.setStrParticipantID(accountTypeMaster.getStrParticipantId());
					accountTransactionLimitation.setStrAccountType(accountTypeMaster.getStrAccountType());
					accountTransactionLimitation.setStrCreatedDate(new Date());
					accountTransactionLimitation.setStrSingleTxnLimit(accountTypeMaster.getStrSingleTxnLimit());
					accountTransactionLimitation.setStrDailyTxnLimit(accountTypeMaster.getStrDailyTxnLimit());
					accountTransactionLimitation.setStrMonthlyTxnLimit(accountTypeMaster.getStrMonthlyTxnLimit());
					accountTransactionLimitation.setStrYearlyTxnLimit(accountTypeMaster.getStrYearlyTxnLimit());
					accountTransactionLimitation.setStrCreatedBy(accountTypeMaster.getStrCreatedBy());
					
					accountTransactionLimitationService.saveAccountTransactionLimitation(accountTransactionLimitation);
				}
			}
			else
			{
				List<AccountTypeTierBasedLimit> accountTypeTierBasedLimitList = new ArrayList<AccountTypeTierBasedLimit>();
				AccountTypeTierBasedLimit accountTypeTierBasedLimit1 = new AccountTypeTierBasedLimit();
				
				accountTypeTierBasedLimit1.setStrAccountType(accountTypeMaster.getStrAccountType());
				accountTypeTierBasedLimit1.setStrTierType("Tier1");
				accountTypeTierBasedLimit1.setStrDailyCumulativeTxnLimit(accountTypeMaster.getStrDailyCumulativeTxnLimit1());
				accountTypeTierBasedLimit1.setStrCumulativeBalanceLimit(accountTypeMaster.getStrCumulativeBalanceLimit1());
				accountTypeTierBasedLimit1.setStrCreatedBy(accountTypeMaster.getStrCreatedBy());
				
				accountTypeTierBasedLimitList.add(accountTypeTierBasedLimit1);
				 
				AccountTypeTierBasedLimit accountTypeTierBasedLimit2 = new AccountTypeTierBasedLimit();
				
				accountTypeTierBasedLimit2.setStrAccountType(accountTypeMaster.getStrAccountType());
				accountTypeTierBasedLimit2.setStrTierType("Tier2");
				accountTypeTierBasedLimit2.setStrDailyCumulativeTxnLimit(accountTypeMaster.getStrDailyCumulativeTxnLimit2());
				accountTypeTierBasedLimit2.setStrCumulativeBalanceLimit(accountTypeMaster.getStrCumulativeBalanceLimit2());
				accountTypeTierBasedLimit2.setStrCreatedBy(accountTypeMaster.getStrCreatedBy());
				
				accountTypeTierBasedLimitList.add(accountTypeTierBasedLimit2);
				
				AccountTypeTierBasedLimit accountTypeTierBasedLimit3 = new AccountTypeTierBasedLimit();
				
				accountTypeTierBasedLimit3.setStrAccountType(accountTypeMaster.getStrAccountType());
				accountTypeTierBasedLimit3.setStrTierType("Tier3");
				accountTypeTierBasedLimit3.setStrDailyCumulativeTxnLimit(accountTypeMaster.getStrDailyCumulativeTxnLimit3());
				accountTypeTierBasedLimit3.setStrCumulativeBalanceLimit(accountTypeMaster.getStrCumulativeBalanceLimit3());
				accountTypeTierBasedLimit3.setStrCreatedBy(accountTypeMaster.getStrCreatedBy());
				
				accountTypeTierBasedLimitList.add(accountTypeTierBasedLimit3);
				
				accountTypeTierBasedLimitService.batchEntryForAccountTypeTierLimit(accountTypeTierBasedLimitList);
			}
			result = "success";
			return ResponseEntity.ok(result);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result = "Exception while save AccountTypes:"+e.getMessage();
		}
		return ResponseEntity.ok(result);
	}
	//Added by Prashant to identify application for nigeria client on 10-May-2023 End
		
	@RequestMapping(value = "/getAccountType", method = RequestMethod.GET)
	public ResponseEntity<?> processToGetAccountTypeList()
	{
		try 
		{
			List<AccountTypeMaster> listData = accountTypeMasterService.getAllAccountTypeMasterList();
			return ResponseEntity.ok(listData);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("{}");
	}
	
	@RequestMapping(value = "/getNonCreditAccounType", method = RequestMethod.POST)
	public ResponseEntity<?> getNonCreditAccounTypeObject(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		try 
		{
			List<AccountTypeMaster> listData = accountTypeMasterService.getNonCreditAccounTypeObject(accountTypeMaster);
			if ( listData != null && listData.size() > 0)
			{
				return ResponseEntity.ok(listData);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("{}");
	}
	
	@RequestMapping(value = "/getACTypeListByParticipantWise", method = RequestMethod.POST)
	public ResponseEntity<?> getAcccountTypeListByParticipantWise(@RequestBody String participantId)
	{
		try 
		{
			List<AccountTypeMaster> accountTypeMastersList = accountTypeMasterService.getAccountTypeMastersByParticipantWise(participantId);
			if (accountTypeMastersList != null && accountTypeMastersList.size() > 0)
			{
				return ResponseEntity.ok(accountTypeMastersList);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/getSingleAccountType", method = RequestMethod.POST)
	public ResponseEntity<?> processToGetSingleAccountTypeObject(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		try 
		{
			accountTypeMaster = accountTypeMasterService.getAccountTypeObject(accountTypeMaster);
			if (accountTypeMaster.getStrID() > 0)
			{
				return ResponseEntity.ok(accountTypeMaster);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/isAccountTypeExist", method = RequestMethod.POST)
	public ResponseEntity<?> isAccountTypeAlreadyExist(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		Boolean isAccountExist = false;
		try 
		{
			isAccountExist = accountTypeMasterService.isAccounTypeAlreadyExist(accountTypeMaster);
			return ResponseEntity.ok(isAccountExist);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(isAccountExist);
	}
	
	@RequestMapping(value = "/getLastAccounNumber", method = RequestMethod.POST)
	public ResponseEntity<?> getLastAccounNumberBasedOnAccountType(@RequestBody String accountType)
	{
		String result = null;
		try 
		{
			result = accountTypeMasterService.getLastAccounNumberBasedOnAccountType(accountType);
			if (result != null) 
			{
				return ResponseEntity.ok(result);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
			result = "Exception while save AccountTypes:"+e.getMessage();
		}
		return ResponseEntity.ok(result);
	}
	//Abhishek T-Start
	@RequestMapping(value = "/getYCreditAccounType", method = RequestMethod.POST)
	public ResponseEntity<?> getYCreditAccounTypeObject(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		try 
		{
			List<AccountTypeMaster> listData = accountTypeMasterService.getYCreditAccounTypeObject(accountTypeMaster);
			if ( listData != null && listData.size() > 0)
			{
				return ResponseEntity.ok(listData);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("{}");
	}
	//Abhishek T-End
	
	//Added by prashant for revolving credit account type
	@RequestMapping(value = "/getCreditAccountType", method = RequestMethod.POST)
	public ResponseEntity<?> getCreditAccountType(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		try 
		{
			List<AccountTypeMaster> listData = accountTypeMasterService.getCreditAccounTypeObject(accountTypeMaster);
			if (listData != null && listData.size() > 0)
			{
				return ResponseEntity.ok(listData);
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("{}");
	}
	//End
	
	@RequestMapping(value = "/updateIsRevolvingCreditFromMcc", method = RequestMethod.POST)
	public ResponseEntity<?> updateIsRevolvingCreditFromMcc(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		int resultCount = 0;
		try 
		{
			resultCount = accountTypeMasterService.updateIsRevolvingCreditFromMcc(accountTypeMaster);
			return ResponseEntity.ok(resultCount);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(resultCount);
	}
	
	@RequestMapping(value = "/updateIsRevolvingCredit", method = RequestMethod.POST)
	public ResponseEntity<?> updateIsRevolvingCredit(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		int resultCount = 0;
		try 
		{
			resultCount = accountTypeMasterService.updateIsRevolvingCredit(accountTypeMaster);
			return ResponseEntity.ok(resultCount);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(resultCount);
	}
	
	//created by ankit
	@RequestMapping(value = "/getAccDescription", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountDescription(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		
		try 
		{
			List<AccountTypeMaster> accountDescription = accountTypeMasterService.getAccountDescription(accountTypeMaster);
			return ResponseEntity.ok(accountDescription);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	//created by ankit
	
	
	@RequestMapping(value = "/isGLAccountTypeExist", method = RequestMethod.POST)
	public ResponseEntity<?> isGLAccountTypeAlreadyExist(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		Boolean isGLAccountTypeExist = false;
		try 
		{
			isGLAccountTypeExist = accountTypeMasterService.isGLAccountTypeAlreadyExist(accountTypeMaster);
			return ResponseEntity.ok(isGLAccountTypeExist);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(isGLAccountTypeExist);
	}
	
	@RequestMapping(value = "/getAccountTypeMasterDetailsBasedOnAccountType", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountTypeMasterDetailsBasedOnAccountType(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		String result = null;
		try 
		{
			List<AccountTypeMaster> accountTypeMaster2 = accountTypeMasterService.getAccountTypeMasterDetailsBasedOnAccountType(accountTypeMaster);
			
			if (accountTypeMaster2 != null && accountTypeMaster2.size() > 0)
			{
				accountTypeMaster = accountTypeMaster2.get(0);
				
				Date creationDate = accountTypeMaster.getCreationDate();
				if(creationDate != null)
				{
					String strDate = Utils.simpleDateFormat.format(creationDate);
					accountTypeMaster.setCreationDate(creationDate);
				}
			AccountTypeTierBasedLimit accountTypeTierBasedLimit = new AccountTypeTierBasedLimit();
			accountTypeTierBasedLimit.setStrAccountType(accountTypeMaster.getStrAccountType());
			accountTypeTierBasedLimit.setStrAccountNo(accountTypeMaster.getStrLastAccNumber());
			
			//accountTypeTierBasedLimit = accountTypeTierBasedLimitService.getTierBasedLimits(accountTypeTierBasedLimit);
			List<AccountTypeTierBasedLimit> accountTypeTierBasedLimit1 = accountTypeTierBasedLimitService.getTiersLimitsBasedonAccountType(accountTypeTierBasedLimit);
			
			if(accountTypeTierBasedLimit1!=null)
			{
				accountTypeMaster.setStrCumulativeBalanceLimit1(accountTypeTierBasedLimit1.get(0).getStrCumulativeBalanceLimit());
				accountTypeMaster.setStrDailyCumulativeTxnLimit1(accountTypeTierBasedLimit1.get(0).getStrDailyCumulativeTxnLimit());
				accountTypeMaster.setStrCumulativeBalanceLimit2(accountTypeTierBasedLimit1.get(1).getStrCumulativeBalanceLimit());
				accountTypeMaster.setStrDailyCumulativeTxnLimit2(accountTypeTierBasedLimit1.get(1).getStrDailyCumulativeTxnLimit());
				accountTypeMaster.setStrCumulativeBalanceLimit3(accountTypeTierBasedLimit1.get(2).getStrCumulativeBalanceLimit());
				accountTypeMaster.setStrDailyCumulativeTxnLimit3(accountTypeTierBasedLimit1.get(2).getStrDailyCumulativeTxnLimit());
			}
			
			}
			
			return ResponseEntity.ok(accountTypeMaster2);
			
		} 
		catch (Exception e) {
			System.out.println("AccountTypeMasterController.getAccountTypeMasterDetailsBasedOnAccountType()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/updateAccountTypeDetails", method = RequestMethod.POST)
	public ResponseEntity<?> updateAccountTypeDetails(@RequestBody AccountTypeMaster accountTypeMaster)
	{
		Integer result = null;
		try 
		{
			int count = accountTypeMasterService.updateAccountTypeDetails(accountTypeMaster);
			if(count > 0)
			{
				
				AccountTypeTierBasedLimit accountTypeTierBasedLimit = new AccountTypeTierBasedLimit();
				accountTypeTierBasedLimit = new AccountTypeTierBasedLimit();
				accountTypeTierBasedLimit.setStrAccountType(accountTypeMaster.getStrAccountType());
				
				List<AccountTypeTierBasedLimit> accountTypeTierBasedLimitList = new ArrayList<AccountTypeTierBasedLimit>();
				AccountTypeTierBasedLimit accountTypeTierBasedLimit1 = new AccountTypeTierBasedLimit();
				
				accountTypeTierBasedLimit1.setStrAccountType(accountTypeMaster.getStrAccountType());
				accountTypeTierBasedLimit1.setStrTierType("Tier1");
				accountTypeTierBasedLimit1.setStrDailyCumulativeTxnLimit(accountTypeMaster.getStrDailyCumulativeTxnLimit1());
				accountTypeTierBasedLimit1.setStrCumulativeBalanceLimit(accountTypeMaster.getStrCumulativeBalanceLimit1());
				//accountTypeTierBasedLimit1.setStrCreatedBy(accountTypeMaster.getStrCreatedBy());
				
				accountTypeTierBasedLimitList.add(accountTypeTierBasedLimit1);
				 
				AccountTypeTierBasedLimit accountTypeTierBasedLimit2 = new AccountTypeTierBasedLimit();
				
				accountTypeTierBasedLimit2.setStrAccountType(accountTypeMaster.getStrAccountType());
				accountTypeTierBasedLimit2.setStrTierType("Tier2");
				accountTypeTierBasedLimit2.setStrDailyCumulativeTxnLimit(accountTypeMaster.getStrDailyCumulativeTxnLimit2());
				accountTypeTierBasedLimit2.setStrCumulativeBalanceLimit(accountTypeMaster.getStrCumulativeBalanceLimit2());
				//accountTypeTierBasedLimit2.setStrCreatedBy(accountTypeMaster.getStrCreatedBy());
				
				accountTypeTierBasedLimitList.add(accountTypeTierBasedLimit2);
				
				AccountTypeTierBasedLimit accountTypeTierBasedLimit3 = new AccountTypeTierBasedLimit();
				
				accountTypeTierBasedLimit3.setStrAccountType(accountTypeMaster.getStrAccountType());
				accountTypeTierBasedLimit3.setStrTierType("Tier3");
				accountTypeTierBasedLimit3.setStrDailyCumulativeTxnLimit(accountTypeMaster.getStrDailyCumulativeTxnLimit3());
				accountTypeTierBasedLimit3.setStrCumulativeBalanceLimit(accountTypeMaster.getStrCumulativeBalanceLimit3());
				//accountTypeTierBasedLimit3.setStrCreatedBy(accountTypeMaster.getStrCreatedBy());
				
				accountTypeTierBasedLimitList.add(accountTypeTierBasedLimit3);
				
				accountTypeTierBasedLimitService.batchUpdateEntryForAccountTypeTierLimit(accountTypeTierBasedLimitList);
				
				if(accountTypeTierBasedLimitList!= null)
				{
					return ResponseEntity.ok(count +"");
				}
			}
			
		}
		catch (Exception e) {
			System.out.println("AccountTypeMasterController.updateAccountTypeDetails()"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getAccountTypeCode", method = RequestMethod.GET)
	public ResponseEntity<?> processToGetAccountTypeCodeList()
	{
		try 
		{
			List<AccountTypeMaster> listData = accountTypeMasterService.getAllAccountTypeMasterCodeList();
			return ResponseEntity.ok(listData);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok("{}");
	}
	
}
