package ams.cms.dao;

import ams.cms.model.AccountTranMaster;
import ams.cms.model.PullAccountModel;

public interface PullAccountDao extends GenericDao<PullAccountModel> 
{
	int updateSendAccountMoneyList(AccountTranMaster accountTranMaster);

	PullAccountModel getPullAccountModelByParticipantId(PullAccountModel pullAccountModel);

	PullAccountModel getPullAccountModelByBankCode(PullAccountModel pullAccountModel);
}
