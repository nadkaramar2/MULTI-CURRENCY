package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.NUBANAccountDto;
import ams.cms.api.model.NubanCodeConfig;

public interface NubanCodeConfigService 
{
	int addNubanCodeConfig(NubanCodeConfig nubanCodeConfig);

	void addNewNubanCodeConfig(NubanCodeConfig nubanCodeConfig);

	List<NubanCodeConfig> getNubanCodeConfig(NubanCodeConfig nubanCodeConfig);

	NUBANAccountDto getAccountNo(NUBANAccountDto nUBANAccountDto);

	int updateNubanSerialNo(NubanCodeConfig nubanCodeConfig);

	//created by ankit on 31-05-2023
	boolean isConfigCodeExists(NubanCodeConfig nubanCodeConfig);

	NubanCodeConfig getNubanConfigObjectBasedOnParameter(NubanCodeConfig nubanCodeConfig);
}
