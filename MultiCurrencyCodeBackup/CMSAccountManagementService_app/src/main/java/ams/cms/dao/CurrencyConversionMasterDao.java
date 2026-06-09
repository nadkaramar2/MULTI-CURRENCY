package ams.cms.dao;

import ams.cms.model.CurrencyConversionMaster;
import ams.cms.model.CurrencyMaster;

public interface CurrencyConversionMasterDao extends GenericDao<CurrencyConversionMaster> {

	CurrencyConversionMaster getCurrencyConverionValue(CurrencyConversionMaster currencyConversionMaster);

	CurrencyConversionMaster getCurrencyMasterByCurrencyCode(CurrencyConversionMaster currencyMaster);

}
