package ams.cms.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.ChargeMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountTypeCharges;
import ams.cms.model.ChargeMaster;

@Repository
public class ChargeMasterDaoImpl extends AbstractGenericDao<ChargeMaster> implements ChargeMasterDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	@SuppressWarnings("deprecation")
	public List<ChargeMaster> getChargeMasterList(ChargeMaster chargeMaster) {
		try {

			List<ChargeMaster> chargeMasterList = this.jdbcTemplate.query(
					"SELECT charge_type AS strChargeType, charge_description AS strChargeDescription FROM charge_master WHERE charge_related = 'C'",
					new Object[] {},
					(RowMapper<ChargeMaster>) new BeanPropertyRowMapper<ChargeMaster>(ChargeMaster.class));
			System.out.println("ChargeMasterDaoImpl.getChargeMasterList()" + chargeMasterList);
			return chargeMasterList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<ChargeMaster> getTransactionChargeList(ChargeMaster chargeMaster) throws Exception {
		try {

			List<ChargeMaster> chargeMasterList = this.jdbcTemplate.query(
					" SELECT charge_type AS strChargeType , charge_description AS strChargeDescription "
							+ " FROM charge_master WHERE charge_related = 'T'",
					new Object[] {},
					(RowMapper<ChargeMaster>) new BeanPropertyRowMapper<ChargeMaster>(ChargeMaster.class));
			System.out.println("ChargeMasterDaoImpl.getChargeMasterList()" + chargeMasterList);
			return chargeMasterList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<ChargeMaster> getFuelChargeList(ChargeMaster chargeMaster) throws Exception {
		try {

			List<ChargeMaster> chargeMasterList = this.jdbcTemplate.query(
					" SELECT charge_type AS strChargeType , charge_description AS strChargeDescription "
							+ " FROM charge_master WHERE charge_related = 'F'",
					new Object[] {},
					(RowMapper<ChargeMaster>) new BeanPropertyRowMapper<ChargeMaster>(ChargeMaster.class));
			System.out.println("ChargeMasterDaoImpl.getChargeMasterList()" + chargeMasterList);
			return chargeMasterList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<AccountTypeCharges> getSelectedChargesAccountTypeWise(AccountTypeCharges accountTypeCharges) 
	{
		try
		{
			
			StringBuilder sqlQuerySb = new StringBuilder("SELECT atc.account_type, cmst.charge_related AS strChargeRelated,");
			sqlQuerySb.append("atc.charge_type AS strChargeType, atc.charge_description AS strChargeDescription,");
			sqlQuerySb.append("atc.amount AS strAmount, atc.percentage AS strPercentage FROM account_type_charges atc ");
			sqlQuerySb.append("INNER JOIN charge_master cmst ON cmst.charge_type = atc.charge_type ");
			sqlQuerySb.append("WHERE atc.account_type = ?");

			List<AccountTypeCharges> accTypeChargeList = this.jdbcTemplate.query(sqlQuerySb.toString(),
					(RowMapper<AccountTypeCharges>) new BeanPropertyRowMapper<AccountTypeCharges>(AccountTypeCharges.class),
					new Object[] 
					{
							accountTypeCharges.getStrAccountType()
					});
			System.out.println("ChargeMasterDaoImpl.getSelectedChargesAccountTypeWise()" + accTypeChargeList);
			return accTypeChargeList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//batch entry for charging module conf...Added by prashant
	@Override
	public int[] batchEntryforChargingConfigData(List<AccountTypeCharges> accountTypeCharges) {
		int[] batchResponse = null;
		try {
			return this.jdbcTemplate.batchUpdate("INSERT INTO account_type_charges "
					+ " (participant_id,account_type,charge_type,charge_description,amount,percentage,created_by,created_date)"
					+ "values(?,?,?,?,?,?,?,?) ", new BatchPreparedStatementSetter() {

						@Override
						public void setValues(PreparedStatement ps, int i) throws SQLException {
							ps.setString(1, accountTypeCharges.get(i).getStrParticipantID());
							ps.setString(2, accountTypeCharges.get(i).getStrAccountType());
							ps.setString(3, accountTypeCharges.get(i).getStrChargeType());
							ps.setString(4, accountTypeCharges.get(i).getStrChargeDescription());
							ps.setString(5, accountTypeCharges.get(i).getStrAmount());
							ps.setString(6, accountTypeCharges.get(i).getStrPercentage());
							ps.setString(7, accountTypeCharges.get(i).getStrCreatedBy());
							ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
							System.out.println("new BatchPreparedStatementSetter() {...}" + ps);
						}

						@Override
						public int getBatchSize() {
							return accountTypeCharges.size();

						}

					});

		} catch (Exception e) {
			System.out.println("ChargeMasterDaoImpl.batchEntryforChargingConfigData()" + e);
			e.printStackTrace();
		}
		return batchResponse;
	}

	@Override
	@SuppressWarnings("deprecation")
	public String getChargeDescriptionBasedOnChargeType(String strChargeType) {
		try {

			String chargedesc = this.jdbcTemplate.queryForObject(
					" SELECT charge_description AS strChargeDescription "
							+ "  FROM charge_master WHERE charge_type = ? ",
					new Object[] {strChargeType}, String.class);
			return chargedesc;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Boolean validateChargeType(ChargeMaster chargeMaster) throws Exception 
	{
		try {	
			String sql = "SELECT COUNT(*) FROM charge_master "
					+ "where charge_type = ? "
					+ "AND charge_description = ?";

			String validateCount = this.jdbcTemplate.queryForObject(sql, String.class,
					new Object[]
					{
						chargeMaster.getStrChargeType(),
						chargeMaster.getStrChargeDescription()
					}
			);
			if(Integer.valueOf(validateCount) > 0)
			{
				return true;
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
