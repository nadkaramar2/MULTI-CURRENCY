package ams.cms.services;

import java.util.List;

import ams.cms.model.AcTypeLrsTcsMaster;
import ams.cms.model.AccountCreation;
import ams.cms.model.LrsView;
import ams.cms.util.ProcessResponse;


public interface AcTypeLrsTcsMasterService {
	
	AcTypeLrsTcsMaster addLrsTcs(AcTypeLrsTcsMaster acTypeLrsTcsMaster);

	AcTypeLrsTcsMaster getAccountTypeLrsAndTcs(AcTypeLrsTcsMaster acTypeLrsTcsMaster);

	ProcessResponse checkLrsLimit(AcTypeLrsTcsMaster accountCreation);

	List<AcTypeLrsTcsMaster> getAccountTypeTcsAndLrs(AcTypeLrsTcsMaster acTypeLrsTcsMaster);

	void createMultiCurrencyFinancialYearMasterForAccount(AccountCreation accountCreation);
}
