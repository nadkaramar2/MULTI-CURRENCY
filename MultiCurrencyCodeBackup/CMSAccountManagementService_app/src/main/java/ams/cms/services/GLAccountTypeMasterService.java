package ams.cms.services;

import java.util.List;


import ams.cms.model.GLAccountTypeMaster;

public interface GLAccountTypeMasterService 
{
	List<GLAccountTypeMaster> getGLAccountTypesList(GLAccountTypeMaster glAccountTypeMaster);
	
	String getClosingBalanceOfGlAccount(GLAccountTypeMaster glAccountTypeMaster);
	
	int updateGLAccountTypeDetails(GLAccountTypeMaster glAccountTypeMaster);

	List<GLAccountTypeMaster> GLAccountcreationView( GLAccountTypeMaster glAccountviewModel);
	
	GLAccountTypeMaster getSingleGLAccountTypeMasterObj(GLAccountTypeMaster glAccountviewModel) throws Exception; 
	
	GLAccountTypeMaster getMappedGLAccountTypeMasterObjWithAccountType(GLAccountTypeMaster glAccountTypeMaster) throws Exception;
	
	// Added by pankaj pawar for charts of accounts menu [Start]
	List<GLAccountTypeMaster> getGLAccountList(GLAccountTypeMaster glAccountTypeMaster);
	
	//created by ankit
	List<GLAccountTypeMaster> getGLAccountTypesListThirdPartyAllow();
	
	List<GLAccountTypeMaster> getDataOfGLAccount(GLAccountTypeMaster glAccountTypeMaster);
	

	List<GLAccountTypeMaster> getGLDescription(GLAccountTypeMaster glAccountTypeMaster);
	//created by ankit

	//added by ankit on 04-05-2023
	List<GLAccountTypeMaster> getClosingBalanceAndDescriptionOfGlAccount(GLAccountTypeMaster glAccountTypeMaster);
	//added by ankit on 04-05-2023
	
	GLAccountTypeMaster getGLAccountNumberOnAccountType(GLAccountTypeMaster glAccountTypeMaster);
	
	int[] updatesBatchEntryOfLinkedGLAccount(List<GLAccountTypeMaster> glAccountTypeMasters) throws Exception;
}
	


