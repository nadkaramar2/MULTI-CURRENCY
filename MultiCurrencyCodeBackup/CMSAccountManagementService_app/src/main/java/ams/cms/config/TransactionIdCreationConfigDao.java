package ams.cms.config;

public interface TransactionIdCreationConfigDao 
{
	String getTransactionId() throws Exception;
	
	String getPreTransactionId() throws Exception;
}
