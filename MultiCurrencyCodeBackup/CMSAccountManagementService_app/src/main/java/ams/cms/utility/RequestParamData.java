package ams.cms.utility;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class RequestParamData implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	ResponseParamData responseParamData;
	
	public ResponseParamData getResponseParamData() {
		return responseParamData;
	}
	public void setResponseParamData(ResponseParamData responseParamData) {
		this.responseParamData = responseParamData;
	}
}
