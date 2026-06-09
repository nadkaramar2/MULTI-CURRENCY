package ams.cms.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.AccountKycDetailsDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.AccountKycDetails;

@Repository
public class AccountKycDetailsDaoImpl extends AbstractGenericDao<AccountKycDetails> implements AccountKycDetailsDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@SuppressWarnings("unchecked")
	@Override
	public AccountKycDetails getSingleAccountKycDetail(AccountKycDetails accountKycDetails) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (accountKycDetails.getStrParticipantID()!=null && accountKycDetails.getStrParticipantID().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strParticipantID", accountKycDetails.getStrParticipantID()));
			}
			if (accountKycDetails.getStrAccountType()!=null && accountKycDetails.getStrAccountType().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountType", accountKycDetails.getStrAccountType()));
			}
			if (accountKycDetails.getStrCustId()!=null && accountKycDetails.getStrCustId().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strCustId", accountKycDetails.getStrCustId()));
			}
			if (accountKycDetails.getStrMobileNo()!=null && accountKycDetails.getStrMobileNo().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strMobileNo", accountKycDetails.getStrMobileNo()));
			}
			
			List<AccountKycDetails> listData = (List<AccountKycDetails>) criteria.list();
			
			if (listData !=null && listData.size() > 0) 
			{
				accountKycDetails = listData.get(0);
				return accountKycDetails;
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getSingleAccountKycDetail::"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public int updateKycDetailsInfo(AccountKycDetails accountKycDetails) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE kyc_details "
					+ "SET address_proof_document_type = ?, "
					+ " address_proof_document_value = ?, "
					+ " identity_proof_document_type = ?, "
					+ "	identity_proof_document_value = ?"
					+ " WHERE "
					+ "participant_id = ? and "
					+ "account_type = ? and "
					+ "cust_id = ?",
			new Object[] { accountKycDetails.getStrAddressProofDocumentType(), 
						accountKycDetails.getStrAddressDocumentValue(), 
						accountKycDetails.getStrIdentityProofDocumentType(), 
						accountKycDetails.getStrIdentityProofDocumentValue(),
						accountKycDetails.getStrParticipantID(), 
						accountKycDetails.getStrAccountType(), 
						accountKycDetails.getStrCustId() });
			System.out.println("count" + count);
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int updateKycDetail(AccountKycDetails accountKycDetails) 
	{
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE kyc_details "
					+ "SET cust_id = ?, "
					+ " participant_id = ? "
					+ " WHERE "
					+ "mobile_no = ?",
					new Object[] 
					{ 
						accountKycDetails.getStrCustId(),
						accountKycDetails.getStrParticipantID(), 
						accountKycDetails.getStrMobileNo()
					});
			System.out.println("count" + count);
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateAddressInformation(AccountKycDetails accountKycDetails) 
	{
		try 
		{
			String updateAddressInformation = "UPDATE kyc_details SET address_proof_document_type = ?, address_proof_document_value = ? WHERE mobile_no = ?";
			int count = this.jdbcTemplate.update(updateAddressInformation, new Object[] 
			{ 
				accountKycDetails.getStrAddressProofDocumentType(),
				accountKycDetails.getStrAddressDocumentValue(), 
				accountKycDetails.getStrMobileNo() 
			});
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
		@SuppressWarnings("unchecked")
	public AccountKycDetails getAccountKycDetails(String mobileNo) {
		try {
			AccountKycDetails accountKycDetails;
			Criteria criteria = createEntityCriteria();
			if (mobileNo != null && mobileNo.trim().length() > 0) {
				criteria.add(Restrictions.eq("strMobileNo",mobileNo ));
			}
			List<AccountKycDetails> listData = (List<AccountKycDetails>) criteria.list();

			if (listData != null && listData.size() > 0) {
				accountKycDetails = listData.get(0);
				return accountKycDetails;
			}
		} catch (Exception e) {
			System.out.println("Exception in getSingleAccountKycDetail::" + e);
			e.printStackTrace();
		}
		return null;
	}
		
	@Override
	public int updateIdentityInformation(AccountKycDetails accountKycDetails) 
	{
		try 
		{
			String updateAddressInformation = "UPDATE kyc_details SET identity_proof_document_type = ?, identity_proof_document_value = ? WHERE mobile_no = ?";
			int count = this.jdbcTemplate.update(updateAddressInformation, new Object[] { accountKycDetails.getStrIdentityProofDocumentType(),
							accountKycDetails.getStrIdentityProofDocumentValue(), accountKycDetails.getStrMobileNo() });
			return count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}	
	
	//changes added by prashant for customer kyc update
	@Override
	public int updateKycDetailsInfoCustomerDetails(AccountKycDetails accountKycDetails) {
		try 
		{
			int count = this.jdbcTemplate.update("UPDATE kyc_details "
					+ "SET address_proof_document_type = ?, "
					+ " address_proof_document_value = ?, "
					+ " identity_proof_document_type = ?, "
					+ "	identity_proof_document_value = ?"
					+ " WHERE "
					+ "participant_id = ? and "
					+ "mobile_no = ? and "
					+ "cust_id = ?",
			new Object[] { accountKycDetails.getStrAddressProofDocumentType(), 
						accountKycDetails.getStrAddressDocumentValue(), 
						accountKycDetails.getStrIdentityProofDocumentType(), 
						accountKycDetails.getStrIdentityProofDocumentValue(),
						accountKycDetails.getStrParticipantID(), 
						accountKycDetails.getStrMobileNo(), 
						accountKycDetails.getStrCustId() });
			System.out.println("count" + count);
			return count;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public boolean isDocumentValueAlreadyExist(String documentValue) 
	{
		try 
		{
			String sql = "select kyc.mobile_no as strMobileNo from kyc_details kyc where kyc.address_proof_document_value = '"+documentValue+"' OR kyc.identity_proof_document_value='"+documentValue+"';";
			
			List<String> strLst = this.jdbcTemplate.query(sql, new RowMapper<String>() 
			{
			    public String mapRow(ResultSet rs, int rowNum) throws SQLException
			    {
			        return rs.getString(1);
			    }
			});
			
			if (strLst.size() > 0) 
			{ 
			    return true;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}
}
