package ams.cms.api.dao;

import ams.cms.api.model.MiddleWareBankRequestModel;

public interface MiddleWareBankRequestDao 
{
	MiddleWareBankRequestModel getMiddleWareBankRequestByParticipantId(MiddleWareBankRequestModel middleWareBankRequestModel);
	
	int updateMiddleWareAppInfo(MiddleWareBankRequestModel middleWareBankRequestModel);
}
