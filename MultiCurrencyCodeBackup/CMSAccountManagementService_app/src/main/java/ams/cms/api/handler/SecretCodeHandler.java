package ams.cms.api.handler;

import ams.cms.config.TransactionConfig;

public interface SecretCodeHandler 
{
	TransactionConfig validateSecretCode(TransactionConfig transactionConfig);
}
