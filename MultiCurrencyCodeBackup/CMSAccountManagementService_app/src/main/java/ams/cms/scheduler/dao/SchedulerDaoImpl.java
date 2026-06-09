package ams.cms.scheduler.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.impl.PullAccountDaoImpl;
import ams.cms.logger.AMSLogger;
import ams.cms.model.AccountCreation;
import ams.cms.scheduler.model.AccountCreditCardStatement;
import ams.cms.scheduler.model.AccountWiseInterestMaster;
import ams.cms.scheduler.model.RevolvingCreditCardInterest;
import ams.cms.scheduler.schedule.SchedularModel;
import ams.cms.utility.Utils;
import ams.cms.model.RevolvingCreditCardTxnMaster;

@Repository
public class SchedulerDaoImpl implements SchedulerDao
{
	private AMSLogger amsLogger = AMSLogger.getInstance(SchedulerDaoImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<AccountWiseInterestMaster> getCreditAccountWiseInterestList(AccountWiseInterestMaster accountwiseInterestMaster)
	{
		try
		{
			String sqlQuery = "SELECT cct.participant_id AS strParticipantId,cct.account_type AS strAccountType, "
					+ "cct.account_number AS strAccountNumber, cct.mcc_code AS strMcc, "
					+ "cct.amount_paid AS strAmountPaid, cct.amount_paid_date AS strAmountPaidDate," 
					+ "cct.transaction_amount AS strTransactionAmount, cct.date_of_interest AS strDateOfInterest, "
					+ "cct.txn_date AS strTxnDate, cct.txn_time AS strTxnTime,"
					+ "mwi.interest_rate AS strInterestRate, mwi.grace_period AS strGracePeriod, "
					+ "cct.is_paid AS strIsPaid, mwi.payment_received_within AS strPaymentReceivedWithin, "
					+ "mwi.amount_due_percentage AS strAmountDuePercentage,"
					+ "ROUND(cct.transaction_amount * mwi.interest_rate/100/365, 2) AS strCalculatInterest,"
					+ "ROUND(cct.transaction_amount*18/100,2) AS  strCalculatGst "
					+ "FROM credit_card_transaction AS  cct inner JOIN mcc_wise_interest AS mwi ON cct.id=mwi.id "
					+ "WHERE cct.date_of_interest <= CAST(NOW() AS DATE) and cct.is_paid = ?";
					
			
			List<AccountWiseInterestMaster> accountwiseInterestMasters  = this.jdbcTemplate.query(
			sqlQuery, new BeanPropertyRowMapper<AccountWiseInterestMaster>(AccountWiseInterestMaster.class),
			new Object[] {accountwiseInterestMaster.getStrIsPaid()});
			return accountwiseInterestMasters;
		}
		catch (Exception e) 
		{
				e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int[] batchProcessForInsertAcoountWiseInterest(List<AccountWiseInterestMaster> accountwiseInterestMasters) 
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO interest_account_wise "
					+ "(participant_id, account_type, account_number,"
					+ "mcc_code, transaction_id, transaction_amount, "
					+ "transaction_date, "
					+ "transaction_time, interest_paid_amount,"
					+ "interest_paid_date, interest_calculated_date, "
					+ "is_paid, calculated_interest,calculated_GST "
					+ ")"
					+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new BatchPreparedStatementSetter() 
					{
						@Override
						public void setValues(PreparedStatement psmt, int i) throws SQLException 
						{
							psmt.setString(1, accountwiseInterestMasters.get(i).getStrParticipantId());
							psmt.setString(2, accountwiseInterestMasters.get(i).getStrAccountType());
							psmt.setString(3, accountwiseInterestMasters.get(i).getStrAccountNumber());
							psmt.setString(4, accountwiseInterestMasters.get(i).getStrMcc());
							psmt.setString(5, accountwiseInterestMasters.get(i).getStrTransactionId());
							psmt.setString(6, accountwiseInterestMasters.get(i).getStrTransactionAmount());
							psmt.setString(7, accountwiseInterestMasters.get(i).getStrTxnDate());
							psmt.setString(8, accountwiseInterestMasters.get(i).getStrTxnTime());
							psmt.setString(9, accountwiseInterestMasters.get(i).getStrInterestPaidAmount());
							psmt.setString(10, accountwiseInterestMasters.get(i).getStrInterestPaidDate());
							psmt.setString(11, accountwiseInterestMasters.get(i).getStrInterestCalculateDate());
							psmt.setString(12, accountwiseInterestMasters.get(i).getStrIsPaid());
							psmt.setString(13, accountwiseInterestMasters.get(i).getStrCalculatInterest());
							psmt.setString(14, accountwiseInterestMasters.get(i).getStrCalculatGst());
						}
						
						@Override
						public int getBatchSize() 
						{
							return accountwiseInterestMasters.size();
						}
					});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return batchResponse;
	}

	@Override
	public List<AccountCreditCardStatement> getAccountCreditStatementlist(AccountCreditCardStatement accountCreditCardStatement)
	{
		try
		{
			String sqlQuery =" SELECT CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, "
				   +"am.account_number AS strAccountNumber, "
				   +"am.account_type AS strAccountType, "
				   +"ast.txn_type AS strTransactionType, "
				   +"ast.txn_mode AS strTransactionMode, "
				   +"rccm.mad_rate AS strMadRate, " 
				   +"calm.card_type AS strCardType, "
				   +"calm.card_number AS strCardNumber, "
				   +"am.available_credit_limit AS strAvailableCreditLimit, "
				   +"ast.closing_balance AS strclosingBalance, "
				   +"am.email AS strEmailID, "
				   +"ROUND(am.total_outstanding_balance* (rccm.mad_rate/100)) AS strMinimumAmountDue, "
				   +"am.total_outstanding_balance AS strTotalOutstandingBal "
				   +"FROM account_master AS am "
				   +"INNER JOIN account_statement AS ast "
				   +"ON am.account_number = ast.account_number "
				   +"INNER JOIN card_account_linkage_master AS calm "
				   +"ON calm.account_number=am.account_number "
				   +"INNER JOIN revolving_credit_card_master rccm "
				   +"ON rccm.account_type=am.account_type "
				   +"INNER JOIN account_type_master atm "  
				   +"ON atm.account_type=am.account_type "
				   +"WHERE atm.is_credit_type = ? "  
				   +"AND atm.is_revolving_credit = ? "
				   +"AND ast.transaction_date BETWEEN DATE_ADD(CAST(CURRENT_TIMESTAMP as DATE), INTERVAL - 1 MONTH) AND CAST(CURRENT_TIMESTAMP as DATE)";

			List<AccountCreditCardStatement> accountCreditCardStatements = this.jdbcTemplate.query(sqlQuery,
			new BeanPropertyRowMapper<AccountCreditCardStatement>(AccountCreditCardStatement.class),
			new Object[] 
			{
					accountCreditCardStatement.getStrIsCreditType(),
					accountCreditCardStatement.getStrIsRevolvingCredit() 
			});
			return accountCreditCardStatements;
		}
		catch (Exception e) 
		{
				e.printStackTrace();
		}
		return null;
	}

	@Override
	public int[] batchProcessForInsertAccountCreditCardStatement(List<AccountCreditCardStatement> accountCreditCardStatement) 
	{
		int[] batchResponse = null;
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO revolving_credit_interest_txn "
			+ "(participant_id, account_type, account_number,"
			+ "mcc_code, txn_id, txn_amount, "
			+ "txn_date, "
			+ "txn_time, calculated_interest_amount,"
			+ "calculated_gst_amount "
			+ ")"
			+ " values(?,?,?,?,?,?,?,?,?,?,?)",
			new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					psmt.setString(1, accountCreditCardStatement.get(i).getStrAccountNumber());
					
				}
				
				@Override
				public int getBatchSize() 
				{
					return accountCreditCardStatement.size();
				}
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return batchResponse;
	}

	@Override
	public List<AccountCreation> getUserListForPaymentDueNotify() 
	{
		try 
		{
			StringBuilder sqlQuerySb = new StringBuilder("SELECT acm.account_type AS strAccountType, acm.account_number AS strAccountNumber, ");
			sqlQuerySb.append("acm.email AS strEmailID,");
			sqlQuerySb.append("acm.payement_due_date AS payementDueDate, acm.notify_by_payment_date AS notifyByPaymentDate ");
			sqlQuerySb.append("FROM account_master acm INNER JOIN account_type_master atm ON acm.account_type = atm.account_type ");
			sqlQuerySb.append("WHERE atm.is_credit_type = 'Y' AND atm.is_revolving_credit = 'Y' AND acm.payement_due_date < CAST(CURRENT_TIMESTAMP as DATE)");
			
			List<AccountCreation> notifyUserList = jdbcTemplate.query(sqlQuerySb.toString(),
					new BeanPropertyRowMapper<AccountCreation>(AccountCreation.class),
					new Object[] {}
			);

			return notifyUserList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AccountCreditCardStatement> getListOfCustomerAccountForBilling() 
	{
		try 
		{
			StringBuilder sqlQuery = new StringBuilder("SELECT am.participant_id AS strParticipantId, CONCAT_WS(' ', am.first_name, am.middle_name, am.last_name) AS strAccountHolderName, ");
			sqlQuery.append( "am.account_number AS strAccountNumber, am.account_type AS strAccountType, ");
			sqlQuery.append( "am.email AS strEmailID, calm.card_type AS strCardType, calm.card_number AS strCardNumber, ");
			sqlQuery.append( "am.total_outstanding_balance AS strTotalOutstandingBal, am.available_credit_limit AS strAvailableCreditLimit, ");
			sqlQuery.append( "ROUND(am.total_outstanding_balance* (rccm.mad_rate/100)) AS strMinimumAmountDue, ");
			sqlQuery.append( "am.last_total_amount_clear_date as lastTotalAmountClearDate, am.payement_due_date as strPayementDueDate, DATE_ADD(CAST(CURRENT_TIMESTAMP AS DATE),INTERVAL + rccm.grace_period_in_days DAY) AS calculatedtDueDate");
			sqlQuery.append( "atm.last_billing_cycle_date AS strLastBillingCycleDate, CAST(CURRENT_TIMESTAMP as DATE) AS strCurrentBillingCycleDate ");
			sqlQuery.append( "FROM account_master AS am ");
			sqlQuery.append("INNER JOIN account_type_master atm ");
			sqlQuery.append("ON atm.account_type = am.account_type ");
			sqlQuery.append( "INNER JOIN revolving_credit_card_master rccm ");
			sqlQuery.append( "ON rccm.account_type = am.account_type ");
			sqlQuery.append( "INNER JOIN card_account_linkage_master AS calm ");
			sqlQuery.append( "ON calm.account_number = am.account_number ");
			sqlQuery.append( "WHERE atm.is_credit_type = 'Y' AND atm.is_revolving_credit = 'Y' AND am.total_outstanding_balance > 0 ");
			sqlQuery.append( "AND DATE_ADD(CAST(atm.last_billing_cycle_date as DATE), INTERVAL + 1 MONTH) = CAST(CURRENT_TIMESTAMP as DATE)");
			
			List<AccountCreditCardStatement> accountCreditCardStatements = this.jdbcTemplate.query(sqlQuery.toString(),
					new BeanPropertyRowMapper<AccountCreditCardStatement>(AccountCreditCardStatement.class),
					new Object[] {});
			return accountCreditCardStatements;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AccountCreditCardStatement> getStatementListOfCustomer(AccountCreditCardStatement accountCreditCardStatement) 
	{
		try 
		{
			StringBuilder sqlQuery = new StringBuilder("SELECT ast.transaction_date AS strTransactionDate, ast.transaction_id AS strTransactionId, ");
			sqlQuery.append("ast.naration AS strTransactionDetails, \"-\" AS strRewardsPoints, ");
			sqlQuery.append("\"-\" AS strInternationalAmount, ast.transaction_amount AS strTransactionAmount ");
			sqlQuery.append("FROM account_master AS am ");
			sqlQuery.append("INNER JOIN account_statement AS ast ");
			sqlQuery.append("ON am.account_number = ast.account_number ");
			sqlQuery.append("WHERE am.account_number = '"+accountCreditCardStatement.getStrAccountNumber()+"' AND ");
			sqlQuery.append("ast.transaction_date BETWEEN DATE_ADD(CAST(CURRENT_TIMESTAMP as DATE), INTERVAL - 1 MONTH) AND CAST(CURRENT_TIMESTAMP as DATE)");
			
			List<AccountCreditCardStatement> accountCreditCardStatements = this.jdbcTemplate.query(sqlQuery.toString(),
					new BeanPropertyRowMapper<AccountCreditCardStatement>(AccountCreditCardStatement.class),
					new Object[] {});
			return accountCreditCardStatements;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateLastBillingCycleDateOFAccountType(AccountCreditCardStatement accountCreditCardStatement)
	{
		int count = 0;
		try
		{
			StringBuilder sqlquerySb = new StringBuilder("UPDATE account_type_master atm ");
			sqlquerySb.append( "SET atm.last_billing_cycle_date = CAST(CURRENT_TIMESTAMP as DATE) ");
			sqlquerySb.append("WHERE atm.account_type = ? AND atm.is_credit_type = 'Y' ");
			sqlquerySb.append("AND atm.is_revolving_credit = 'Y'");
			
			List<Object> objectList = new ArrayList<Object>();
			
			objectList.add(accountCreditCardStatement.getStrAccountType());		
			
			Object[] object = objectList.toArray();
			
			count = this.jdbcTemplate.update(sqlquerySb.toString(), object);
			
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return count;
	}

	@Override
	public List<RevolvingCreditCardInterest> getRevolvingCreditInterestTxn(RevolvingCreditCardInterest revolvingCreditCardInterest) 
	{
		try {
			StringBuilder sqlQuery = new StringBuilder("SELECT am.participant_id AS strParticipantId, ");
			sqlQuery.append("am.account_type AS strAccountType, am.account_number AS strAccountNumber,");
			sqlQuery.append("am.total_outstanding_balance AS strTotalOutstandingBal,am.total_outstanding_interest AS totalOutstandingInterest,am.payement_due_date as paymentDueDate, ");
			sqlQuery.append("am.grace_period_start_date AS gracePeriodStartDate, ");
			sqlQuery.append("ROUND(am.total_outstanding_balance * rccm.interest_rate/100/365, 2) AS strCalculatedInterest,");
			sqlQuery.append("ROUND(ROUND(am.total_outstanding_balance * rccm.interest_rate/100/365, 2)*18/100,2) AS  strCalcualteGstAmount,");
			sqlQuery.append("ROUND((am.total_outstanding_interest + ROUND(am.total_outstanding_balance * rccm.interest_rate/100/365, 2)),2) AS strTotalOutstandingInterest,");
			sqlQuery.append("ROUND((am.total_calculated_gst_amount + ROUND(ROUND(am.total_outstanding_balance * rccm.interest_rate/100/365, 2)*18/100,2)),2) AS strTotalCalculatedGST ");
			sqlQuery.append("FROM account_master AS am INNER JOIN revolving_credit_card_master AS rccm ON am.account_type = rccm.account_type ");
			sqlQuery.append("INNER JOIN account_type_master AS atym ON atym.account_type = rccm.account_type ");
			sqlQuery.append("WHERE atym.is_credit_type = 'Y' AND atym.is_revolving_credit = 'Y'  ");
			sqlQuery.append("AND am.total_outstanding_balance > 0");
			//sqlQuery.append("AND (DAY(CAST(CURRENT_TIMESTAMP AS DATE)) + rccm.grace_period_in_days) AS paymentDueDate < CAST(NOW() AS DATE);");
					
			List<RevolvingCreditCardInterest> revolvingCreditInterest = this.jdbcTemplate.query(sqlQuery.toString(),
					new BeanPropertyRowMapper<RevolvingCreditCardInterest>(RevolvingCreditCardInterest.class),
					new Object[] {
					});
			return revolvingCreditInterest;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int[] batchProcessForInsertRevolvingCreditCardInterest(List<RevolvingCreditCardInterest> revolvingCreditCardInterest)
	{
		int[] batchResponse = {};
		try
		{
			return this.jdbcTemplate.batchUpdate("INSERT INTO revolving_credit_interest_txn "
			+ "(participant_id, account_type, account_number,"
			+ "txn_id, txn_amount,"
			+ "txn_date, "
			+ "txn_time,calculated_interest_amount,"
			+ "calculated_gst_amount "
			+ ")"
			+ " values(?,?,?,?,?,?,?,?,?)",
			new BatchPreparedStatementSetter() 
			{
				@Override
				public void setValues(PreparedStatement psmt, int i) throws SQLException 
				{
					
					psmt.setString(1, revolvingCreditCardInterest.get(i).getStrParticipantId());
					psmt.setString(2, revolvingCreditCardInterest.get(i).getStrAccountType());
					psmt.setString(3, revolvingCreditCardInterest.get(i).getStrAccountNumber());
					psmt.setString(4, "12221");
					psmt.setString(5, String.valueOf(revolvingCreditCardInterest.get(i).getStrTotalOutstandingBal()));
					psmt.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
					psmt.setTime(7, new java.sql.Time(System.currentTimeMillis()));
					psmt.setDouble(8, revolvingCreditCardInterest.get(i).getStrCalculatedInterest());
					psmt.setDouble(9,revolvingCreditCardInterest.get(i).getStrCalcualteGstAmount());
					
					updateSomeAccountMasterRecord(revolvingCreditCardInterest.get(i));
					
					insertRevolvingInterestInAccountTxnMaster(revolvingCreditCardInterest.get(i));
				}
				
				@Override
				public int getBatchSize() 
				{
					return revolvingCreditCardInterest.size();
				}
			});
	
		} 
		catch (Exception e) {
			System.out.println("[batchProcessForInsertRevolvingCreditCardInterest] Exception Occured= "+e.getMessage());
			e.printStackTrace();
		}
		return batchResponse;
	}
	
	private void updateSomeAccountMasterRecord(RevolvingCreditCardInterest revolvingCreditCardInterest)
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "total_outstanding_interest = ?,"
					+ "total_calculated_gst_amount = ? "										
					+ "WHERE "
					+ "account_type = ? and "
					+ "account_number = ?",
					new Object[] 
					{ 	
						revolvingCreditCardInterest.getTotalOutstandingInterest(),
						revolvingCreditCardInterest.getStrTotalCalculatedGST(), 
						revolvingCreditCardInterest.getStrAccountType(),
						revolvingCreditCardInterest.getStrAccountNumber()
					});
			System.out.println("Count updateSomeAccountMasterRecord()" + count);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	public void insertRevolvingInterestInAccountTxnMaster(RevolvingCreditCardInterest revolvingCreditCardInterest) 
	{
		try
		{
			int sql= this.jdbcTemplate.update("INSERT INTO account_tran_master "
					+ "(participant_id,"
					+ "sys_id, "
					+ "transaction_amount, "
					+ "switch_txn_date "
					//+ "txn_time"
					+ ")"
					+ " values(?,?,?,?)",
					new Object[]  
					{
						
							revolvingCreditCardInterest.getStrParticipantId(),
							revolvingCreditCardInterest.getStrTxnId(),
							revolvingCreditCardInterest.getStrTotalOutstandingBal(),
							//revolvingCreditCardInterest.getSwitchTxnDate()
							//revolvingCreditCardInterest.getTxnTime()
							new Timestamp(System.currentTimeMillis())
							//new java.sql.Time(System.currentTimeMillis())
					});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public int updateNotifyPaymentDueDateOfAccountMaster(AccountCreation accountCreation) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE account_master am "
					+ "SET "
					+ "am.notify_by_payment_date = DATE_ADD(CAST(CURRENT_TIMESTAMP as DATE), INTERVAL + 7 DAY ) "
					+ "WHERE "
					+ "account_type = ? and "
					+ "account_number = ?",
					new Object[] 
					{ 	
						accountCreation.getStrAccountType(),
						accountCreation.getStrAccountNumber()
					});
			System.out.println("Count updateSomeAccountMasterRecord()" + count);
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public List<RevolvingCreditCardTxnMaster> getRevolvingCreditcardTxnMasterAccountWise(RevolvingCreditCardInterest revolvingCreditCardTxnMaster) 
	{
		try 
		{
			StringBuilder sqlQuery = new StringBuilder("SELECT ");
			//sqlQuery.append("rcctm.remaning_grace_period AS strRemaningGracePeriod, ");
			//sqlQuery.append("rcctm.updated_txn_amount As strUpdateTxnAmount, ");
			sqlQuery.append("SUM(ROUND(rcctm.updated_txn_amount * rcctm.interest_rate/100/365, 2) *rcctm.remaning_grace_period) AS totalOutstandingInterest, ");
			sqlQuery.append("ROUND(SUM(ROUND(rcctm.updated_txn_amount * rcctm.interest_rate/100/365) *rcctm.remaning_grace_period)*18/100,2) AS  totalCalGstAmount ");
			sqlQuery.append("FROM revolving_credit_card_txn_master AS rcctm ");
			sqlQuery.append("WHERE rcctm.account_type=? ");
			sqlQuery.append("AND rcctm.account_number=? ");
			sqlQuery.append("AND rcctm.txn_date BETWEEN ? AND ? ");
			
			List<RevolvingCreditCardTxnMaster> revolvingCreditTxnMaster = this.jdbcTemplate.query(sqlQuery.toString(),
					new BeanPropertyRowMapper<RevolvingCreditCardTxnMaster>(RevolvingCreditCardTxnMaster.class),
					new Object[] {
							revolvingCreditCardTxnMaster.getStrAccountType(),
							revolvingCreditCardTxnMaster.getStrAccountNumber(),
							revolvingCreditCardTxnMaster.getGracePeriodStartDate(),
							revolvingCreditCardTxnMaster.getPaymentDueDate()
					});
			return revolvingCreditTxnMaster;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updatePaymentDueDateOFAccountMaster(AccountCreditCardStatement accountCreditCardStatement) 
	{
		int updateCount = 0;
		try 
		{
			StringBuilder sqlquery = new StringBuilder("UPDATE account_master AS  am ");
			sqlquery.append( "SET am.payement_due_date = ? ");
			sqlquery.append( "WHERE am.account_type = ? and am.account_number = ? ");
			
			List<Object> objectList = new ArrayList<Object>();
			
			objectList.add(accountCreditCardStatement.getCalculatedtDueDate());
			objectList.add(accountCreditCardStatement.getStrAccountType());
			objectList.add(accountCreditCardStatement.getStrAccountNumber());
			
			Object[] object = objectList.toArray();
			
			updateCount = this.jdbcTemplate.update(sqlquery.toString(), object);
			
			return updateCount;
			
		} catch (Exception e) 
		{
			e.printStackTrace();
		}
		return updateCount;
	}

	@Override
	public List<SchedularModel> getScheduleInfo() 
	{
		try 
		{
			StringBuilder sqlQuerySb = new StringBuilder("SELECT si.schedule_id AS scheduleId, si.schedule_param AS scheduleParam,");
			sqlQuerySb.append("si.schedule_name AS scheduleName, DATE_FORMAT(si.schedule_date,'%d/%m/%Y') AS scheduleDate, si.schedule_time AS scheduleTime,");
			sqlQuerySb.append("si.schedular_bean_name AS schedularBeanName, si.schedular_bean_method AS schedularBeanMethod ");
			sqlQuerySb.append("FROM schedular_info si WHERE si.schedule_time BETWEEN '"+Utils.getCurrentTimeInString()+":00' AND '"+Utils.getExtendedTimeInStringFromCuurentTime(5)+":00'");			
			
			//amsLogger.writeInfoLog("::getScheduleInfo sqlQuery=["+sqlQuerySb.toString()+"]"); 
			
			List<SchedularModel> scheduleList = jdbcTemplate.query(sqlQuerySb.toString(),
					new BeanPropertyRowMapper<SchedularModel>(SchedularModel.class),
					new Object[] {}
			);

			return scheduleList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateScheduleInfo(SchedularModel schedularModel) {

		int count = 0;
		try
		{
			StringBuilder sqlquerySb = new StringBuilder("UPDATE schedular_info si ");
			sqlquerySb.append("SET si.schedule_date = ? WHERE si.schedule_id = ? ");
			
			List<Object> objectList = new ArrayList<Object>();
			
			objectList.add(schedularModel.getUpdatedScheduleDate());
			objectList.add(schedularModel.getScheduleId());
			
			Object[] object = objectList.toArray();
			
			count = this.jdbcTemplate.update(sqlquerySb.toString(), object);
			
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return count;
	
	}
	
}
