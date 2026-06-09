package ams.cms.api.dao;

import ams.cms.api.model.DenominationValuesMaster;
import ams.cms.dao.GenericDao;

public interface DenominationValuesMasterDao extends GenericDao<DenominationValuesMaster>
{
		DenominationValuesMaster getDenominationValuesMasterBasedOnParameters(DenominationValuesMaster denominationValuesMaster); 
}
