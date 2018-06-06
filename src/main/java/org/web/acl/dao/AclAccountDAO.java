package org.web.acl.dao;

import java.util.List;
import org.web.acl.query.QueryAclAccount;
import org.web.acl.domain.AclAccountDO;

public interface AclAccountDAO {

	public AclAccountDO selectAclAccountByAclAccountId(Long aclAccountId);

	public List< AclAccountDO > selectAclAccountList(QueryAclAccount queryAclAccount);

	public Integer countAclAccountList(QueryAclAccount queryAclAccount);

	public int insertAclAccount(AclAccountDO aclAccountDO);

	public int updateAclAccountByAclAccountId(AclAccountDO aclAccountDO);
	
	public List<String> selectDistinctList(QueryAclAccount queryAclAccount);

}

