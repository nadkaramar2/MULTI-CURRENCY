package ams.cms.dao;

import java.util.List;

import ams.cms.model.CurrencyTransferMaster;

public interface CurrencyTransferMasterDao extends GenericDao<CurrencyTransferMaster>{

	List<CurrencyTransferMaster> getCurrencyMasterByTranId(CurrencyTransferMaster currencyTransferMaster);
	
	
}
