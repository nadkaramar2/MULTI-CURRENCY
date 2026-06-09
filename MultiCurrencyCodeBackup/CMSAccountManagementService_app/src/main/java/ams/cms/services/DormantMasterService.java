package ams.cms.services;

import ams.cms.model.AccountCreation;
import ams.cms.model.DormantAccountMaster;
import ams.cms.util.ProcessResponse;

public interface DormantMasterService {
	
	
	ProcessResponse DormancyValidationProcess(AccountCreation accountMaster);
	
	ProcessResponse makerProcessForDormancy(DormantAccountMaster dormantAccountMaster);
	
	ProcessResponse makerProcessForMobileDormancy(AccountCreation accountCreation);
	
	ProcessResponse checkerProcessForDormancy(DormantAccountMaster dormantAccountMaster);

	ProcessResponse checkerProcessValidation(AccountCreation accountCreation);

	ProcessResponse dormantStatusByAccountNo(DormantAccountMaster dormantAccountMaster);
}
