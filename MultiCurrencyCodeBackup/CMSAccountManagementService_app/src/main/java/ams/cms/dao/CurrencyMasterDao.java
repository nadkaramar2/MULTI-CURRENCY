package ams.cms.dao;

import java.util.List;

import ams.cms.model.CurrencyMaster;

public interface CurrencyMasterDao extends GenericDao<CurrencyMaster> {

	CurrencyMaster getCurrencyMasterByCurrencyCode(CurrencyMaster currencyMaster);

	List<CurrencyMaster> getCurrencyList();

	CurrencyMaster getCurrencyMasterByCurrencyCode();

	CurrencyMaster getCurrencyDetailByCurrencyCode(CurrencyMaster currencyMaster);

	
}
