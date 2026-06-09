package ams.cms.services;

import java.util.List;

import ams.cms.api.model.WalletBalanceResponse;
import ams.cms.api.model.WalletBalanceShowResponse;
import ams.cms.model.WalletAccountMaster;
import ams.cms.util.ProcessResponse;

public interface WalletAccountMasterService
{
	int createWalletAccountBasedOnMccWise(List<WalletAccountMaster> walletAccountMasters) throws Exception;
	
	String generateWalletAccountNumber(WalletAccountMaster walletAccountMaster) throws Exception;
	
	List<WalletBalanceResponse> getWalletBalancelist(WalletAccountMaster walletAccountMaster) throws Exception;

	List<WalletBalanceShowResponse> getWalletShowBalance(WalletAccountMaster walletAccountMaster);
	
	List<WalletAccountMaster> getLinkedAccountWalletList(WalletAccountMaster walletAccountMaster) throws Exception;
	
	ProcessResponse getWalletShowBalance(WalletAccountMaster walletAccountMaster, ProcessResponse processResponse) throws Exception;
	
	int updateWalletPercentage(WalletAccountMaster walletAccountMaster) throws Exception;
}
