package ams.cms.api.dao;
import java.util.List;

import ams.cms.api.model.AccountLimitRequest;
import ams.cms.api.model.AccountMaster;
import ams.cms.dao.GenericDao;
import ams.cms.model.AccountCreation;


public interface AccountMasterApiDao extends GenericDao<AccountMaster>{

	AccountMaster getAvailableMonthlyAndDailyLimit(AccountLimitRequest accountLimitRequest);

	List<String> getAccTypeOnCustId(AccountMaster accountMaster);	
	
	AccountMaster getCreditFlagOnCustId(AccountMaster accountMaster);
	
	AccountMaster getUpdateOrAvailableTxnlimits(AccountMaster accontMaster);
	
	AccountMaster getUpdateOrAvailableCreditlimits(AccountMaster accontMaster);
	
	AccountMaster getCreditLimitAmount(AccountMaster accountMaster);

	int updatecreditLimitAmount(AccountMaster accountMaster);
}
