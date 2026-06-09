package ams.cms.api.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.AddressProofDocumentTypeDao;
import ams.cms.api.model.AddressProofDocumentType;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class AddressProofDocumentTypeDaoImpl extends AbstractGenericDao<AddressProofDocumentType> implements AddressProofDocumentTypeDao
{
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<String> getAddressDocumentTypes()
	{
		try
		{
			String sql = "select document_description as strDocumentDescription from address_proof_document_type_master";
			List<String> addressTypeList = this.jdbcTemplate.queryForList(sql,String.class);
			return addressTypeList;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String getPrefixOfDocumentType(String identityProofDocumentType) 
	{
		try
		{
			String sql = "select document_type as strDocumentTypeDescription from address_proof_document_type_master where document_description = ?";
			String identityTypePrefix = this.jdbcTemplate.queryForObject(sql, String.class, new Object[] {identityProofDocumentType});
			return identityTypePrefix;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
