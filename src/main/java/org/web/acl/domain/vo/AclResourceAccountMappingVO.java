package org.web.acl.domain.vo;

import lombok.Getter;
import lombok.Setter;
import org.web.acl.domain.AclResourceAccountMappingDO;

/**
 * Created by luyl on 17-9-14.
 */
@Setter
@Getter
public class AclResourceAccountMappingVO extends AclResourceAccountMappingDO {

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

    // 资源名称，与resourceKey，resourceValue唯一确定一条记录
    private String resourceName;
    // 资源以KEY，MAP的方式来描述，资源KEY。
    private String resourceKey;
    // 资源以KEY，VALUE来描述，资源VALUE
    private String resourceValue;
    // 资源等级，用树状结构描述资源
    private String resourceLevel;
    // 拓展value
    private String extendsValue;
    // 描述
    private String description;
    // 资源状态
    private String resourceStatus;
    // 资源类型，扩展属性，非必入例项
    private String resourceType;
    // 第一级Level
    private String firstLevel;
    // 第二级Level
    private String secondLevel;
    // 第三级Level
    private String thirdLevel;
    // 第四级Level
    private String fourthLevel;
    // 第五级Level
    private String fifthLevel;
}
