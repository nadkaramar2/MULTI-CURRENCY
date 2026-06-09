package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.AccountCreditBalanceTxnResponse;
import ams.cms.api.model.AccountCreditCardInterestResponse;
import ams.cms.api.model.AccountCreditCardTxnResponse;
import ams.cms.api.model.AccountWiseInterestMasterResponse;
import ams.cms.dao.AccountCreditCardTxnDao;
import ams.cms.dao.AccountMasterDao;
import ams.cms.model.AccountCreation;
import ams.cms.model.AccountCreditCardTransactionModel;
import ams.cms.services.AccountCreditCardTxnServices;

@Transactional
@Service
public class AccountCreditCardTxnServicesImpl implements AccountCreditCardTxnServices
{
	@Autowired
	AccountCreditCardTxnDao accountCreditCardTxnDao;
	
	@Autowired
	AccountMasterDao accountMasterDao;

	@Override
	public AccountCreditCardTransactionModel addAccountCreditCardTransactionModel(AccountCreditCardTransactionModel accountCreditCardTransactionModel) throws Exception 
	{
		accountCreditCardTransactionModel.setTxnDate(new Date());
		accountCreditCardTxnDao.save(accountCreditCardTransactionModel);
		return accountCreditCardTransactionModel;
	}
	
	@Override
	public List<AccountCreditBalanceTxnResponse> getCreditCardTxn(AccountCreditCardTransactionModel accountCreditCardTransactionModel) 
	{
		  List<AccountCreditCardTransactionModel> creditBalancelist = accountCreditCardTxnDao.getCreditCardTxn(accountCreditCardTransactionModel);
		
		List<AccountCreditBalanceTxnResponse> CreditBalanceResponselist=new ArrayList<>();
		for (int i = 0; i < creditBalancelist.size(); i++) 
		{
			AccountCreditCardTransactionModel creditransaction = creditBalancelist.get(i);
			
			AccountCreditBalanceTxnResponse mapCreditCardBalanceToCreditBalanceResponse=mapCreditCardBalanceToCreditBalanceResponse(creditransaction);
			CreditBalanceResponselist.add(mapCreditCardBalanceToCreditBalanceResponse);
		}
		return  CreditBalanceResponselist;
	}

	private AccountCreditBalanceTxnResponse mapCreditCardBalanceToCreditBalanceResponse(AccountCreditCardTransactionModel creditransaction) {
		
		AccountCreditBalanceTxnResponse accountCreditBalanceTxnResponse=new AccountCreditBalanceTxnResponse();
		accountCreditBalanceTxnResponse.setStrMcc(creditransaction.getStrMcc());
		accountCreditBalanceTxnResponse.setStrTransactionAmount(creditransaction.getStrTransactionAmount());
		accountCreditBalanceTxnResponse.setStrTxnDate(creditransaction.getStrTxnDate());
		accountCreditBalanceTxnResponse.setStrTransactionType(creditransaction.getStrTransactionType());


		
		return accountCreditBalanceTxnResponse;
	}

	@Override
	public List<AccountCreditCardTxnResponse> getCreditCardBalancelist(AccountCreation accountCreation)
	{
	      List<AccountCreation> accountBalancelist = accountMasterDao.getCreditCardBalancelist(accountCreation);
	      
	      List<AccountCreditCardTxnResponse> accountCreditBalanceTxnResponselist= new ArrayList<>();
	      for (int i = 0; i < accountBalancelist.size(); i++) 
	      {
	    	  AccountCreation accountCreditCardTxn=accountBalancelist.get(i);
	    	  
	    	  AccountCreditCardTxnResponse mapCreditBalanceToAccountCreditCardTxnResponse=mapCreditBalanceToAccountCreditCardTxnResponse(accountCreditCardTxn);
	    	  accountCreditBalanceTxnResponselist.add(mapCreditBalanceToAccountCreditCardTxnResponse);
	    	  
		   }
		return accountCreditBalanceTxnResponselist;
	}

	private AccountCreditCardTxnResponse mapCreditBalanceToAccountCreditCardTxnResponse(AccountCreation accountCreditCardTxn)
	{
		AccountCreditCardTxnResponse accountCreditCardTxnResponse= new AccountCreditCardTxnResponse();
		accountCreditCardTxnResponse.setStrTotalOutstandingBal((accountCreditCardTxn.getStrTotalOutstandingBal() !=null && Double.parseDouble(accountCreditCardTxn.getStrTotalOutstandingBal()) > 0d) ? "-" + accountCreditCardTxn.getStrTotalOutstandingBal() : "0");
		return accountCreditCardTxnResponse;
	}

	@Override
	public List<AccountCreditCardInterestResponse> getCreditCardInterestlist(AccountCreation accountCreation) 
	{
		 List<AccountCreation> creditcardInterestlist=accountMasterDao.getCreditCardInterestlist(accountCreation);
	      
	      List<AccountCreditCardInterestResponse> accountCreditCardInterestResponselist= new ArrayList<>();
	      for (int i = 0; i < creditcardInterestlist.size(); i++) 
	      {
	    	  AccountCreation creditCardInterest=creditcardInterestlist.get(i);
	    	  
	    	  AccountCreditCardInterestResponse mapCreditInterestToCreditCardInterestResponse=mapCreditInterestToCreditCardInterestResponse(creditCardInterest);
	    	  accountCreditCardInterestResponselist.add(mapCreditInterestToCreditCardInterestResponse);
	    	  
		   }
		return accountCreditCardInterestResponselist;
	}

	private AccountCreditCardInterestResponse mapCreditInterestToCreditCardInterestResponse(AccountCreation creditCardInterest) {
		
		AccountCreditCardInterestResponse accountCreditCardInterestResponse = new AccountCreditCardInterestResponse();
		accountCreditCardInterestResponse.setStrTotalOutstandingInterest(creditCardInterest.getStrTotalOutstandingInterest());
		return accountCreditCardInterestResponse;
	}

	@Override
	public List<AccountWiseInterestMasterResponse> getCreditAccountWiseInterestList(ams.cms.scheduler.model.AccountWiseInterestMaster accountWiseInterestMaster) 
	{
		List<ams.cms.scheduler.model.AccountWiseInterestMaster> accountWiseInterestlist=accountMasterDao.getCreditAccountWiseInterestList(accountWiseInterestMaster);
		
		List<AccountWiseInterestMasterResponse> accountWiseInterestMasterResponselist = new ArrayList<AccountWiseInterestMasterResponse>();
		for (int i = 0; i < accountWiseInterestlist.size(); i++) 
		{
			ams.cms.scheduler.model.AccountWiseInterestMaster accountwiseInterest=accountWiseInterestlist.get(i);	
			
			AccountWiseInterestMasterResponse mapaccountwiseInterestToAccountWiseInterestlist=mapaccountwiseInterestToAccountWiseInterestlist(accountwiseInterest);
			accountWiseInterestMasterResponselist.add(mapaccountwiseInterestToAccountWiseInterestlist);
		}
		return accountWiseInterestMasterResponselist;	
}

private AccountWiseInterestMasterResponse mapaccountwiseInterestToAccountWiseInterestlist(ams.cms.scheduler.model.AccountWiseInterestMaster accountwiseInterest) 
{
	AccountWiseInterestMasterResponse accountWiseInterestMasterResponse =new AccountWiseInterestMasterResponse();
	accountWiseInterestMasterResponse.setStrMcc(accountwiseInterest.getStrMcc());
	accountWiseInterestMasterResponse.setStrTransactionAmount(accountwiseInterest.getStrTransactionAmount());
	accountWiseInterestMasterResponse.setStrCalculatInterest(accountwiseInterest.getStrCalculatInterest());
	accountWiseInterestMasterResponse.setStrInterestCalculateDate(accountwiseInterest.getStrCalculatInterest());
	
	
	return accountWiseInterestMasterResponse;
 }

@Override
public List<AccountCreditCardTransactionModel> getAccountCreditCardTxn(AccountCreditCardTransactionModel accountCreditCardTransactionModel) {
	return null;
}

@Override
public int addAccountCreditCardTxn(AccountCreditCardTransactionModel accountCreditCardTransactionModel) {
	
	 accountCreditCardTxnDao.save(accountCreditCardTransactionModel);
	 return Integer.parseInt(accountCreditCardTransactionModel.getStrID());
}

}
