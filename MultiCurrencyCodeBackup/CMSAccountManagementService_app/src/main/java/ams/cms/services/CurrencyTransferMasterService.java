package ams.cms.services;

import java.util.List;

import ams.cms.model.CurrencyTransferMaster;

public interface CurrencyTransferMasterService {
	
	public int addEntryInCurrencyTransferMaster(CurrencyTransferMaster currencyTransferMaster);
	
	List<CurrencyTransferMaster> getCurrencyMasterByTranId(CurrencyTransferMaster currencyTransferMaster);

}
