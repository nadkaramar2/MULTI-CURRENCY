package ams.cms.services;



import ams.cms.model.CloseAccountMaster;
import ams.cms.model.CloseAccountResponse;

public interface CloseAccountMasterService {

	CloseAccountResponse saveCloseAccountData(CloseAccountMaster closeAccountMaster);
	
	CloseAccountMaster saveCloseAccountMaster(CloseAccountMaster closeAccountMaster);

	CloseAccountResponse makerProcessToAccountClouser(CloseAccountMaster closeAccountMaster) throws Exception;

	
	
}
