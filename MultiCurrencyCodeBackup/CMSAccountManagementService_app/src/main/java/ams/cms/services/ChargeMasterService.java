package ams.cms.services;

import java.util.List;

import ams.cms.model.AccountTypeCharges;
import ams.cms.model.ChargeMaster;

public interface ChargeMasterService {

	List<AccountTypeCharges> getSelectedChargesAccountTypeWise(AccountTypeCharges accountTypeCharges);
	
	List<ChargeMaster> getChargeMasterList(ChargeMaster chargeMaster) throws Exception;
	
	List<ChargeMaster> getTransactionChargeList(ChargeMaster chargeMaster) throws Exception;
	
	List<ChargeMaster> getFuelChargeList(ChargeMaster chargeMaster) throws Exception;

	AccountTypeCharges addChargingConfigData(AccountTypeCharges accountTypeCharges);
	
	ChargeMaster addChargeType(ChargeMaster chargeMaster);
	
	Boolean validateChargeType(ChargeMaster chargeMaster) throws Exception;
}
