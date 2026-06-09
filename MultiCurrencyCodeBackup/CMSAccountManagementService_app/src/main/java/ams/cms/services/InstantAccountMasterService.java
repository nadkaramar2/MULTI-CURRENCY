package ams.cms.services;

import ams.cms.model.InstantAccountCreation;

public interface InstantAccountMasterService {

	void saveInstantAccountInformation(InstantAccountCreation instantAccountCreation) throws Exception;
	
	int createInstantAccount(InstantAccountCreation instantAccountCreation) throws Exception;
}
