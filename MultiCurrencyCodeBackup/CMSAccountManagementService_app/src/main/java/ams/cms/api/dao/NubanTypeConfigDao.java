package ams.cms.api.dao;

import ams.cms.api.model.NubanTypeConfig;
import ams.cms.dao.GenericDao;

public interface NubanTypeConfigDao extends GenericDao<NubanTypeConfig>{

	 NubanTypeConfig findDescriptionByType(NubanTypeConfig nubanTypeConfig);

}
