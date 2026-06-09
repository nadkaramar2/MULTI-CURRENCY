package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountInterestMaster;

public interface AccountInterestMasterDao extends GenericDao<AccountInterestMaster>
{
	List<AccountInterestMaster> getOutStandingInterestList(AccountInterestMaster accountInterestMaster);
}
