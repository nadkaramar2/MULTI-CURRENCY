package ams.cms.api.service;

import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountTransactionLimit;

public interface AccountTransactionLimitService {

	AccountTransactionLimit getMonthlyAndDailyTransactionLimit(AccountLimitRequest accountLimitRequest);


	AccountTransactionLimit getLimitDetails(AccountLimitRequest accountLimitRequest);

}
