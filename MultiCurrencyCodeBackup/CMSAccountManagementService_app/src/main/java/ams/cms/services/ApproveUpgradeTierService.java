package ams.cms.services;

import java.util.List;

import ams.cms.api.model.CumulativelimitResponse;
import ams.cms.model.AccountTypeTierBasedLimit;
import ams.cms.model.ApproveUpgradeTier;

public interface ApproveUpgradeTierService 
{
	List<ApproveUpgradeTier> getUpgradeTierType(ApproveUpgradeTier approveUpgradeTier);
	
	List<CumulativelimitResponse> getAccountTypeTierBasedLimit(AccountTypeTierBasedLimit accountTypeTierBasedLimit);
}
