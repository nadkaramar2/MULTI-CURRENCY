package ams.cms.api.handler;

import ams.cms.api.model.ExternalTxnRequest;
import ams.cms.util.ProcessResponse;

public interface ExternalTxnRequestValidator 
{
	ProcessResponse validateMontraTxnRequest(ProcessResponse processResponse, ExternalTxnRequest externalTxnRequest);
}
