package ams.cms.api.model;

public class AccountTranscationResponseComparator implements java.util.Comparator<AccountTranscationResponse> 
{
	@Override
	public int compare(AccountTranscationResponse a, AccountTranscationResponse b) 
	{
		String strTransactionId1 = a.getAccountTxnResponse().getTransactionSumary().getStrTransactionID();    	
    	String strTransactionId2 = b.getAccountTxnResponse().getTransactionSumary().getStrTransactionID();    	
    	int value = strTransactionId2.compareTo(strTransactionId1);     	
		return value;
	}
}
