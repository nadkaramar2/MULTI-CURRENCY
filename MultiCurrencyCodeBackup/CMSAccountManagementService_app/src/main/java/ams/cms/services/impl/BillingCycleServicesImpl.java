package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.BillingCycleModelDao;
import ams.cms.model.BillingCycleModel;
import ams.cms.services.BillingCycleService;

@Service
@Transactional
public class BillingCycleServicesImpl implements BillingCycleService 
{

	@Autowired
	BillingCycleModelDao billingCycleModelDao;

	@Override
	public List<BillingCycleModel> getBillingStatementlist(BillingCycleModel billingCycleModel) throws Exception 
	{
		return billingCycleModelDao.getBillingStatementlist(billingCycleModel);
	}

	@Override
	public BillingCycleModel saveBillingCyleData(BillingCycleModel billingCycleModel) throws Exception {
		billingCycleModelDao.save(billingCycleModel);
		System.out.println("Record Instered Succesfully...........................");
		return billingCycleModel;
	}
}
