package ams.cms.services;

import java.util.List;

import ams.cms.model.BillingCycleModel;

public interface BillingCycleService 
{
	List<BillingCycleModel> getBillingStatementlist(BillingCycleModel billingCycleModel) throws Exception;
	
	BillingCycleModel saveBillingCyleData(BillingCycleModel billingCycleModel) throws Exception;

}
