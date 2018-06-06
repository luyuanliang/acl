package org.web.acl.query;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.web.domain.QueryBase;

@Setter
@Getter
public class QueryAclResourceRoleMapping extends QueryBase {

	
	private Long aclResourceRoleMappingId;
				
	private List < Long > aclResourceRoleMappingIdList;
		
	private Long aclResourceId;
				
	private List < Long > aclResourceIdList;
		
	private Long aclRoleId;
				
	private List < Long > aclRoleIdList;
		
	private String businessLine;
							
	private Date moreThanCreateTime;
	private Date lessThanCreateTime;
	private Date equalAndMoreThanCreateTime;
	private Date equalAndLessThanCreateTime;
					
	private Date moreThanUpdateTime;
	private Date lessThanUpdateTime;
	private Date equalAndMoreThanUpdateTime;
	private Date equalAndLessThanUpdateTime;
			
	private String isDelete;
				}

