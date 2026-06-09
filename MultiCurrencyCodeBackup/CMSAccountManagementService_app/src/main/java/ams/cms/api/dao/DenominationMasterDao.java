package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.DenominationMaster;
import ams.cms.dao.GenericDao;

public interface DenominationMasterDao extends GenericDao<DenominationMaster>
{
	List<DenominationMaster> getTxnDenominationDetails(DenominationMaster denominationMaster);

	List<DenominationMaster> getTxnDenominationDetailsbyAgent(DenominationMaster denominationMaster);
}
