package ams.cms.services;

import ams.cms.model.CurrencyConversionMaster;

public interface CurrencyConversionMasterService {

	CurrencyConversionMaster getCurrencyConverionValue(CurrencyConversionMaster currencyConversionMaster);

	CurrencyConversionMaster getCurrencyMasterByCurrencyCode(CurrencyConversionMaster currencyMaster);

}
