package ams.cms.api.dao;

import java.util.List;
import ams.cms.api.model.NUBANAccountDto;
import ams.cms.api.model.NubanCodeConfig;
import ams.cms.dao.GenericDao;

public interface NubanCodeConfigDao extends GenericDao<NubanCodeConfig>{

	int addNewNubanCodeConfig(NubanCodeConfig nubanCodeConfig);

	List<NubanCodeConfig> findSerialNoByUser(NubanCodeConfig nubanCodeConfig);

	boolean isNubanCodeExists(NubanCodeConfig nubanCodeConfig);

	void addNewNubanCodeConfig();

	List<NubanCodeConfig> getNubanCodeConfig(NubanCodeConfig nubanCodeConfig);

	NubanCodeConfig getNubanCodeConfigObject(NubanCodeConfig nubanCodeConfig);
	
	NubanCodeConfig getNubanCodeConfigObjectByNubanCode(NubanCodeConfig nubanCodeConfig);
	
	NubanCodeConfig getNubanConfigObjectBasedOnParameter(NubanCodeConfig nubanCodeConfig);

	int updateNubanSerialNoByNubanCode(NubanCodeConfig nubanCodeConfig);

	void isConfigCodeExists(NUBANAccountDto nUBANAccountDto);
}
