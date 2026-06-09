package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import ams.cms.api.model.AccountTranMasterResponse;
import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.api.model.RefundRequest;
import ams.cms.api.model.ReverseTransactionRequest;
import ams.cms.config.AppInfo;
import ams.cms.config.EncryptDecryptConfig;
import ams.cms.dao.AccountTranMasterDao;
import ams.cms.model.AccountTranMaster;
import ams.cms.services.AccountTranMasterService;
import ams.cms.util.ApiSecretKeyUtility;
import ams.cms.util.ProcessResponse;

@Transactional
@Service
public class AccountTranMasterServiceImpl implements AccountTranMasterService
{
	@Autowired
	private	AccountTranMasterDao accountTranMasterDao;
	
	@Autowired
	private	AppInfo appInfo;
	
	@Override
	public AccountTranMaster addAccountTransactionData(AccountTranMaster accountTranMaster) 
	{
		try 
		{		
			accountTranMaster.setSwitchTxDate(new Date());
			
			accountTranMasterDao.save(accountTranMaster);
			return accountTranMaster;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	public List<AccountTranMasterResponse> getAccountTransactionlist(AccountTranMaster accountTranMaster) 
	{
		List<AccountTranMaster> accountTranMasterlist = accountTranMasterDao.getAccountTransactionlist(accountTranMaster);
		return getAccountTranMasterResponseList(accountTranMasterlist);
	}

	@Override
	public List<AccountTranMasterResponse> getAcctTransactionlist(AccountTranMaster accountTranMaster)
	{
		List<AccountTranMaster> accountTranMasterlist = accountTranMasterDao.getAcctTransactionlist(accountTranMaster);
		return getAccountTranMasterResponseList(accountTranMasterlist);
	}
	
	private List<AccountTranMasterResponse> getAccountTranMasterResponseList(List<AccountTranMaster> accountTranMasterlist) 
	{
		List<AccountTranMasterResponse> accountTranMasterResponseList = new ArrayList<AccountTranMasterResponse>();
		try
		{
			if (accountTranMasterlist!=null && accountTranMasterlist.size() > 0) 
			{
				for (int i = 0; i < accountTranMasterlist.size(); i++) 
				{
				   AccountTranMaster accountTxnMaster = accountTranMasterlist.get(i);
				
				   AccountTranMasterResponse mapaccountTxnMasterToAccountTranMasterResponse = mapaccountTxnMasterToAccountTranMasterResponse(accountTxnMaster);
				   accountTranMasterResponseList.add(mapaccountTxnMasterToAccountTranMasterResponse);
				}
				return accountTranMasterResponseList;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return accountTranMasterResponseList;
	}

	private AccountTranMasterResponse mapaccountTxnMasterToAccountTranMasterResponse(AccountTranMaster accountTxnMaster)
	{
		
		AccountTranMasterResponse accountTranMasterResponse=new AccountTranMasterResponse();
		accountTranMasterResponse.setStrTxn_id(accountTxnMaster.getStrTxn_id());
		accountTranMasterResponse.setStrSys_id(accountTxnMaster.getStrSys_id());
		accountTranMasterResponse.setStrTran_type(accountTxnMaster.getStrTran_type());
		accountTranMasterResponse.setStrAccountType(accountTxnMaster.getStrAccountType());
		accountTranMasterResponse.setStrFrom_account_number(accountTxnMaster.getStrFrom_account_number());
		accountTranMasterResponse.setStrTo_account_number(accountTxnMaster.getStrTo_account_number());
		accountTranMasterResponse.setStrTransaction_amount(accountTxnMaster.getStrTransaction_amount());
		
		accountTranMasterResponse.setStrLocal_tran_time(accountTxnMaster.getStrLocal_tran_time()+"");
		accountTranMasterResponse.setStrLocal_tran_date(accountTxnMaster.getStrLocal_tran_date()+"");
		
		accountTranMasterResponse.setStrClosingBalance(accountTxnMaster.getStrClosingBalance());
		accountTranMasterResponse.setStrSwitchTxnDate(accountTxnMaster.getStrSwitch_txn_date());
		
		return accountTranMasterResponse;
	}
	
	@Override
	public AccountTranMaster getAccountInfo(AccountTranMaster accountTranMaster) 
	{
		return accountTranMasterDao.getAccountInfo(accountTranMaster);
	}
	
	//created by ankit on 16-04-2023
	@SuppressWarnings("unused")
	@Override
	public List<AccountTranMaster> verifyTransactionForRefund(RefundRequest refundRequest) {
		String strDateOfTransaction = refundRequest.getStrDateOfTransaction();
		String strTransactionId = refundRequest.getStrTransactionId();
		AccountTranMaster accountTranMaster = new AccountTranMaster();
		accountTranMaster.setStrTxn_id(strTransactionId);
		List<AccountTranMaster> transactionIfExist = accountTranMasterDao.getTransactionIfExist(accountTranMaster);
		return transactionIfExist;
	}
	//created by ankit on 16-04-2023


	//created by ankit on 16-04-2023
	@Override
	public List<AccountTranMaster> verifyTransactionForReversal(ReverseTransactionRequest refundTransactionRequest) {
		String strOriginalTransactionId = refundTransactionRequest.getStrOriginalTransactionId();
		AccountTranMaster accountTranMaster = new AccountTranMaster();
		accountTranMaster.setStrTxn_id(strOriginalTransactionId);
		List<AccountTranMaster> transactionIfExist = accountTranMasterDao.getTransactionIfExist(accountTranMaster);
		return transactionIfExist;
	}
	//created by ankit on 16-04-2023
	
	
	@Override
	public AccountTranMaster addEntryInAccountTranMasterData(AccountTranMaster accountTranMaster) 
	{
		String participantId = appInfo.getStrParticipantId();
		accountTranMaster.setStrParticipantId(participantId);
		accountTranMasterDao.save(accountTranMaster);
		return accountTranMaster;
	}
	
	@Override
	public List<AccountTranMaster> getTxnAccountTypelist(AccountTranMaster accountTranMaster)
	{
		return accountTranMasterDao.getTxnAccountTypelist(accountTranMaster);
	}

	@Override
	public int updateAccountTranMasterColumns(AccountTranMaster accountTranMaster) 
	{
		return accountTranMasterDao.updateAccountTranMasterColumns(accountTranMaster);
	}
	
	@Override
	public List<AccountTranMaster> getTransactionByTxnId(AccountTranMaster accountTranMaster) {
		return accountTranMasterDao.getTransactionByTxnId(accountTranMaster);
	}

	@Override
	public int[] batchEntryOfAccountTranMaster(List<AccountTranMaster> accountTranMasters) throws Exception {
		return accountTranMasterDao.batchEntryOfAccountTranMaster(accountTranMasters);
	}


	@Override
	public AccountTranMaster getAccountTranMaster(AccountTranMaster accountTranMaster) throws Exception 
	{
		return accountTranMasterDao.getAccountTranMaster(accountTranMaster);
	}


	@Override
	public List<AgencyBankingResponse> getAccountTranMasterWithAccountInfo(AccountTranMaster accountTranMaster) 
	{
		return accountTranMasterDao.getAccountTranMasterWithAccountInfo(accountTranMaster);
	}
	
	@Override
	public AccountTranMaster getAccountTranInfoData(AccountTranMaster accountTranMaster) throws Exception 
	{
		return accountTranMasterDao.getAccountTranInfoData(accountTranMaster);
	}
}
