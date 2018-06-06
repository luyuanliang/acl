package org.web.acl.query;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.web.domain.QueryBase;

@Setter
@Getter
public class QueryAclRole extends QueryBase {

	
	private Long aclRoleId;
				
	private List < Long > aclRoleIdList;
		
	private String roleGroup;
				
	private List < String > roleGroupList;
		
	private String roleName;
									
	private String businessLine;
				
	private List < String > businessLineList;
		
	private String isDelete;
				}

