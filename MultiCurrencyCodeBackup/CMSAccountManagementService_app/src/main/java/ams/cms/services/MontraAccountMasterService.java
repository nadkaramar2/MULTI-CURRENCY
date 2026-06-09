package ams.cms.services;

import ams.cms.model.MontraAccountMaster;
import ams.cms.util.ProcessResponse;

public interface MontraAccountMasterService 
{
    ProcessResponse validateMontraIdAndCustId(MontraAccountMaster montraAccountMaster);
	
    ProcessResponse validateMontraId(MontraAccountMaster montraAccountMaster);
	
    MontraAccountMaster getMontraAccountMasterInstance(MontraAccountMaster montraAccountMaster) throws Exception;
    
    ProcessResponse validateMontraIdAndCustId(String cid, String custId);
    
    ProcessResponse validateCid(MontraAccountMaster montraAccountMaster);
    
    ProcessResponse validateBid(MontraAccountMaster montraAccountMaster);
    
    ProcessResponse validateMontraRequest(ProcessResponse processResponse, MontraAccountMaster montraAccountMaster);

	ProcessResponse validateMontraIdAccountTypeAndCustId(MontraAccountMaster montraAccountMaster);
	
	MontraAccountMaster findMontraAccountMasterByCustIdAndMontraId(MontraAccountMaster montraRequestModel);
	
	ProcessResponse validateMccForAcc(ProcessResponse processResponse, MontraAccountMaster montraAccountMaster);
	
	MontraAccountMaster addMontraAccountMaster(MontraAccountMaster montraAccountMaster);
	
	ProcessResponse accountInfoByCid(MontraAccountMaster montraAccountMaster);
}
