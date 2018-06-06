package org.web.acl.domain.vo;

import lombok.Getter;
import lombok.Setter;
import org.web.acl.domain.AclResourceRoleMappingDO;

import java.util.Date;

/**
 * Created by luyl on 17-9-14.
 */
@Setter
@Getter
public class AclResourceRoleMappingVO extends AclResourceRoleMappingDO {

    // 角色分组
    private String roleGroup;
    // 角色名称
    private String roleName;

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
