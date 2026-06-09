package ams.cms.services.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.CumulativelimitResponse;
import ams.cms.dao.ApproveUpgradeTierDao;
import ams.cms.model.AccountTypeTierBasedLimit;
import ams.cms.model.ApproveUpgradeTier;
import ams.cms.services.ApproveUpgradeTierService;

@Transactional
@Service
public class ApproveUpgradeTierServiceImpl implements ApproveUpgradeTierService
{
	@Autowired
	ApproveUpgradeTierDao approveUpgradeTierDao;
	
	@Override
	public List<ApproveUpgradeTier> getUpgradeTierType(ApproveUpgradeTier approveUpgradeTier) {
		return approveUpgradeTierDao.getUpgradeTierType(approveUpgradeTier);
	}
	
	@Override
	public List<CumulativelimitResponse> getAccountTypeTierBasedLimit(AccountTypeTierBasedLimit accountTypeTierBasedLimit) 
	{
		List<AccountTypeTierBasedLimit> accountTypeTierLimit = approveUpgradeTierDao.getAccountTypeTierBasedLimit(accountTypeTierBasedLimit);
        
		  List<CumulativelimitResponse> cumulativeLimitResponselist= new ArrayList<>();
	      for (int i = 0; i < accountTypeTierLimit.size(); i++) 
	      {
	    	  AccountTypeTierBasedLimit accountTierBasedLimit=accountTypeTierLimit.get(i);
	    	  
	    	  CumulativelimitResponse mapcumulativeLimitResponselistTocumulativeLimitResponselist = mapcumulativeLimitResponselistTocumulativeLimitResponselist(accountTierBasedLimit);
	    	  cumulativeLimitResponselist.add(mapcumulativeLimitResponselistTocumulativeLimitResponselist);
	    	  
		   }
		return cumulativeLimitResponselist;
	}
	private CumulativelimitResponse mapcumulativeLimitResponselistTocumulativeLimitResponselist(AccountTypeTierBasedLimit accountTierBasedLimit) 
	{
		CumulativelimitResponse cumulativelimitResponse = new  CumulativelimitResponse();
		cumulativelimitResponse.setStrDailyCumulativeTxnLimit(accountTierBasedLimit.getStrDailyCumulativeTxnLimit());
		cumulativelimitResponse.setStrCumulativeBalanceLimit(accountTierBasedLimit.getStrCumulativeBalanceLimit());
		cumulativelimitResponse.setStrAvailableDailyCumulativeLimit(accountTierBasedLimit.getStrAvailableDailyCumulativeLimit());
		cumulativelimitResponse.setStrMaxAssignDailyCumulativeLimit(accountTierBasedLimit.getStrMaxAssignDailyCumulativeLimit());
		
		return cumulativelimitResponse;
	}
}
