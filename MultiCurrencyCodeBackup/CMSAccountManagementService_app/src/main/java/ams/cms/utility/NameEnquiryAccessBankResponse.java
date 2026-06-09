package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NameEnquiryAccessBankResponse implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String resp_code;
	private String resp_desc;
	private String tranid;
	private String referencenumber;
	
	private NameEnquiryAccessBankObj obj;
	
	public String getResp_code() {
		return resp_code;
	}
	public void setResp_code(String resp_code) {
		this.resp_code = resp_code;
	}
	public String getResp_desc() {
		return resp_desc;
	}
	public void setResp_desc(String resp_desc) {
		this.resp_desc = resp_desc;
	}
	public String getTranid() {
		return tranid;
	}
	public void setTranid(String tranid) {
		this.tranid = tranid;
	}
	public String getReferencenumber() {
		return referencenumber;
	}
	public void setReferencenumber(String referencenumber) {
		this.referencenumber = referencenumber;
	}
	public NameEnquiryAccessBankObj getObj() {
		return obj;
	}
	public void setObj(NameEnquiryAccessBankObj obj) {
		this.obj = obj;
	}
	
	@Override
	public String toString() {
		return "NameEnquiryAccessBankResponse [resp_code=" + resp_code + ", resp_desc=" + resp_desc + ", tranid="
				+ tranid + ", referencenumber=" + referencenumber + ", obj=" + obj + "]";
	}
}
