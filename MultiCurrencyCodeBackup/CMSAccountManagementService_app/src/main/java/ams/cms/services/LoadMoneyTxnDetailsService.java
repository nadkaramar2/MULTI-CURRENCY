package ams.cms.services;

import ams.cms.model.LoadMoneyTxnDetails;

public interface LoadMoneyTxnDetailsService {

	
	LoadMoneyTxnDetails getLoadDetailTxnByAccountNumber(LoadMoneyTxnDetails loadMoneyTxnDetails);

	void saveLoadMoney(LoadMoneyTxnDetails loadMoneyTxnDetails);

}
