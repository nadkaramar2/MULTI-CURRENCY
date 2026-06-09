package ams.cms.dao;

import java.util.List;

import ams.cms.model.AcTypeLrsTcsMaster;

public interface AcTypeLrsTcsMasterDao extends GenericDao<AcTypeLrsTcsMaster> {

	AcTypeLrsTcsMaster getAccountTypeLrsAndTcs(AcTypeLrsTcsMaster acTypeLrsTcsMaster);

	List<AcTypeLrsTcsMaster> getAccountTypeLrsBasedOnAccountType(AcTypeLrsTcsMaster acTypeLrsTcsMaster);

	List<AcTypeLrsTcsMaster> getAccountTypeTcsAndLrs(AcTypeLrsTcsMaster acTypeLrsTcsMaster);

}
