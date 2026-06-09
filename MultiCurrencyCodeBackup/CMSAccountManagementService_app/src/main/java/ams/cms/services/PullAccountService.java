package ams.cms.services;

import ams.cms.model.PullAccountModel;

public interface PullAccountService 
{
	//PullAccountModel saveSendAccountMoneyList(PullAccountModel pullAccountModel) throws Exception;

	int updateSendAccountMoneyList(PullAccountModel pullAccountModel);
	
	//FundTransferExternalServerReqRespModel getSendMoneyConfiguration(FundTransferExternalServerReqRespModel fundTransferExternalServerReqRespModel);
    
	PullAccountModel getPullAccountModelByParticipantId(PullAccountModel pullAccountModel);

	PullAccountModel savePullAccountMaster(PullAccountModel pullAccountModel);
	
	PullAccountModel getPullAccountModelByBankCode(PullAccountModel pullAccountModel);
}
