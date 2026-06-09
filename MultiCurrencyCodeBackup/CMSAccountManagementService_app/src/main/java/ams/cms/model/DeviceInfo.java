package ams.cms.model;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class DeviceInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String isTidRiskEnable;
	private String isMidRiskEnable;
	private String terminalType;
	private String isTokenCheckEnable;
	private String keyExchangeType;
	private String defaultTmk;
	private String primaryTmk;
	private String bdk;
	private String ipek;
	private String ipek1; //for CryptoMaster IPEK
	private String isBharatQrEnable;
	private String isUpiEnable;
	private String isMicroAtmEnable;
	private String additionalInfo;

	public String getIsTidRiskEnable() {
		return isTidRiskEnable;
	}

	public void setIsTidRiskEnable(String isTidRiskEnable) {
		this.isTidRiskEnable = isTidRiskEnable;
	}

	public String getIsMidRiskEnable() {
		return isMidRiskEnable;
	}

	public void setIsMidRiskEnable(String isMidRiskEnable) {
		this.isMidRiskEnable = isMidRiskEnable;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getIsTokenCheckEnable() {
		return isTokenCheckEnable;
	}

	public void setIsTokenCheckEnable(String isTokenCheckEnable) {
		this.isTokenCheckEnable = isTokenCheckEnable;
	}

	public String getKeyExchangeType() {
		return keyExchangeType;
	}

	public void setKeyExchangeType(String keyExchangeType) {
		this.keyExchangeType = keyExchangeType;
	}

	public String getDefaultTmk() {
		return defaultTmk;
	}

	public void setDefaultTmk(String defaultTmk) {
		this.defaultTmk = defaultTmk;
	}

	public String getPrimaryTmk() {
		return primaryTmk;
	}

	public void setPrimaryTmk(String primaryTmk) {
		this.primaryTmk = primaryTmk;
	}

	public String getBdk() {
		return bdk;
	}

	public void setBdk(String bdk) {
		this.bdk = bdk;
	}

	public String getIpek() {
		return ipek;
	}

	public String getIpek1() {
		return ipek1;
	}

	public void setIpek1(String ipek1) {
		this.ipek1 = ipek1;
	}

	public void setIpek(String ipek) {
		this.ipek = ipek;
	}

	public String getIsBharatQrEnable() {
		return isBharatQrEnable;
	}

	public void setIsBharatQrEnable(String isBharatQrEnable) {
		this.isBharatQrEnable = isBharatQrEnable;
	}

	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}

	public String getIsUpiEnable() {
		return isUpiEnable;
	}

	public void setIsUpiEnable(String isUpiEnable) {
		this.isUpiEnable = isUpiEnable;
	}

	public String getIsMicroAtmEnable() {
		return isMicroAtmEnable;
	}

	public void setIsMicroAtmEnable(String isMicroAtmEnable) {
		this.isMicroAtmEnable = isMicroAtmEnable;
	}

}
