package ams.cms.api.dao;

import ams.cms.api.model.DynamicQrGeneration;
import ams.cms.api.model.QrInfo;
import ams.cms.dao.GenericDao;

public interface DynamicQrGenerationDao extends GenericDao<DynamicQrGeneration>
{
	int updateDynamicQrCode(DynamicQrGeneration dynamicQrGeneration);
	
	int updateQrCodeFieldsAfterTxn(DynamicQrGeneration dynamicQrGeneration);
}
