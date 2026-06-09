package ams.cms.services;

import java.util.List;

import ams.cms.model.AccountInterestMaster;

public interface AccountInterestMasterService 
{
	List<AccountInterestMaster> getOutStandingInterestList(AccountInterestMaster accountInterestMaster) throws Exception;
}
