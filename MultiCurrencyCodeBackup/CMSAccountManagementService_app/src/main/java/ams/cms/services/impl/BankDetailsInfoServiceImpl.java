package ams.cms.services.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ams.cms.api.model.BankListResponse;
import ams.cms.api.model.MiddleWareBankRequestModel;
import ams.cms.dao.BankDetailsInfoDao;
import ams.cms.model.BankDetailsInfo;
import ams.cms.services.BankDetailsInfoService;
import ams.cms.utility.BankMiddleWareData;
import ams.cms.utility.ExternalServerRequestResponseModel;
import ams.cms.utility.ExternalServerTokenModel;
import ams.cms.utility.MiddleWareTokenModel;
import ams.cms.utility.NameEnquiryRequestResponseModel;

@Transactional
@Service
public class BankDetailsInfoServiceImpl implements BankDetailsInfoService
{
	@Autowired
	private BankDetailsInfoDao bankDetailsInfoDao;	

	@Override
	public int[] batchEntryOfBankDetailsInfoFromNIBSSS(List<BankDetailsInfo> baDetailsInfos) 
	{
		return bankDetailsInfoDao.batchEntryOfBankDetailsInfoFromNIBSSS(baDetailsInfos);
	}

	@Override
	public void truncateTable(String tableName) {
		bankDetailsInfoDao.truncateTable(tableName);		
	}

	@Override
	public int addBankRelatedToken(ExternalServerTokenModel externalServerTokenModel) {
		return bankDetailsInfoDao.addBankRelatedToken(externalServerTokenModel);
	}

	@Override
	public int updateankRelatedToken(ExternalServerTokenModel externalServerTokenModel) {
		return bankDetailsInfoDao.updateankRelatedToken(externalServerTokenModel);
	}

	@Override
	public String getAccessToken(ExternalServerTokenModel externalServerTokenModel) {
		return bankDetailsInfoDao.getAccessToken(externalServerTokenModel);
	}

	@Override
	public List<BankListResponse> getNibssBankList() {
		return bankDetailsInfoDao.getNibssBankList();
	}

	@Override
	public ExternalServerRequestResponseModel getNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel) 
	{
		return bankDetailsInfoDao.getNameEnquiryReqRes(externalServerRequestResponseModel);
	}

	@Override
	public int addNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel) 
	{
		return bankDetailsInfoDao.addNameEnquiryReqRes(externalServerRequestResponseModel);
	}

	@Override
	public int updaNameEnquiryReqRes(ExternalServerRequestResponseModel externalServerRequestResponseModel)
	{
		return bankDetailsInfoDao.updaNameEnquiryReqRes(externalServerRequestResponseModel);
	}

	
	////middleWare APi for get Bank List
	@Override
	public int updateBankRelatedMiddleWareToken(MiddleWareTokenModel middleWareTokenModel) {
		return bankDetailsInfoDao.updateBankRelatedMiddleWareToken(middleWareTokenModel);
	}

	@Override
	public String getMiddleWareToken(MiddleWareTokenModel middleWareTokenModel) {
		return bankDetailsInfoDao.getMiddleWareToken(middleWareTokenModel);
	}

	@Override
	public int addBankRelatedMiddleWareToken(MiddleWareTokenModel middleWareTokenModel) 
	{
		return bankDetailsInfoDao.addBankRelatedMiddleWareToken(middleWareTokenModel);
	}

	@Override
	public int[] batchEntryOfBankDetailsInfoFromMiddleWare(List<BankMiddleWareData> banksDetails) 
	{
		return bankDetailsInfoDao.batchEntryOfBankDetailsInfoFromMiddleWare(banksDetails);
	}

	@Override
	public MiddleWareBankRequestModel getRequestDataForBank(MiddleWareBankRequestModel middleWareBankRequestModel) {
		return bankDetailsInfoDao.getRequestDataForBank(middleWareBankRequestModel);
	}
	
	@Override
	public int addNameEnquiryRequestResponse(NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel) {
		
		return bankDetailsInfoDao.addNameEnquiryRequestResponse(nameEnquiryRequestResponseModel);
	}
	
	@Override
	public int updateNameEnquiryRequestResponse(NameEnquiryRequestResponseModel nameEnquiryRequestResponseModel) 
	{
		return bankDetailsInfoDao.updateNameEnquiryRequestResponse(nameEnquiryRequestResponseModel);
	}

	@Override
	public int[] saveAllBulkEntriesOfBank(List<BankDetailsInfo> collect) {
		bankDetailsInfoDao.saveAll(collect);
		return null;
	}

	@Override
	public BankDetailsInfo getBankDetailsInfoFromBankCode(String bankcode) 
	{
		return bankDetailsInfoDao.getBankDetailsInfoFromBankCode(bankcode);
	}

	
}
