package ams.cms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.services.AccountCreditCardTxnServices;

@RestController
@RequestMapping("/account-credit-card-txn")
public class AccountCreditCardTxnController 
{
	@Autowired
	private AccountCreditCardTxnServices accountCreditCardTxnServices;
	
	@RequestMapping(value = "/getCreditCardTxnWise", method = RequestMethod.POST)
	public ResponseEntity<?> getCreditCardTxnWise(@RequestBody AccountCreditCardTransactionModel accountCreditCardTransactionModel)
	{
		try 
		{
			List<AccountCreditCardTransactionModel> accountCreditLimitCategoriesList = accountCreditCardTxnServices.getAccountCreditCardTxn(accountCreditCardTransactionModel);
			return ResponseEntity.ok(accountCreditLimitCategoriesList);				
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return ResponseEntity.ok(null);
	}
}
