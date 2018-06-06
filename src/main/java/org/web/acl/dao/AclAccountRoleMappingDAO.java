package org.web.acl.dao;

import org.web.acl.domain.AclAccountRoleMappingDO;
import org.web.acl.query.QueryAclAccountRoleMapping;

import java.util.List;

public interface AclAccountRoleMappingDAO {

	public AclAccountRoleMappingDO selectAclAccountRoleMappingByAclAccountRoleMappingId(Long aclAccountRoleMappingId);

	public List< AclAccountRoleMappingDO > selectAclAccountRoleMappingList(QueryAclAccountRoleMapping queryAclAccountRoleMapping);

	public Integer countAclAccountRoleMappingList(QueryAclAccountRoleMapping queryAclAccountRoleMapping);

	public int insertAclAccountRoleMapping(AclAccountRoleMappingDO aclAccountRoleMappingDO);

	public int updateAclAccountRoleMappingByAclAccountRoleMappingId(AclAccountRoleMappingDO aclAccountRoleMappingDO);
	
	public List<String> selectDistinctList(QueryAclAccountRoleMapping queryAclAccountRoleMapping);

}

