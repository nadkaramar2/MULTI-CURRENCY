package ams.cms.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.api.model.PreAccountMaster;
import ams.cms.api.service.PreAccountMasterService;
import ams.cms.services.AccountMasterService;
import ams.cms.utility.Utils;

@RestController
@RequestMapping("/preAccountMaster")
public class PreAccountMasterController 
{
	SimpleDateFormat dt = new SimpleDateFormat("yyyy/MM/dd");
	
	@Autowired
	PreAccountMasterService preAccountMasterService;
	
	@Autowired
	AccountMasterService accountMasterService;
	
	//added for fetching kyc details	
	@RequestMapping(value = "/getKycDataForverification", method = RequestMethod.POST)
	public ResponseEntity<?> getKycDataForverification(@RequestBody PreAccountMaster preAccountMaster)
	{
		String result = null;
		try 
		{
			PreAccountMaster accountInfo = preAccountMasterService.getKycDataForverification(preAccountMaster);
			return ResponseEntity.ok(accountInfo);
		}
		catch (Exception e) {
			System.out.println("PreAccountMasterController.getKycDataForverification()");
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/getPreAccount", method = RequestMethod.POST)
	public ResponseEntity<?> getPreAccountMaster(@RequestBody PreAccountMaster preAccountMaster)
	{
		try 
		{
			preAccountMaster = preAccountMasterService.getPreAccountMaster(preAccountMaster);
			Date birthDate = preAccountMaster.getBirthDate();
			
			if(birthDate!=null)
			{
				preAccountMaster.setStrDOB(dt.format(birthDate));
			}
			
			return ResponseEntity.ok(preAccountMaster);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/getPreAccountData", method = RequestMethod.POST)
	public ResponseEntity<?> getPreAccountData(@RequestBody PreAccountMaster preAccountMaster)
	{
		String result = null;
		try 
		{
			List<PreAccountMaster> preAccountMasters = preAccountMasterService.getPreAccountData(preAccountMaster);
			return ResponseEntity.ok(preAccountMasters);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/updatePreAccountAccount", method = RequestMethod.POST)
	public ResponseEntity<?> updatePreAccountAccount(@RequestBody PreAccountMaster preAccountMaster)
	{
		int resultCount = 0;
		try 
		{
			//resultCount = preAccountMasterService.updatePreAccountMaster(preAccountMaster);
			resultCount = preAccountMasterService.updatePreAccountMasterBasedOnParameter(preAccountMaster);
			return ResponseEntity.ok(resultCount);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(resultCount);
	}
	
	@RequestMapping(value = "/nonLinkedCustomerForAccountNo", method = RequestMethod.POST)
	public ResponseEntity<?> getNonLinkedCustomerForAccountNo(@RequestBody PreAccountMaster preAccountMaster)
	{
		String result = null;
		try 
		{
			List<PreAccountMaster> preAccountMasters = preAccountMasterService.getNonLinkedAccountNoList(preAccountMaster);
			return ResponseEntity.ok(preAccountMasters);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/nonLinkedCustomersForCutomerId", method = RequestMethod.POST)
	public ResponseEntity<?> getNonLinkedCustomersForCutomerId(@RequestBody PreAccountMaster preAccountMaster)
	{
		String result = null;
		try 
		{
			List<PreAccountMaster> preAccountMasters = preAccountMasterService.getNonLinkedCustomerList(preAccountMaster);
			return ResponseEntity.ok(preAccountMasters);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/updatePreAccountAccountBasedOnParam", method = RequestMethod.POST)
	public ResponseEntity<?> updatePreAccountAccountBasedOnParam(@RequestBody PreAccountMaster preAccountMaster)
	{
		int resultCount = 0;
		try 
		{
			resultCount = preAccountMasterService.updatePreAccountMasterBasedOnParameter(preAccountMaster);
			return ResponseEntity.ok(resultCount);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(resultCount);
	}
	
	@RequestMapping(value = "/insertDatalist", method = RequestMethod.POST)
	public ResponseEntity<?> preAccountInsertSavelist(@RequestBody PreAccountMaster preAccountMaster)
	{
		String result = null;
		try 
		{
			Date birthDate = dt.parse(preAccountMaster.getStrDOB());
			preAccountMaster.setBirthDate(birthDate);
			preAccountMaster.setStrDateOfCreation(Utils.getCurrentDate());
			preAccountMaster = preAccountMasterService.saveSignUpData(preAccountMaster);
		    return ResponseEntity.ok(preAccountMaster);					
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	//Added changes on 05-May-2023 Start
	@RequestMapping(value = "/updateMobileAgaintCustId", method = RequestMethod.POST)
	public ResponseEntity<?> UpdateReasonData(@RequestBody PreAccountMaster preAccountMaster)
	{
		String result = null;
		try 
		{
			int  txnCustIdcheck = preAccountMasterService.getUpdateMobileCustlist(preAccountMaster);
		    return ResponseEntity.ok(txnCustIdcheck);					
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}  

	@RequestMapping(value = "/updatekyc", method = RequestMethod.POST)
	public ResponseEntity<?> UpdateKycFlag(@RequestBody PreAccountMaster preAccountMaster)
	{
		String result = null;
		try 
		{
			int  kycUpdatecheck = preAccountMasterService.getUpdatePreAccountMasterKyclist(preAccountMaster);
		    return ResponseEntity.ok(kycUpdatecheck);					
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	//Added changes on 05-May-2023 Start
	
	@RequestMapping(value = "/registerCustomersList", method = RequestMethod.POST)
	public ResponseEntity<?> registerCustomersList(@RequestBody PreAccountMaster preAccountMaster)
	{
		String result= null;
		try 
		{
			List<PreAccountMaster> registercustomer = preAccountMasterService.getRegisterCustomers(preAccountMaster);
			return ResponseEntity.ok(registercustomer);
		}
		catch (Exception e) {
			System.out.println("Exception in getAccountInfoListByParticipantId::"+e);
			e.printStackTrace();
		}
		
		return ResponseEntity.ok(result);
	}
}
