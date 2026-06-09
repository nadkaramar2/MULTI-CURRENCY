package ams.cms.services;

import ams.cms.model.CurrencyMaster;
import ams.cms.util.ProcessResponse;

public interface CurrencyMasterService {

	CurrencyMaster getCurrencyMasterByCurrencyCode(CurrencyMaster currencyMaster);

	ProcessResponse getCurrencyList();

	CurrencyMaster getBaseCurrencyFromCurrencyMaster();

	CurrencyMaster getCurrencyDetailByCurrencyCode(CurrencyMaster currencyToMaster);

	
}
