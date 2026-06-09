package ams.cms.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.dao.CardAccountLinkageDao;
import ams.cms.dao.generic.AbstractGenericDao;
import ams.cms.model.CardAccountLinkage;

@Repository
public class CardAccountLinkageDaoImpl extends AbstractGenericDao<CardAccountLinkage> implements CardAccountLinkageDao
{
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Override
	public List<CardAccountLinkage> getCardAccountLinkageBasedOnAccount(CardAccountLinkage cardAccountLinkage) 
	{
		String accountLinkSearchQuery = "SELECT id AS strID,participant_id AS strParticipantID, "
				+ "account_type AS strAccountType,account_number AS strAccountNumber, "
				+ "card_number AS strCardNumber,card_type AS strCardType, "
				+ "card_status AS strCardStatus,account_status AS strAccountStatus, "
				+ "creation_date AS strCreationDate, "
				+ "created_by AS strCreatedBy from card_account_linkage_master AS link "
				+ "where link.account_type = ? and link.account_number = ? ";
		
		List<CardAccountLinkage> getCardAccountLinkage = jdbcTemplate.query(accountLinkSearchQuery,
				new BeanPropertyRowMapper<CardAccountLinkage>(CardAccountLinkage.class),
				new Object[] 
				{ 
					cardAccountLinkage.getStrAccountType(), 
					cardAccountLinkage.getStrAccountNumber()
				});
			return getCardAccountLinkage;
	}

	@Override
	public List<CardAccountLinkage> getCardAccountLinkageDataBasedOnCard(CardAccountLinkage cardAccountLinkage) 
	{
		String sqlQuery = "SELECT id AS strID,participant_id AS strParticipantID, "
				+ "account_type AS strAccountType,account_number AS strAccountNumber,card_number AS strCardNumber,"
				+ "card_type AS strCardType,card_status AS strCardStatus,account_status AS strAccountStatus,"
				+ "creation_date AS strCreationDate,created_by AS strCreatedBy from `card_account_linkage_master` "
				+ "AS link where link.card_type = ? and link.enc_card_number = CARD_TOKEN(?) ";
				//+ "AS link where link.card_type = ? and link.card_number = ? ";
		
		List<CardAccountLinkage> getCardAccountLinkage = jdbcTemplate.query(sqlQuery,
				new BeanPropertyRowMapper<CardAccountLinkage>(CardAccountLinkage.class),
				new Object[] { cardAccountLinkage.getStrCardType(), cardAccountLinkage.getStrCardNumber() });
		return getCardAccountLinkage;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CardAccountLinkage> getCardAccountlinkagelist(CardAccountLinkage cardAccountLinkage) {
		try 
		{
			Criteria criteria = createEntityCriteria();
			criteria.add(Restrictions.eq("strParticipantID", cardAccountLinkage.getStrParticipantID()));
			criteria.add(Restrictions.eq("strAccountType", cardAccountLinkage.getStrAccountType()));
			
			List<CardAccountLinkage> cardlinkagelistdata = (List<CardAccountLinkage>) criteria.list();
			if (cardlinkagelistdata !=null && cardlinkagelistdata.size() > 0) 
			{
				return cardlinkagelistdata;
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getAccountCardLinkagelist::"+e);
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings({ "unchecked" })
	@Override
	public List<CardAccountLinkage> getLinkagecardlist(CardAccountLinkage cardAccountLinkage) 
	{
		try 
		{
			Criteria criteria = createEntityCriteria();
			if (cardAccountLinkage.getStrAccountNumber()!=null && cardAccountLinkage.getStrAccountNumber().trim().length() > 0)
			{
				criteria.add(Restrictions.eq("strAccountNumber", cardAccountLinkage.getStrAccountNumber()));
			}
			
			List<CardAccountLinkage> cardlinkagelistData = (List<CardAccountLinkage>) criteria.list();
			if (cardlinkagelistData !=null && cardlinkagelistData.size() > 0) 
			{
				return cardlinkagelistData;
			}
		}
		catch (Exception e) 
		{
			System.out.println("Exception in getLinkagecardlist::"+e);
			e.printStackTrace();
		}
	return null;
	}
	
		@Override
	public boolean isCardAccountLinkageDataExist(CardAccountLinkage cardAccountLinkage)
	{
		try
		{
			int count = this.jdbcTemplate.queryForObject("SELECT COUNT(id) FROM card_account_linkage_master WHERE account_number = ? AND card_number = ?",
			Integer.class, 
			new Object[] 
			{
				cardAccountLinkage.getStrAccountNumber(),
				cardAccountLinkage.getStrCardNumber()
			}
			);
			return count > 0;
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


	@Override
	public List<CardAccountLinkage> getAccountCardLinkagelist(CardAccountLinkage cardAccountLinkage) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public CardAccountLinkage getCardAccountLinkages(CardAccountLinkage cardAccountLinkage) 
	{
		try
		{
	        List<CardAccountLinkage> cardAccountLinkages = this.jdbcTemplate.query(
					"SELECT account_type AS strAccountType, account_number AS strAccountNumber, "
					+"card_number AS strCardNumber, card_type AS strCardType, "
					+"card_status AS strCardStatus, account_status AS strAccountStatus, "
					+"creation_date AS strCreationDate FROM card_account_linkage_master "
					+ "WHERE participant_id = ? AND account_number = ? AND card_number = ?",
					
					(RowMapper) new BeanPropertyRowMapper(CardAccountLinkage.class),
					
					new Object[] 
					{
						cardAccountLinkage.getStrParticipantID(), 
						(cardAccountLinkage.getStrAccountNumber()!=null) ? cardAccountLinkage.getStrAccountNumber() : "", 
						(cardAccountLinkage.getStrCardNumber()!=null) ? cardAccountLinkage.getStrCardNumber() : ""
					}
					);
					
	        if(cardAccountLinkages!=null && cardAccountLinkages.size() > 0)
	        {
	        	cardAccountLinkage = cardAccountLinkages.get(0);
	        }
	        return cardAccountLinkage;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<CardAccountLinkage> getCardLinkAccountList(CardAccountLinkage cardAccountLinkage) 
	{
		try 
		{
			List<CardAccountLinkage> cardAccountLinkageList = this.jdbcTemplate.query(
					" SELECT id as strID, account_type AS strAccountType, account_number AS strAccountNumber, card_type AS strCardType, "
					+ " card_number AS strCardNumber from card_account_linkage_master "
					+ "WHERE	account_number = ?",
							
					(RowMapper) new BeanPropertyRowMapper(CardAccountLinkage.class),
					
					new Object[] 
					{ 
						cardAccountLinkage.getStrAccountNumber() 
					});
			return cardAccountLinkageList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<CardAccountLinkage> getLinkageCardDetailsBasedOnCustId(CardAccountLinkage cardAccountLinkage) 
	{
		try
		{
	        List<CardAccountLinkage> cardAccountLinkages = this.jdbcTemplate.query(
					"SELECT calm.card_number AS strCardNumber, "
					+"calm.card_type AS strCardType, "
					+"calm.card_description AS strCardDescription, "
					+"calm.card_status AS strCardStatus "
					+"FROM account_master AS acm "
					+"INNER JOIN card_account_linkage_master AS calm "
					+"ON acm.account_number = calm.account_number "
					+"WHERE acm.cust_id = ?",					
					(RowMapper) new BeanPropertyRowMapper(CardAccountLinkage.class),					
					new Object[] 
					{
						cardAccountLinkage.getStrCustId()
					}
					);
					
	        if(cardAccountLinkages!=null && cardAccountLinkages.size() > 0)
	        {
	        	return cardAccountLinkages;
	        }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public CardAccountLinkage getLinkageCardDetailsOnCustId(CardAccountLinkage cardAccountLinkage) 
	{
		try
		{
			StringBuilder sqlQuery = new StringBuilder("SELECT CONCAT_WS(' ', acm.first_name, acm.middle_name, acm.last_name) AS strAccountHolderName, ");
			sqlQuery.append("calm.card_number AS strCardNumber, clear_card(calm.enc_card_number) AS strEncryptCard, ");
			sqlQuery.append("calm.card_type AS strCardType, calm.token_card AS strTokenCard, ");
			sqlQuery.append("calm.card_status AS strCardStatus, acm.account_number AS strAccountNumber, acm.account_type AS strAccountType");
			sqlQuery.append("acm.email AS strEmail");
			sqlQuery.append("FROM account_master AS acm ");
			sqlQuery.append("INNER JOIN card_account_linkage_master AS calm ");
			sqlQuery.append("ON acm.account_number = calm.account_number ");
			sqlQuery.append("WHERE acm.cust_id = ? AND acm.account_number = ? AND calm.token_card = ? ");
			
	        List<CardAccountLinkage> cardAccountLinkages = this.jdbcTemplate.query(
					sqlQuery.toString(),
					(RowMapper) new BeanPropertyRowMapper(CardAccountLinkage.class),
					new Object[] 
					{
						cardAccountLinkage.getStrCustId(),
						cardAccountLinkage.getStrAccountNumber(),
						cardAccountLinkage.getStrTokenCard()
					}
					);
					
	        if(cardAccountLinkages!=null && cardAccountLinkages.size() > 0)
	        {
	        	return cardAccountLinkages.get(0);
	        }
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateCardStatusBlock(CardAccountLinkage cardAccountLinkage) {
		try 
		{
			String cardStatusValue = "Block";
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "card_status = ?, "
					+ "WHERE account_number = ? and token_card = ? ",
					new Object[] 
					{ 	
							cardStatusValue,
							cardAccountLinkage.getStrAccountNumber(),
							cardAccountLinkage.getStrTokenCard()
					});
			System.out.println("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int updateCardStatusUnBlock(CardAccountLinkage cardAccountLinkage) {
		try 
		{
			String cardStatusValue = "Active";
			int count = this.jdbcTemplate.update("UPDATE account_master "
					+ "SET "
					+ "card_status = ?, "
					+ "WHERE account_number = ? and card_type = ? ",
					new Object[] 
					{ 	
							cardStatusValue,
							cardAccountLinkage.getStrAccountNumber(),
							cardAccountLinkage.getStrCardType()
					});
			System.out.println("Count updateBalnceLimitValues()" + count);
			return count;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
	
	@Override
	public int updateExpiryDate(CardAccountLinkage cardAccountLinkage) 
	{
		try 
		{
			String updateExpiryDate = "UPDATE card_account_linkage_master SET card_expiry_date = ? WHERE token_card = ? ";
			int update = this.jdbcTemplate.update(updateExpiryDate,new Object[] {cardAccountLinkage.getStrCardExpDate(),cardAccountLinkage.getOldTokenCard()});
			return update;
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return 0;
	}
}
