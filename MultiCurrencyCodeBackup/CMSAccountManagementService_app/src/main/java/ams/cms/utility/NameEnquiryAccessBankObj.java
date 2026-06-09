package ams.cms.utility;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NameEnquiryAccessBankObj implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String ResponseCode;
	private String ResponseMessage;
	
	private List<AccessBankDataObj> getcustomeracctsdetailsresp;

	public String getResponseCode() {
		return ResponseCode;
	}

	public void setResponseCode(String responseCode) {
		ResponseCode = responseCode;
	}

	public String getResponseMessage() {
		return ResponseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		ResponseMessage = responseMessage;
	}

	public List<AccessBankDataObj> getGetcustomeracctsdetailsresp() {
		return getcustomeracctsdetailsresp;
	}

	public void setGetcustomeracctsdetailsresp(List<AccessBankDataObj> getcustomeracctsdetailsresp) {
		this.getcustomeracctsdetailsresp = getcustomeracctsdetailsresp;
	}	
}
