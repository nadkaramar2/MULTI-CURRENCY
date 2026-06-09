package ams.cms.services.impl;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.dao.BeneficiaryTxnMasterDao;
import ams.cms.model.BeneficiaryTxnMaster;
import ams.cms.services.BeneficiaryTxnMasterService;

@Transactional
@Service
public class BeneficiaryTxnMasterServiceImpl implements BeneficiaryTxnMasterService
{
	@Autowired
	BeneficiaryTxnMasterDao beneficiaryTxnMasterDao;

	@Override
	public BeneficiaryTxnMaster saveBeneficiaryTxnMasterRecords(BeneficiaryTxnMaster beneficiaryTxnMaster) throws Exception 
	{
		beneficiaryTxnMasterDao.save(beneficiaryTxnMaster);
		return beneficiaryTxnMaster;
	}

	@Override
	public BeneficiaryTxnMaster getInfoByTxnID(BeneficiaryTxnMaster beneficiaryTxnMaster) 
	{
		beneficiaryTxnMaster = beneficiaryTxnMasterDao.getInfoByTxnID(beneficiaryTxnMaster);
		return beneficiaryTxnMaster;
	}

	@Override
	public int updateBeneficiaryTxnMaster(BeneficiaryTxnMaster beneficiaryTxnMaster) throws Exception 
	{
		return beneficiaryTxnMasterDao.updateBeneficiaryTxnMaster(beneficiaryTxnMaster);
	}
	
	
}
