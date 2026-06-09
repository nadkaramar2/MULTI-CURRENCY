package ams.cms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class TxnHeader implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String mti;// ->by device
	private String InstrumentType;
	// BY SWITCH IN LOGIN RESPONSE. FOR REMAINING TXN IT WILL BE POPULATED BY
	// DEVICE
	private String token;
	private String channel;// WEB/MOBILE ->by device
	private String appVersion;// APPLICATION VERSION ->by device	
	// DEVICE OS
	private String operatingSystem;
	private String userAgent;
	private String ipAddress;
	private String fcmId;// ->by device
	private String connectivity;
	private String referralUrl;
	// MPOS/POS/PG/WALLET WILL BE POPULATED BY PROXY
	private String gatewayRequest;	
	@JsonProperty(access = Access.WRITE_ONLY)
	private long requestId;
	@JsonProperty(access = Access.WRITE_ONLY)
	private long responseId;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String txnFlowIdentifier;

	public String getMti() {
		return mti;
	}

	public void setMti(String mti) {
		this.mti = mti;
	}
	
	

	public String getInstrumentType() {
		return InstrumentType;
	}

	public void setInstrumentType(String instrumentType) {
		InstrumentType = instrumentType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(String appVersion) {
		this.appVersion = appVersion;
	}

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public long getResponseId() {
		return responseId;
	}

	public void setResponseId(long responseId) {
		this.responseId = responseId;
	}

	public String getTxnFlowIdentifier() {
		return txnFlowIdentifier;
	}

	public void setTxnFlowIdentifier(String txnFlowIdentifier) {
		this.txnFlowIdentifier = txnFlowIdentifier;
	}

	public String getGatewayRequest() {
		return gatewayRequest;
	}

	public void setGatewayRequest(String gatewayRequest) {
		this.gatewayRequest = gatewayRequest;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getFcmId() {
		return fcmId;
	}

	public void setFcmId(String fcmId) {
		this.fcmId = fcmId;
	}

	public String getOperatingSystem() {
		return operatingSystem;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getConnectivity() {
		return connectivity;
	}

	public void setConnectivity(String connectivity) {
		this.connectivity = connectivity;
	}

	public String getReferralUrl() {
		return referralUrl;
	}

	public void setReferralUrl(String referralUrl) {
		this.referralUrl = referralUrl;
	}

	
	
}
