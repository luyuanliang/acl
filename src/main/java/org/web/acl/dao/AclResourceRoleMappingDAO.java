package org.web.acl.dao;

import java.util.List;
import org.web.acl.query.QueryAclResourceRoleMapping;
import org.web.acl.domain.AclResourceRoleMappingDO;

public interface AclResourceRoleMappingDAO {

	public AclResourceRoleMappingDO selectAclResourceRoleMappingByAclResourceRoleMappingId(Long aclResourceRoleMappingId);

	public List< AclResourceRoleMappingDO > selectAclResourceRoleMappingList(QueryAclResourceRoleMapping queryAclResourceRoleMapping);

	public Integer countAclResourceRoleMappingList(QueryAclResourceRoleMapping queryAclResourceRoleMapping);

	public int insertAclResourceRoleMapping(AclResourceRoleMappingDO aclResourceRoleMappingDO);

	public int updateAclResourceRoleMappingByAclResourceRoleMappingId(AclResourceRoleMappingDO aclResourceRoleMappingDO);
	
	public List<String> selectDistinctList(QueryAclResourceRoleMapping queryAclResourceRoleMapping);

}

