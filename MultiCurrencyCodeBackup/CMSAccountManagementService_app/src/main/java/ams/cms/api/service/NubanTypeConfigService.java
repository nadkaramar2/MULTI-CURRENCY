package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.NubanTypeConfig;



public interface NubanTypeConfigService {

	List<NubanTypeConfig> getNubanTypes();

	NubanTypeConfig getNubanTypeDescription(NubanTypeConfig nubanTypeConfig);

}
