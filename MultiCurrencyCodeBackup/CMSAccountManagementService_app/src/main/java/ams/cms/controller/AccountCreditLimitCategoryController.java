package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountCreditLimitCategory;
import ams.cms.services.AccountCreditLimitCategoryService;

@RestController
@RequestMapping("/credit_limit")
public class AccountCreditLimitCategoryController 
{
	@Autowired
	AccountCreditLimitCategoryService accountCreditLimitCategoryService;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<?> saveAccountCreditLimitCategory(@RequestBody AccountCreditLimitCategory accountCreditLimitCategory)
	{
		try 
		{
			accountCreditLimitCategory = accountCreditLimitCategoryService.saveAccountCreditLimitCategory(accountCreditLimitCategory);
			return ResponseEntity.ok(accountCreditLimitCategory);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	
	@RequestMapping(value = "/getCreditLimitListByParticipantWise", method = RequestMethod.POST)
	public ResponseEntity<?> getAcccountTypeListByParticipantWise(@RequestBody String participantId)
	{
		try 
		{
			List<AccountCreditLimitCategory> accountCreditLimitCategoriesList = accountCreditLimitCategoryService.getAccountCreditLimitCategoriesListByParticipantWise(participantId);
			if (accountCreditLimitCategoriesList!=null && accountCreditLimitCategoriesList.size() > 0)
			{
				return ResponseEntity.ok(accountCreditLimitCategoriesList);				
			}		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/getAccountCreditLimitCategoryObj", method = RequestMethod.POST)
	public ResponseEntity<?> getAccountCreditLimitCategoryObj(@RequestBody AccountCreditLimitCategory accountCreditLimitCategory)
	{
		try 
		{
			AccountCreditLimitCategory accCreditLimitCategory = accountCreditLimitCategoryService.getAccountCreditLimitCategoryObj(accountCreditLimitCategory);
			if (accCreditLimitCategory!=null )
			{
				return ResponseEntity.ok(accCreditLimitCategory);			
			}		
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
	
	@RequestMapping(value = "/isCreditTypeExist", method = RequestMethod.POST)
	public ResponseEntity<?> isCreditTypeAlreadyExist(@RequestBody AccountCreditLimitCategory accountCreditLimitCategory)
	{
		Boolean isCreditTypeExist = false;
		try 
		{
			isCreditTypeExist = accountCreditLimitCategoryService.isCreditTypeAlreadyExist(accountCreditLimitCategory);
			return ResponseEntity.ok(isCreditTypeExist);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseEntity.ok(isCreditTypeExist);
	}
	
	@RequestMapping(value = "/updateCreditTypeCategoryLimit", method = RequestMethod.POST)
	public ResponseEntity<?> updateAccountTypeCategoryInfo(@RequestBody AccountCreditLimitCategory accountCreditLimitCategory)
	{
		Integer result = null;
		try 
		{
			int count = this.accountCreditLimitCategoryService.updateAccountTypeCategoryInfo(accountCreditLimitCategory);
			result = new Integer(count);
			return ResponseEntity.ok(result);
		}
		catch (Exception e) 
		{
			System.out.println("Exception in updateCrdeitLimitCategoryBasedON Type ::"+e);
			e.printStackTrace();
		}
		return ResponseEntity.ok(result);
	}
	
}
