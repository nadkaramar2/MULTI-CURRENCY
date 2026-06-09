package ams.cms.dao;

import java.util.List;

import ams.cms.model.GLAccountTypeMaster;

public interface GLAccountTypeMasterDao extends GenericDao<GLAccountTypeMaster>
{
	List<GLAccountTypeMaster> getGLAccountTypesList(GLAccountTypeMaster glAccountTypeMaster);
	
	String getClosingBalanceOfGlAccount(GLAccountTypeMaster glAccountTypeMaster);
	
	int updateGLAccountTypeDetails(GLAccountTypeMaster glAccountTypeMaster);

	List<GLAccountTypeMaster> GLAccountcreationView(GLAccountTypeMaster glAccountviewModel);
	
	GLAccountTypeMaster getSingleGLAccountTypeMasterObj(GLAccountTypeMaster glAccountviewModel); 
	
	GLAccountTypeMaster getMappedGLAccountTypeMasterObjWithAccountType(GLAccountTypeMaster glAccountTypeMaster);

	// Added by pankaj pawar for charts of accounts menu [Start]
	List<GLAccountTypeMaster> getGLAccountList(GLAccountTypeMaster glAccountTypeMaster);
	
	//created by ankit -start-
	List<GLAccountTypeMaster> getGLAccountTypeListThirdPartyAllow();
	
	List<GLAccountTypeMaster> getDataOfGLAccount(GLAccountTypeMaster glAccountTypeMaster);

	List<GLAccountTypeMaster> getGLAccountDescription(GLAccountTypeMaster glAccountTypeMaster);
	//created by ankit -end-

	List<GLAccountTypeMaster> getGLAccountTypeListExceptCTR();
	
	//created by ankit on 04-05-2023
	List<GLAccountTypeMaster> getGLClosingBalanceAndDescription(GLAccountTypeMaster glAccountTypeMaster);
	//created by ankit on 04-05-2023
	
	GLAccountTypeMaster getGLAccountNumberOnAccountType(GLAccountTypeMaster glAccountTypeMaster);

	GLAccountTypeMaster getGLAccountNumberObjectOnAccountType(GLAccountTypeMaster glAccountTypeMaster);

	List<GLAccountTypeMaster> getDataOfGLAccountTypeMaster(GLAccountTypeMaster glAccountTypeMaster);
	
	int[] updatesBatchEntryOfLinkedGLAccount(List<GLAccountTypeMaster> glAccountTypeMasters);	
}
