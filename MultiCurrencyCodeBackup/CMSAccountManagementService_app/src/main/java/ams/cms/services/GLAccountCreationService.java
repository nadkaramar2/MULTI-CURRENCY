package ams.cms.services;

import java.util.List;

import ams.cms.model.GLAccountCreation;

public interface GLAccountCreationService 
{
	GLAccountCreation addGLAccountType(GLAccountCreation glAccountCreation) throws Exception;
	
	boolean isGLAccountTypeAlreadyExist(GLAccountCreation glAccountCreation);
	
	boolean isGLAccountAccountNumberAlreadyExist(GLAccountCreation glAccountCreation);

	List<GLAccountCreation> getGlAccTypeAccNumber(GLAccountCreation glAccountCreation);
	
	int updateClosingBalance(GLAccountCreation glAccountCreation) ;
	
	GLAccountCreation isGLAccountTypeExist(GLAccountCreation glAccountCreation);
	
	String getAllGLTogetherBalance();
	
	List<GLAccountCreation> getGlAccTypeAccNumberList(GLAccountCreation glAccountCreation);
}
