package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountTypeTierBasedLimit;

public interface AccountTypeTierBasedLimitDao extends GenericDao<AccountTypeTierBasedLimit> 
{
	AccountTypeTierBasedLimit getTierBasedLimitsValues(AccountTypeTierBasedLimit accountTypeTierBasedLimit);

	int[] batchEntryForAccountTypeTierLimit(List<AccountTypeTierBasedLimit> accountTypeTierBasedLimit);
	
	List<AccountTypeTierBasedLimit> getTiersLimitsBasedonAccountType(AccountTypeTierBasedLimit accountTypeTierBasedLimit);
	
	int[] batchUpdateEntryForAccountTypeTierLimit(List<AccountTypeTierBasedLimit> accountTypeTierBasedLimitList);
}
