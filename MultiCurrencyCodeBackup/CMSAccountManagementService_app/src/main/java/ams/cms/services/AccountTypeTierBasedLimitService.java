package ams.cms.services;

import java.util.List;

import ams.cms.model.AccountTypeTierBasedLimit;

public interface AccountTypeTierBasedLimitService 
{
	AccountTypeTierBasedLimit getTierBasedLimits(AccountTypeTierBasedLimit accountTypeTierBasedLimit);
	
	int[] batchEntryForAccountTypeTierLimit(List<AccountTypeTierBasedLimit> accountTypeTierBasedLimit);
	
	List<AccountTypeTierBasedLimit> getTiersLimitsBasedonAccountType(AccountTypeTierBasedLimit accountTypeTierBasedLimit);
	
	int[] batchUpdateEntryForAccountTypeTierLimit(List<AccountTypeTierBasedLimit> accountTypeTierBasedLimitList);
}
