package ams.cms.dao.impl;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.dao.CloseAccountMasterDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.CloseAccountMaster;

@Repository
public class CloseAccountMasterDaoImpl extends AbstractGenericDao<CloseAccountMaster> implements CloseAccountMasterDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public int updateCloseAccountMaster(CloseAccountMaster CloseAccountMaster) {
		try {
			StringBuilder updateQuerySb = new StringBuilder("UPDATE account_close_master SET cheker_user_id = ? , request_status = ? , ");
			updateQuerySb.append(" account_closed_date = ?, account_closed_time = ? , transaction_id = ? , ");
			updateQuerySb.append(" reason_for_rejection = ?, account_closure_rejected_date = ? , account_closure_rejected_time = ?  ");
			updateQuerySb.append("WHERE account_no = ? AND cust_id = ?  AND account_type = ? ");
			

			int count = this.jdbcTemplate.update(updateQuerySb.toString(),
					new Object[] {
							CloseAccountMaster.getChekerUserId(), 
							CloseAccountMaster.getRequestStatus(),
							CloseAccountMaster.getAccountClosedDate(),
							CloseAccountMaster.getAccountClosedTime(),
							CloseAccountMaster.getTransactionId(),
							CloseAccountMaster.getReasonForRejection(),
							CloseAccountMaster.getAccountClosureRejectedDate(),
							CloseAccountMaster.getAccountClosureRejectedTime(),
							CloseAccountMaster.getAccountNo(),
							CloseAccountMaster.getStrCustId(),
							CloseAccountMaster.getStrAccountType()
							
			
			});
			System.out.println(updateQuerySb.toString());
			return count;
		} catch (Exception e) {
		System.out.println(e.getMessage().toString());
		}
		return 0;
	}

	@Override
	public int updateCloseAccountMasterForRejection(CloseAccountMaster CloseAccountMaster) {
		try {
			StringBuilder updateQuerySb = new StringBuilder("UPDATE account_close_master SET cheker_user_id = ? , request_status = ? , ");
			updateQuerySb.append(" reason_for_rejection = ?, account_closure_rejected_date = ? , account_closure_rejected_time = ?  ");
			updateQuerySb.append("WHERE account_no = ? AND cust_id = ?  AND account_type = ? ");
	

			int count = this.jdbcTemplate.update(updateQuerySb.toString(),
					new Object[] {
							CloseAccountMaster.getChekerUserId(), 
							CloseAccountMaster.getRequestStatus(),
							CloseAccountMaster.getReasonForRejection(),
							CloseAccountMaster.getAccountClosureRejectedDate(),
							CloseAccountMaster.getAccountClosureRejectedTime(),
							CloseAccountMaster.getAccountNo(),
							CloseAccountMaster.getStrCustId(),
							CloseAccountMaster.getStrAccountType()
							
			
			});
			System.out.println(updateQuerySb.toString());
			return count;
		} catch (Exception e) {
		System.out.println(e.getMessage().toString());
		}
		return 0;
	}

}
