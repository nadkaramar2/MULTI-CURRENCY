package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.LoadMoneyTxnDetailsDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.LoadMoneyTxnDetails;

@Repository
public class LoadMoneyTxnDetailsDaoImpl  extends AbstractGenericDao<LoadMoneyTxnDetails> implements LoadMoneyTxnDetailsDao{

	
	@Autowired
	private	JdbcTemplate jdbcTemplate;
	
	@Override
	public LoadMoneyTxnDetails getPreviousLoadMoneyData(LoadMoneyTxnDetails loadMoneyTxnDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LoadMoneyTxnDetails getLoadDetailTxnByAccountNumber(LoadMoneyTxnDetails loadMoneyTxnDetails) {
		try 
		{
			StringBuilder queryBuilder = new StringBuilder("SELECT lmtd.lrs_limit AS lrsLimit , lmtd.account_type AS accountType , lmtd.account_number AS accountNumber , lmtd.fy_balance AS fyBalance , ");
			queryBuilder.append(" lmtd.load_amt AS loadAmt , lmtd.balanced_lrs AS balancedLRS , lmtd.total_lrs_consumed AS totalLrsConsumed , lmtd.total_excess_loading AS totalExcessLoading ,");
			queryBuilder.append(" lmtd.total_loaded AS totalLoaded , lmtd.tcs_on AS tcsOn , lmtd.total_tcs_on AS totalTcsOn");
			queryBuilder.append(" FROM load_money_txn_details lmtd WHERE lmtd.account_type= '"+loadMoneyTxnDetails.getAccountType()+"' AND lmtd.account_number= '"+loadMoneyTxnDetails.getAccountNumber()+"'");
			//ORDER BY accstmt.transaction_date DESC
			queryBuilder.append(" ORDER BY lmtd.loaded_date DESC ");
			
			List<LoadMoneyTxnDetails> loadMoneyTxnDetailsObj  = jdbcTemplate.query(queryBuilder.toString(),
			new BeanPropertyRowMapper<LoadMoneyTxnDetails>(LoadMoneyTxnDetails.class),
			new Object[]  {});
			
			if (loadMoneyTxnDetailsObj!=null && loadMoneyTxnDetailsObj.size() > 0) 
			{
				return loadMoneyTxnDetailsObj.get(0);
			}	
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountCreditCardTxnWise::"+e);
			e.printStackTrace();
		}
		return null;
}

}
