package ams.cms.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountTypeChargesDao;
import ams.cms.dao.generic.AbstractGenericDao;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import ams.cms.model.AccountTypeCharges;

@Repository
public class AccountTypeChargesDaoImpl extends AbstractGenericDao<AccountTypeCharges> implements AccountTypeChargesDao {

	
	@Autowired
	JdbcTemplate jdbcTemplate;

	@Override
	@SuppressWarnings("deprecation")
	public List<AccountTypeCharges> getSelectedChargesAccountTypeWise(AccountTypeCharges accountTypeCharges) {
		try {

			List<AccountTypeCharges> accTypeChargeList = this.jdbcTemplate.query(
					" SELECT charge_type AS strChargeType, charge_description AS strChargeDescription,"
							+ "amount AS strAmount FROM account_type_charges " + " WHERE account_type = 'GEN'",
					new Object[] {}, (RowMapper<AccountTypeCharges>) new BeanPropertyRowMapper<AccountTypeCharges>(
							AccountTypeCharges.class));
			System.out.println("ChargeMasterDaoImpl.getSelectedChargesAccountTypeWise()" + accTypeChargeList);
			return accTypeChargeList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	
	//batch entry for charging module conf...Added by prashant
	@Override
	public int[] batchEntryforAccountTypeChargesData(List<AccountTypeCharges> accountTypeCharges) {
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
							ps.setNString(6, accountTypeCharges.get(i).getStrPercentage());
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


	

}
