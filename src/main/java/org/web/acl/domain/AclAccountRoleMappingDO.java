/**
* AclAccountRoleMappingDO entity encapsulation table AclAccountRoleMapping record. 
* @author luyl,Generated by the auto build tools .
* @Time 2017-09-14 14:56:48
*/
package org.web.acl.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AclAccountRoleMappingDO {

	// 主键
	private Long aclAccountRoleMappingId;
	// 业务线
	private String businessLine;
	// AclAccount表主键
	private Long aclAccountId;
	// 账户ID,相当与aclAccountId
	private String accountNum;
	// 角色ID
	private Long aclRoleId;
	// 记录操作者
	private String inputer;
	// 记录创建时间
	private Date createTime;
	// 记录修改时间
	private Date updateTime;
	// 记录是否逻辑删除
	private String isDelete;
	
}


