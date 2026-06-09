package ams.cms.api.dao;

import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountTransactionLimit;

public interface AccountTransactionLimitDao {

	AccountTransactionLimit getMonthlyAndDailyTransactionLimit(AccountLimitRequest accountLimitRequest);

	AccountTransactionLimit getAccountTransactionLimitation(AccountLimitRequest accountLimitRequest);

}
