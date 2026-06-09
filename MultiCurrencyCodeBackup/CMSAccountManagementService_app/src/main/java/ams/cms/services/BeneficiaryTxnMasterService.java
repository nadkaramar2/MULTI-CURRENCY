package ams.cms.services;

import ams.cms.model.BeneficiaryTxnMaster;

public interface BeneficiaryTxnMasterService 
{
	BeneficiaryTxnMaster saveBeneficiaryTxnMasterRecords(BeneficiaryTxnMaster beneficiaryTxnMaster) throws Exception;
	
	BeneficiaryTxnMaster getInfoByTxnID(BeneficiaryTxnMaster beneficiaryTxnMaster);
	
	int updateBeneficiaryTxnMaster(BeneficiaryTxnMaster beneficiaryTxnMaster) throws Exception;
}
