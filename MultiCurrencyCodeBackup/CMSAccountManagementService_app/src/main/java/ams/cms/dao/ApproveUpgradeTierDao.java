package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountTypeTierBasedLimit;
import ams.cms.model.ApproveUpgradeTier;

public interface ApproveUpgradeTierDao 
{
	List<ApproveUpgradeTier> getUpgradeTierType(ApproveUpgradeTier approveUpgradeTier);

	List<AccountTypeTierBasedLimit> getAccountTypeTierBasedLimit(AccountTypeTierBasedLimit accountTypeTierBasedLimit);
}
