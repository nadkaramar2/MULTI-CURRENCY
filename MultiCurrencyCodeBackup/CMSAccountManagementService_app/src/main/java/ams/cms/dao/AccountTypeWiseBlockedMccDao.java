package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountTypeWiseBlockedMccMaster;

public interface AccountTypeWiseBlockedMccDao extends GenericDao<AccountTypeWiseBlockedMccMaster> 
{
	int[] batchEntryforAccountTypeWiseBlockedMcc(List<AccountTypeWiseBlockedMccMaster> accountTypeWiseBlockedMccMasters);
	
	List<AccountTypeWiseBlockedMccMaster> getAccountTypeWiseBlockedMccList(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster);
	
	List<AccountTypeWiseBlockedMccMaster> getAccountTypeWiseUnBlockedMccMaster(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster);

	
	//prashant
	List<AccountTypeWiseBlockedMccMaster> getBlockMccListAccountTypeWise(AccountTypeWiseBlockedMccMaster accountTypeWiseBlockedMccMaster); 
}
