package ams.cms.dao;

import java.util.List;

import ams.cms.model.BillingCycleModel;

public interface BillingCycleModelDao extends GenericDao<BillingCycleModel>
{
	List<BillingCycleModel> getBillingStatementlist(BillingCycleModel billingCycleModel);

}
