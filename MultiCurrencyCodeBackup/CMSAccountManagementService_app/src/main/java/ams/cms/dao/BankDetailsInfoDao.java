package ams.cms.dao;

import java.util.List;

import ams.cms.api.model.BankListResponse;
import ams.cms.api.model.MiddleWareBankRequestModel;
import ams.cms.model.BankDetailsInfo;
import ams.cms.utility.BankMiddleWareData;
import ams.cms.utility.ExternalServerRequestResponseModel;
import ams.cms.utility.ExternalServerTokenModel;
import ams.cms.utility.MiddleWareTokenModel;
import ams.cms.utility.NameEnquiryRequestResponseModel;

public interface BankDetailsInfoDao extends GenericDao<BankDetailsInfo>
{
	int[] batchEntryOfBankDetailsInfoFromNIBSSS(List<BankDetailsInfo> baDetailsInfos);
	
	void truncateTable(String tableName);
	
	int addBankRelatedToken(ExternalServerTokenModel externalServerTokenModel);

	int updateankRelatedToken(ExternalServerTokenModel externalServerTokenModel);
	
	String getAccessToken(ExternalServerTokenModel externalServerTokenModel);
	
	List<BankListResponse> getNibssBankList();
	
	ExternalServerRequestResponseModel getNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel);
	
	int addNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel);
	
	int updaNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel);
	

	//middleWare APi for get Bank List
	
	String getMiddleWareToken(MiddleWareTokenModel middleWareTokenModel);

	int updateBankRelatedMiddleWareToken(MiddleWareTokenModel middleWareTokenModel);

	int addBankRelatedMiddleWareToken(MiddleWareTokenModel middleWareTokenModel);

	//int[] batchEntryOfBankDetailsInfoFromMiddleWare(List<BankDetailsInfo> banksDetails);
	int[] batchEntryOfBankDetailsInfoFromMiddleWare(List<BankMiddleWareData> banksDetails);

	MiddleWareBankRequestModel getRequestDataForBank(MiddleWareBankRequestModel middleWareBankRequestModel);

	int updateNameEnquiryRequestResponse(NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel);

	int addNameEnquiryRequestResponse(NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel);
	
	BankDetailsInfo getBankDetailsInfoFromBankCode(String bankcode);
}

