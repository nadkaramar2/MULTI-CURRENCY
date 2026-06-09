package ams.cms.dao;

import ams.cms.model.MontraAccountMaster;

public interface MontraAccountMasterDao extends GenericDao<MontraAccountMaster>
{
	MontraAccountMaster findMontraAccountMasterByCustIdAndMontraId(MontraAccountMaster montraRequestModel);
	
	MontraAccountMaster findByMontraId(MontraAccountMaster montraAccountMaster);
	
	MontraAccountMaster getMontraAccountMasterInstance(MontraAccountMaster montraAccountMaster);
	
	MontraAccountMaster validateBid(MontraAccountMaster montraAccountMaster);
	
	MontraAccountMaster findByBid(MontraAccountMaster montraAccountMaster);

	MontraAccountMaster findByMontraCid(MontraAccountMaster montraAccountMaster);

	MontraAccountMaster validateMCCAgainstAccountType(MontraAccountMaster montraAccountMaster);
	
	MontraAccountMaster findMontraAccountMasterByCustIdAccountTypeAndMontraId(MontraAccountMaster montraAccountMaster);
	
	MontraAccountMaster accountInfoByMontraCid(MontraAccountMaster montraAccountMaster);
}
