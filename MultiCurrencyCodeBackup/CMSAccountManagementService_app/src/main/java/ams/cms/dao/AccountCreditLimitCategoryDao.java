package ams.cms.dao;

import java.util.List;

import ams.cms.model.AccountCreditLimitCategory;

public interface AccountCreditLimitCategoryDao extends GenericDao<AccountCreditLimitCategory>
{
	AccountCreditLimitCategory getAccountCreditLimitCategoryObj(AccountCreditLimitCategory accountCreditLimitCategory);
	
	List<AccountCreditLimitCategory> getAccountCreditLimitCategoriesListByParticipantWise(String participantId);
	
	boolean isCreditTypeAlreadyExist(AccountCreditLimitCategory accountCreditLimitCategory);

	int updateAccountTypeCategoryInfo(AccountCreditLimitCategory accountCreditLimitCategory);
}
