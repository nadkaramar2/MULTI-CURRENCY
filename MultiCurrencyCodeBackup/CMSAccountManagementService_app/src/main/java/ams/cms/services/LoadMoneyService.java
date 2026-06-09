package ams.cms.services;

import ams.cms.model.LoadMoneyRequest;
import ams.cms.model.TransaferCurrencyRequest;
import ams.cms.util.ProcessResponse;

public interface LoadMoneyService {

	ProcessResponse multiCurrencyLoadMoneyTxn(LoadMoneyRequest loadMoneyRequest);

	ProcessResponse getLoadMoneyPayStructure(LoadMoneyRequest loadMoneyRequest);

	ProcessResponse transferCurrency(TransaferCurrencyRequest transferCurrency);

	
}
