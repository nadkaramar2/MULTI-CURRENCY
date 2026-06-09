package ams.cms.dao;

import java.util.List;

import ams.cms.model.GLAccountCreation;

public interface GLAccountCreationDao extends GenericDao<GLAccountCreation> 
{
	boolean isGLAccountTypeAlreadyExist(GLAccountCreation GLAccountCreation);
	
	boolean isGLAccountAccountNumberAlreadyExist(GLAccountCreation GLAccountCreation);

	List<GLAccountCreation> getGlAccTypeAccNumber(GLAccountCreation glAccountCreation);
	
	int updateClosingBalance(GLAccountCreation glAccountCreation);
	
	GLAccountCreation isGLAccountTypeExist(GLAccountCreation glAccountCreation);
	
	String getAllGLTogetherBalance();
	
	List<GLAccountCreation> getGlAccTypeAccNumberList(GLAccountCreation glAccountCreation);
}
