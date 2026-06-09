package ams.cms.constants;

import java.util.HashMap;
import java.util.Map;

public class TransactionType 
{
	public static final String LOAD_BAL;
	public static final Map<String, String> MODE = new HashMap<String, String>();
	
	static
	{
		System.out.println("Loading Static Data......");
		MODE.put("PUR", "DEBIT"); //Purchase
		MODE.put("WDL", "DEBIT"); //Withdrawal
		MODE.put("DPT", "CREDIT"); //Deposit
		MODE.put("RVL", "CREDIT"); //Reversal
		MODE.put("CR", "CREDIT"); //Deposit
		MODE.put("DR", "DEBIT"); //Withdrawal
		MODE.put("TIN", "CREDIT"); //Deposit(Transfer IN)
		MODE.put("TOT", "DEBIT"); //Withdrawal(Transfer OUT)
		
		LOAD_BAL = "Loading Balance";
		
		System.out.println(MODE);
	}
	
	public static class TransactionMode
	{
		public static final String DEBIT = "DEBIT";
		public static final String CREDIT = "CREDIT";
	}
}
