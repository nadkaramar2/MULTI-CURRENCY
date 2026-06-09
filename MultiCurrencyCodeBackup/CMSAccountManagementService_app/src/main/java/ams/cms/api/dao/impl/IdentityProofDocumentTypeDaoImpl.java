package ams.cms.api.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ams.cms.api.dao.IdentityProofDocumentTypeDao;
import ams.cms.api.model.IdentityProofDocumentType;
import ams.cms.dao.generic.AbstractGenericDao;

@Repository
public class IdentityProofDocumentTypeDaoImpl extends AbstractGenericDao<IdentityProofDocumentType> implements IdentityProofDocumentTypeDao{
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public List<String> getIdentityDocumentTypes() {
		try {
			String sql = "select document_description as strDocumentDescription from identity_proof_document_type_master";
			List<String> addressTypeList = this.jdbcTemplate.queryForList(sql,String.class);
			return addressTypeList;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String getPrefixOfDocumentType(String identityProofDocumentType) {
		try {
			String sql = "select document_type as strDocumentTypeDescription from identity_proof_document_type_master where document_description = ?";
			String identityTypePrefix = this.jdbcTemplate.queryForObject(sql,
					new Object[] {identityProofDocumentType},
											String.class);
			return identityTypePrefix;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
