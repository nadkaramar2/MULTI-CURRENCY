package ams.cms.api.service;
import java.util.List;

import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountMaster;

public interface AccountMasterApiService
{
	AccountMaster getAvailableMonthlyAndDailyLimit(AccountLimitRequest accountLimitRequest);

	List<String> getAccTypeOnCustId(AccountMaster accountMaster) throws Exception;

	AccountMaster getCreditFlagOnCustId(AccountMaster accountMaster);
	
	AccountMaster getUpdateOrAvailableTxnlimits(AccountMaster accontMaster) throws Exception;
	
	AccountMaster getUpdateOrAvailableCreditlimits(AccountMaster accontMaster);
	
	AccountMaster getCreditLimitAmount(AccountMaster accountMaster);

	int updatecreditLimitAmount(AccountMaster accountMaster);
}
