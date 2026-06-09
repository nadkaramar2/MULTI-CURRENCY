package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.VatTypeMaster;

public interface VatTypeMasterDao {

	List<VatTypeMaster> getVatCollectedBalance(VatTypeMaster vatTypeMaster);

}
