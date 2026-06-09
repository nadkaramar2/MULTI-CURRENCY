package ams.cms.services;

import java.util.List;

import ams.cms.model.AccountTypeCharges;

public interface AccountTypeChargesService {

	List<AccountTypeCharges> getSelectedChargesAccountTypeWise(AccountTypeCharges accountTypeCharges);

	AccountTypeCharges addChargingConfigData(AccountTypeCharges accountTypeCharges);

}
