package ams.cms.handler;

import java.util.List;

import ams.cms.api.model.AgencyBankingRequest;
import ams.cms.api.model.AgencyBankingResponse;
import ams.cms.util.ProcessResponse;

public interface AccountReverseHandler 
{	
	ProcessResponse processToReversedTxnFromTxn(ProcessResponse processResponse, List<AgencyBankingResponse> agencyBankingResponseList, AgencyBankingRequest agentTxnRequest);
	
	ProcessResponse processReversedAgentDepositTransaction(ProcessResponse processResponse, List<AgencyBankingResponse> agencyBankingResponseList, AgencyBankingRequest agentTxnRequest);
	
	ProcessResponse processReversedAgentWithdrawalTransaction(ProcessResponse processResponse, List<AgencyBankingResponse> agencyBankingResponseList, AgencyBankingRequest agentTxnRequest);
}
