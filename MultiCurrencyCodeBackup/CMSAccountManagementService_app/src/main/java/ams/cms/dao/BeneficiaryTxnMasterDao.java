package ams.cms.dao;

import ams.cms.model.BeneficiaryTxnMaster;

public interface BeneficiaryTxnMasterDao extends GenericDao<BeneficiaryTxnMaster>  
{
	BeneficiaryTxnMaster getInfoByTxnID(BeneficiaryTxnMaster beneficiaryTxnMaster);
	
	int updateBeneficiaryTxnMaster(BeneficiaryTxnMaster beneficiaryTxnMaster);
}
