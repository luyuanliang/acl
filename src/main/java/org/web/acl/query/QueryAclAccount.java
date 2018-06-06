package org.web.acl.query;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;
import org.web.domain.QueryBase;

@Setter
@Getter
public class QueryAclAccount extends QueryBase {

	
	private Long aclAccountId;
				
	private List < Long > aclAccountIdList;
		
	private String accountName;
		
	private String indistinctAccountName;
				
	private String accountNum;
				
	private List < String > accountNumList;
		
	private String accountStatus;
					
	private String businessLine;
				
	private List < String > businessLineList;
		
	private String phone;
					
	private String mail;
					
	private String firstDepartment;
					
	private String secondDepartment;
					
	private String position;
					
	private String isDelete;
				}

