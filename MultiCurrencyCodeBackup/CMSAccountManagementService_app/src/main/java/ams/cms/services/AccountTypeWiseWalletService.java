package ams.cms.services;

import java.util.List;

import ams.cms.model.AccountTypeWiseWalletMaster;

public interface AccountTypeWiseWalletService 
{
	AccountTypeWiseWalletMaster addAccountTypeWiseWallet(AccountTypeWiseWalletMaster accountTypeWiseWalletMaster) throws Exception;
	List<AccountTypeWiseWalletMaster> getAccountTypeWiseWalletMasterList(AccountTypeWiseWalletMaster accountTypeWiseWalletMaster) throws Exception;
}
