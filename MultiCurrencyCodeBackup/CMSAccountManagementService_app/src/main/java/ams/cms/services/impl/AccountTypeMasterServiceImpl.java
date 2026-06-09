package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.AccountMaster;
import ams.cms.dao.AccountTypeMasterDao;
import ams.cms.model.AccountTypeMaster;
import ams.cms.services.AccountTypeMasterService;

@Transactional
@Service
public class AccountTypeMasterServiceImpl implements AccountTypeMasterService
{
	@Autowired
	AccountTypeMasterDao accountTypeMasterDao;
	
	@Override
	public AccountTypeMaster saveAccountTypeMaster(AccountTypeMaster accountTypeMaster) throws Exception
	{
		accountTypeMasterDao.save(accountTypeMaster);
		return accountTypeMaster;
	}
	
	@Override
	public List<AccountTypeMaster> getAllAccountTypeMasterList() throws Exception
	{
		return accountTypeMasterDao.findAll();
	}
	
	@Override
	public AccountTypeMaster getAccountTypeObject(AccountTypeMaster accountTypeMster) throws Exception
	{
		return accountTypeMasterDao.getAccountTypeObject(accountTypeMster);
	}
	
	@Override
	public List<AccountTypeMaster> getNonCreditAccounTypeObject(AccountTypeMaster accountTypeMster) 
	{
		return accountTypeMasterDao.getNonCreditAccounTypeObject(accountTypeMster);
	}
	
	@Override
	public int updateLastAccountNumber(AccountTypeMaster accountTypeMster) throws Exception {
		return accountTypeMasterDao.updateLastAccountNumber(accountTypeMster);
	}
	
	@Override
	public String getLastAccounNumberBasedOnAccountType(String accountType) throws Exception {
		return accountTypeMasterDao.getLastAccounNumberBasedOnAccountType(accountType);
	}
	
	@Override
	public List<AccountTypeMaster> getAccountTypeMastersByParticipantWise(String participantId) throws Exception {
		return accountTypeMasterDao.getAccountTypeMastersByParticipantWise(participantId);
	}
	
	@Override
	public boolean isCreditTypeAccount(AccountTypeMaster accountTypeMster) throws Exception 
	{
		try 
		{
			accountTypeMster = accountTypeMasterDao.getAccountTypeObject(accountTypeMster);
			if (accountTypeMster.getStrIsCreditType()!=null && accountTypeMster.getStrIsCreditType().trim().equalsIgnoreCase("Y")) 
			{
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	@Override
	public boolean isAccounTypeAlreadyExist(AccountTypeMaster accountTypeMster) throws Exception 
	{
		return accountTypeMasterDao.isAccounTypeAlreadyExist(accountTypeMster);
	}

	@Override
	public List<AccountTypeMaster> getYCreditAccounTypeObject(AccountTypeMaster accountTypeMaster) 
	{
		return accountTypeMasterDao.getYCreditAccounTypeObject(accountTypeMaster);
	}
	
	@Override
	public int updateIsRevolvingCreditFromMcc(AccountTypeMaster accountTypeMaster) {
		
		return accountTypeMasterDao.updateIsRevolvingCreditFromMcc(accountTypeMaster);
	}

	@Override
	public int updateIsRevolvingCredit(AccountTypeMaster accountTypeMaster) {
		
		return accountTypeMasterDao.updateIsRevolvingCredit(accountTypeMaster);
	}
	
	@Override
	public String getisRevolingCredit(String accountType) {
		return accountTypeMasterDao.getisRevolingCredit(accountType);
	}
	
	@Override
	public List<AccountTypeMaster> getCreditAccounTypeObject(AccountTypeMaster accountTypeMaster) 
	{
		return accountTypeMasterDao.getCreditAccounTypeObject(accountTypeMaster);
	}

	//created by ankit
	@Override
	public List<AccountTypeMaster> getAccountDescription(AccountTypeMaster accountTypeMaster) {
		return accountTypeMasterDao.getAccountDescription(accountTypeMaster);
	}
	//created by ankit
	//created by ankit on 18-04-2023
	@Override
	public List<AccountTypeMaster> getOnlyAccountStatus(AccountTypeMaster accountTypeMaster) {
		return accountTypeMasterDao.getOnlyAccountStatus(accountTypeMaster);
	}
	//created by ankit on 18-04-2023
	
	//created by ankit on 18-04-2023
	@Override
	public List<AccountTypeMaster> getGLAccontNoAndType(AccountTypeMaster accountTypeMaster) {
		return accountTypeMasterDao.getGLAccountNoAndType(accountTypeMaster);
	}
	//created by ankit on 18-04-2023
	
	
	@Override
	public AccountTypeMaster getGLAccTypeAccNoOnAccType(AccountTypeMaster accountTypeMaster) 
	{
		return accountTypeMasterDao.getGLAccTypeAccNoOnAccType(accountTypeMaster);
	}
	
	@Override
	public boolean isGLAccountTypeAlreadyExist(AccountTypeMaster accountTypeMaster) 
	{
		return accountTypeMasterDao.isGLAccountTypeAlreadyExist(accountTypeMaster);
	}

	@Override
	public List<AccountTypeMaster> getAccountTypeMasterDetailsBasedOnAccountType(AccountTypeMaster accountTypeMaster) 
	{
		return accountTypeMasterDao.getAccountTypeMasterDetailsBasedOnAccountType(accountTypeMaster);
	}

	@Override
	public int updateAccountTypeDetails(AccountTypeMaster accountTypeMaster)
	{
		return accountTypeMasterDao.updateAccountTypeDetails(accountTypeMaster);
	}
	@Override
	public AccountTypeMaster getAccountTypeMasterDetailsBasedOnAccountMasterAccountType(AccountMaster accountTypeMaster) 
	{
		return accountTypeMasterDao.getAccountTypeMasterBasedOnAccountType(accountTypeMaster);
	}

	@Override
	public void updateNubanSerialNumberByAccountType(AccountTypeMaster accountTypeMaster)
	{
		accountTypeMasterDao.updateNubanSerialNumberByAccountType(accountTypeMaster);
	}

	@Override
	public List<AccountTypeMaster> getAllAccountTypeMasterCodeList()
	{
		return accountTypeMasterDao.findAll();		
	}

	@Override
	public boolean isCreditAccounType(AccountTypeMaster accountTypeMster) throws Exception 
	{
		return accountTypeMasterDao.isCreditAccounType(accountTypeMster);
	}
}
