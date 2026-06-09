package ams.cms.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.CustomerIdTableDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.CustomerIdTable;

@Repository
public class CustomerIdTableDaoImpl extends AbstractGenericDao<CustomerIdTable> implements CustomerIdTableDao 
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int updateCustidtbl(CustomerIdTable customerIdTable) {
		int count = this.jdbcTemplate.update(
				"UPDATE customer_id_table "
				+ "SET last_txn_serial_no = ?, create_date = ?, created_by = ? "
				+ "WHERE year = ? and julian_date = ?",
		new Object[] { 
				customerIdTable.getstrLastTxnSerialNo(),
				customerIdTable.getStrCreatedDate(),
				customerIdTable.getstrCreatedby(),
				customerIdTable.getStrYear(),
				customerIdTable.getstrJulianDate()
		});
		return count;
	}

}
