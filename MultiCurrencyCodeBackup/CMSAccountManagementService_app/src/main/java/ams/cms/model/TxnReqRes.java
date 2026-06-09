package ams.cms.model;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import ams.cms.util.ProcessResponse;


@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TxnReqRes implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TxnHeader header;
	private UserInfo userInfo;
	private DeviceInfo deviceInfo;
	private TxnData txnData;
	private ProcessResponse response;
	private FundTransferIn fundTransferIn;
	private List<TxnDetail> txnDetail = new ArrayList<TxnDetail>();
	private BeneficiaryTxnMaster beneficiaryTxnMaster;
	
	
	public BeneficiaryTxnMaster getBeneficiaryTxnMaster() {
		return beneficiaryTxnMaster;
	}

	public void setBeneficiaryTxnMaster(BeneficiaryTxnMaster beneficiaryTxnMaster) {
		this.beneficiaryTxnMaster = beneficiaryTxnMaster;
	}

	public FundTransferIn getFundTransferIn() {
		return fundTransferIn;
	}

	public void setFundTransferIn(FundTransferIn fundTransferIn) {
		this.fundTransferIn = fundTransferIn;
	}

	public TxnHeader getHeader() {
		return header;
	}

	public void setHeader(TxnHeader header) {
		this.header = header;
	}

	public DeviceInfo getDeviceInfo() {
		return deviceInfo;
	}

	public void setDeviceInfo(DeviceInfo deviceInfo) {
		this.deviceInfo = deviceInfo;
	}

	public ProcessResponse getResponse() {
		return response;
	}

	public void setResponse(ProcessResponse response) {
		this.response = response;
	}

	public TxnData getTxnData() {
		return txnData;
	}

	public void setTxnData(TxnData txnData) {
		this.txnData = txnData;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	public List<TxnDetail> getTxnDetail() {
		return txnDetail;
	}

	public void setTxnDetail(List<TxnDetail> txnDetail) {
		this.txnDetail = txnDetail;
	}

}
