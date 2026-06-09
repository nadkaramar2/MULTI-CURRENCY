package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountTransactionReportDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountTranMaster;
import ams.cms.model.TransactionTypeModel;

@Repository
public class AccountTransactionReportDaoImpl extends AbstractGenericDao<AccountTranMaster> implements AccountTransactionReportDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	public List<AccountTranMaster> getTxnAccountlist(AccountTranMaster accountTranMaster) 
	{
		try {
			StringBuilder statementQuery = new StringBuilder("SELECT DATE(atm.switch_txn_date) AS txn_date,TIME(atm.switch_txn_date) AS txn_Time,atm.txn_id AS strTxn_id,atm.tran_type AS strTran_type, ");  
			statementQuery.append("atm.transaction_amount AS strTransaction_amount,atm.response_code AS strResponseCode, ");
			statementQuery.append("atm.account_no AS strAccountNumber ,atm.from_account_number AS strFrom_account_number, ");
			statementQuery.append("atm.to_account_number AS strTo_account_number from account_tran_master AS atm WHERE ");
			statementQuery.append("atm.switch_txn_date BETWEEN '"+accountTranMaster.getFromDate()+"' AND '"+accountTranMaster.getToDate()+"' ");
			
			if (accountTranMaster.getStrTxnTypeKeyWord()!=null && accountTranMaster.getStrTxnTypeKeyWord().trim().length() > 0)
			{
				statementQuery.append("AND atm.tran_type='"+accountTranMaster.getStrTxnTypeKeyWord()+"' ");
				}
			
			List<AccountTranMaster> accountTxnlist = jdbcTemplate.query(statementQuery.toString(),
					new BeanPropertyRowMapper<AccountTranMaster>(AccountTranMaster.class), new Object[] {});
			return accountTxnlist;
		} catch (Exception e) {
			e.printStackTrace();
		}
			return null;
	}

	@Override
	public List<TransactionTypeModel> getTxnAccountTypelist(TransactionTypeModel transactionTypeModel) 
	{
		try {
			String txnReportQuery = "SELECT txn_type_keyword AS strTxnTypeKeyWord FROM transaction_type_master ";
			
			List<TransactionTypeModel> getTxnType = jdbcTemplate.query(txnReportQuery,
					new BeanPropertyRowMapper<TransactionTypeModel>(TransactionTypeModel.class),
					new Object[] 
					{ });
				return getTxnType;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
