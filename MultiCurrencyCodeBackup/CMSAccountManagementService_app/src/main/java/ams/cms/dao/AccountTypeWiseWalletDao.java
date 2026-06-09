package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountTypeWiseWalletMaster;

public interface AccountTypeWiseWalletDao extends GenericDao<AccountTypeWiseWalletMaster>
{
	int[] batchEntryforAccountTypeWiseWallet(List<AccountTypeWiseWalletMaster> listOfAccountTypeWiseWalletMasters);
	
	List<AccountTypeWiseWalletMaster> getAccountTypeWiseWalletMasterList(AccountTypeWiseWalletMaster accountTypeWiseWalletMaster);
}
