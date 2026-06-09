package ams.cms.api.service;

import java.util.List;

import ams.cms.api.model.AccountCreditLimit;

public interface AccountCreditLimtService {

	AccountCreditLimit saveAccountCreditLimitCategory(AccountCreditLimit accountCreditLimitCategory) throws Exception;
	
	AccountCreditLimit getAccountCreditLimitCategoryObj(AccountCreditLimit accountCreditLimitCategory) throws Exception;
	
	List<AccountCreditLimit> getAccountCreditLimitCategoriesListByParticipantWise(String participantId) throws Exception;
	
}
