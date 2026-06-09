package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.AccountTypeWiseBlockedMccDao;
import ams.cms.model.AccountTypeWiseBlockedMccMaster;
import ams.cms.model.ParticipantWiseWalletMaster;
import ams.cms.services.AccountTypeWiseBlockedMccService;

@Transactional
@Service
public class AccountTypeWiseBlockedMccServiceImpl implements AccountTypeWiseBlockedMccService
{
	@Autowired
	AccountTypeWiseBlockedMccDao accountTypeWiseBlockedMccDao;
	
	@Override
	public AccountTypeWiseBlockedMccMaster addAccountTypeWiseBlockedMcc(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) throws Exception 
	{
		try 
		{
			String mccCodecStr = accountTypeWiseBlockedMccMaster.getStrBlockedMCCCode();
			String participantId = accountTypeWiseBlockedMccMaster.getStrParticipantID();
			String loginUser = accountTypeWiseBlockedMccMaster.getStrCreatedBy();
			String accountType = accountTypeWiseBlockedMccMaster.getStrAccounType();
			
			String[] blockedMccCodeArr = {};
			if (mccCodecStr!=null && mccCodecStr.indexOf(",")!=-1)
			{
				blockedMccCodeArr = mccCodecStr.split(",");
			}
			System.out.println("addAccountTypeWiseWallet mccCodeArr::"+blockedMccCodeArr);
			
			if (blockedMccCodeArr.length > 0)
			{
				List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters = new ArrayList<AccountTypeWiseBlockedMccMaster>();
				for (int i = 0; i < blockedMccCodeArr.length; i++)
				{
					String blockedMccCode = blockedMccCodeArr[i];
					if (blockedMccCode!=null && blockedMccCode.trim().length() > 0)
					{
						AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMst = new AccountTypeWiseBlockedMccMaster();
						accountTypeWiseBlockedMccMst.setStrBlockedMCCCode(blockedMccCode);
						accountTypeWiseBlockedMccMst.setStrParticipantID(participantId);
						accountTypeWiseBlockedMccMst.setStrCreatedBy(loginUser);
						accountTypeWiseBlockedMccMst.setStrDateOfCreation(new Date());
						accountTypeWiseBlockedMccMst.setStrAccounType(accountType);
						
						accountTypeWiseBlockedMccMasters.add(accountTypeWiseBlockedMccMst);
					}
				}
				if (accountTypeWiseBlockedMccMasters.size() > 0) 
				{
					int[] resultArr = accountTypeWiseBlockedMccDao.batchEntryforAccountTypeWiseBlockedMcc(accountTypeWiseBlockedMccMasters);
					if (resultArr.length > 0)
					{
						accountTypeWiseBlockedMccMaster.setStrID(String.valueOf(resultArr));
					}
				}
			}
			else
			{
				accountTypeWiseBlockedMccMaster.setStrDateOfCreation(new Date());
				accountTypeWiseBlockedMccDao.save(accountTypeWiseBlockedMccMaster);
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return accountTypeWiseBlockedMccMaster;
	}

	@Override
	public List<AccountTypeWiseBlockedMccMaster> getAccountTypeWiseBlockedMccList(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) throws Exception
	{
		return accountTypeWiseBlockedMccDao.getAccountTypeWiseBlockedMccList(accountTypeWiseBlockedMccMaster);
	}
	
	@Override
	public List<AccountTypeWiseBlockedMccMaster> getAccountTypeWiseUnBlockedMccMaster(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) 
	{
		return accountTypeWiseBlockedMccDao.getAccountTypeWiseUnBlockedMccMaster(accountTypeWiseBlockedMccMaster);
	}

	
	//Prashant
	@Override
	public List<AccountTypeWiseBlockedMccMaster> getBlockMccListAccountTypeWise(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster) throws Exception {
	
		return accountTypeWiseBlockedMccDao.getBlockMccListAccountTypeWise(accountTypeWiseBlockedMccMaster);
	}
}
