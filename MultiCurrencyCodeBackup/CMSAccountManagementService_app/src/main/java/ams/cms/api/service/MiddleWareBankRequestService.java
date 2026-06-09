package ams.cms.api.service;

import ams.cms.api.model.MiddleWareBankRequestModel;

public interface MiddleWareBankRequestService 
{
	MiddleWareBankRequestModel getMiddleWareBankRequestByParticipantId(MiddleWareBankRequestModel pullAccountModel);
	int updateMiddleWareAppInfo(MiddleWareBankRequestModel middleWareBankRequestModel);
}
