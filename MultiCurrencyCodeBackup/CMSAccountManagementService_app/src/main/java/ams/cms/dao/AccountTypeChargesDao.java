package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountTypeCharges;

public interface AccountTypeChargesDao extends GenericDao<AccountTypeCharges>
{
	int[] batchEntryforAccountTypeChargesData(List<AccountTypeCharges> accountTypeCharges);

	List<AccountTypeCharges> getSelectedChargesAccountTypeWise(AccountTypeCharges accountTypeCharges);
}
