package org.web.acl.dao;

import java.util.List;
import org.web.acl.query.QueryAclRole;
import org.web.acl.domain.AclRoleDO;

public interface AclRoleDAO {

	public AclRoleDO selectAclRoleByAclRoleId(Long aclRoleId);

	public List< AclRoleDO > selectAclRoleList(QueryAclRole queryAclRole);

	public Integer countAclRoleList(QueryAclRole queryAclRole);

	public int insertAclRole(AclRoleDO aclRoleDO);

	public int updateAclRoleByAclRoleId(AclRoleDO aclRoleDO);
	
	public List<String> selectDistinctList(QueryAclRole queryAclRole);

}

