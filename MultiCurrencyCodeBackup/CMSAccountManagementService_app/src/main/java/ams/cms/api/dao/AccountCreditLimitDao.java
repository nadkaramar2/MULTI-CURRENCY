package ams.cms.api.dao;

import java.util.List;

import ams.cms.api.model.AccountCreditLimit;
import ams.cms.dao.GenericDao;

public interface AccountCreditLimitDao extends GenericDao<AccountCreditLimit>{

	AccountCreditLimit getAccountCreditLimitCategoryObj(AccountCreditLimit accountCreditLimit);
	
	List<AccountCreditLimit> getAccountCreditLimitCategoriesListByParticipantWise(String participantId);
	
}
