package ams.cms.services;

import java.util.List;

import ams.cms.model.AccountCreditLimitCategory;

public interface AccountCreditLimitCategoryService 
{
	AccountCreditLimitCategory saveAccountCreditLimitCategory(AccountCreditLimitCategory accountCreditLimitCategory) throws Exception;
	
	AccountCreditLimitCategory getAccountCreditLimitCategoryObj(AccountCreditLimitCategory accountCreditLimitCategory) throws Exception;
	
	List<AccountCreditLimitCategory> getAccountCreditLimitCategoriesListByParticipantWise(String participantId) throws Exception;

	boolean isCreditTypeAlreadyExist(AccountCreditLimitCategory accountCreditLimitCategory) throws Exception;

	int updateAccountTypeCategoryInfo(AccountCreditLimitCategory accountCreditLimitCategory);

}
