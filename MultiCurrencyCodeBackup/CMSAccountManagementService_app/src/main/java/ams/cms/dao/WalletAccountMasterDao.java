package ams.cms.dao;

import java.util.List;

import ams.cms.model.WalletAccountMaster;

public interface WalletAccountMasterDao extends GenericDao<WalletAccountMaster>
{
	int[] batchEntryOfWalletAccountMaster(List<WalletAccountMaster> walletAccountMasters);
	
	List<WalletAccountMaster> getWalletBalancelist(WalletAccountMaster walletAccountMaster);

	List<WalletAccountMaster> getWalletShowBalance(WalletAccountMaster walletAccountMaster);
	
	List<WalletAccountMaster> getLinkedAccountWalletList(WalletAccountMaster walletAccountMaster);
	
	//Wallet Percentage Edit By Jyoti S
	int updateWalletPercentage(WalletAccountMaster walletAccountMaster);
	
	
}
