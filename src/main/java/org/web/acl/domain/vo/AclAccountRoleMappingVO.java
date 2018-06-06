package org.web.acl.domain.vo;

import lombok.Getter;
import lombok.Setter;
import org.web.acl.domain.AclAccountRoleMappingDO;

/**
 * Created by luyl on 17-9-14.
 */
@Getter
@Setter
public class AclAccountRoleMappingVO extends AclAccountRoleMappingDO {

    // 账户名称
    private String accountName;
    // 状态,ON OFF.
    private String accountStatus;
    // 账户手机号
    private String phone;
    // 账户邮箱
    private String mail;
    // 大部门
    private String firstDepartment;
    // 小部门
    private String secondDepartment;
    // 职位
    private String position;

    // 角色分组
    private String roleGroup;
    // 角色名称
    private String roleName;

}
