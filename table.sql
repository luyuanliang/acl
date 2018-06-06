CREATE TABLE `AclAccount` (
  `aclAccountId` int(11) NOT NULL AUTO_INCREMENT COMMENT '账户表主键',
  `accountName` varchar(25) NOT NULL COMMENT '账户名称',
  `accountNum` varchar(25) NOT NULL COMMENT '账户工号',
  `accountStatus` varchar(25) NOT NULL COMMENT '状态,ON OFF.',
  `businessLine` varchar(25) NOT NULL COMMENT '业务线名称',
  `phone` varchar(25) DEFAULT NULL COMMENT '账户手机号',
  `mail` varchar(255) DEFAULT NULL COMMENT '账户邮箱',
  `firstDepartment` varchar(25) DEFAULT NULL COMMENT '大部门',
  `secondDepartment` varchar(25) DEFAULT NULL COMMENT '小部门',
  `position` varchar(25) DEFAULT NULL COMMENT '职位',
  `inputer` varchar(25) DEFAULT NULL COMMENT '处理人',
  `createTime` datetime NOT NULL COMMENT '记录创建时间',
  `updateTime` datetime NOT NULL COMMENT '记录修改时间',
  `isDelete` varchar(25) NOT NULL COMMENT '是否逻辑删除',
  PRIMARY KEY (`aclAccountId`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='ACL 账户表';


CREATE TABLE `AclAccountRoleMapping` (
  `aclAccountRoleMappingId` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',``
  `businessLine` varchar(25) NOT NULL COMMENT '业务线',
  `aclAccountId` int(11) NOT NULL COMMENT 'AclAccount表主键',
  `accountNum` varchar(25) NOT NULL COMMENT '账户ID,相当与aclAccountId',
  `aclRoleId` int(11) NOT NULL COMMENT '角色ID',
  `inputer` varchar(25) DEFAULT NULL COMMENT '记录操作者',
  `createTime` datetime NOT NULL COMMENT '记录创建时间',
  `updateTime` datetime NOT NULL COMMENT '记录修改时间',
  `isDelete` varchar(25) NOT NULL COMMENT '记录是否逻辑删除',
  PRIMARY KEY (`aclAccountRoleMappingId`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='用户与角色映射表';

CREATE TABLE `AclResource` (
  `aclResourceId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'AclResource表主键',
  `businessLine` varchar(25) NOT NULL COMMENT '业务线',
  `resourceName` varchar(255) NOT NULL COMMENT '资源名称，与resourceKey，resourceValue唯一确定一条记录',
  `resourceKey` varchar(255) DEFAULT NULL COMMENT '资源以KEY，MAP的方式来描述，资源KEY。',
  `resourceValue` varchar(255) DEFAULT NULL COMMENT '资源以KEY，VALUE来描述，资源VALUE',
  `resourceLevel` varchar(25) DEFAULT NULL COMMENT '资源等级，用树状结构描述资源',
  `extendsValue` varchar(255) DEFAULT NULL COMMENT '拓展value',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `resourceStatus` varchar(25) DEFAULT NULL COMMENT '资源状态，ON生效,OFF失效',
  `resourceType` varchar(255) DEFAULT NULL COMMENT '资源类型，扩展属性，非必入例项',
  `firstLevel` varchar(25) DEFAULT NULL COMMENT '第一级Level',
  `secondLevel` varchar(25) DEFAULT NULL COMMENT '第二级Level',
  `thirdLevel` varchar(25) DEFAULT NULL COMMENT '第三级Level',
  `fourthLevel` varchar(25) DEFAULT NULL COMMENT '第四级Level',
  `fifthLevel` varchar(25) DEFAULT NULL COMMENT '第五级Level',
  `inputer` varchar(25) NOT NULL COMMENT '记录操作者',
  `inputTime` datetime NOT NULL COMMENT '记录创建时间',
  `updateTime` datetime NOT NULL COMMENT '记录修改时间',
  `isDelete` varchar(25) NOT NULL COMMENT '记录是否逻辑删除',
  PRIMARY KEY (`aclResourceId`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='ACL资源管理表';

CREATE TABLE `AclResourceAccountMapping` (
  `aclResourceAccountMappingId` int(11) NOT NULL AUTO_INCREMENT COMMENT '表主键',
  `businessLine` varchar(25) NOT NULL COMMENT '业务线',
  `aclResourceId` int(11) NOT NULL COMMENT '资源表主键',
  `aclAccountId` int(11) NOT NULL COMMENT '账户表主键',
  `accountNum` varchar(25) NOT NULL COMMENT '账户号',
  `inputer` varchar(25) NOT NULL COMMENT '记录修改人',
  `createTime` datetime NOT NULL COMMENT '记录创建时间',
  `updateTime` datetime NOT NULL COMMENT '记录修改时间',
  `isDelete` varchar(25) NOT NULL COMMENT '记录是否逻辑删除',
  PRIMARY KEY (`aclResourceAccountMappingId`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='ACL资源用户映射表'

CREATE TABLE `AclResourceRoleMapping` (
  `aclResourceRoleMappingId` int(11) NOT NULL AUTO_INCREMENT COMMENT '表主键',
  `aclResourceId` int(11) NOT NULL COMMENT 'AclResource表主键',
  `aclRoleId` int(11) NOT NULL COMMENT 'AclRole表主键',
  `businessLine` varchar(25) NOT NULL COMMENT '业务线',
  `inputer` varchar(25) NOT NULL COMMENT '记录修改者',
  `createTime` datetime DEFAULT NULL COMMENT '记录创建时间',
  `updateTime` datetime NOT NULL COMMENT '记录修改时间',
  `isDelete` varchar(25) NOT NULL COMMENT '记录是否逻辑删除',
  PRIMARY KEY (`aclResourceRoleMappingId`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8 COMMENT='ACL资源与角色映射关系';


CREATE TABLE `AclRole` (
  `aclRoleId` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ACL表主键',
  `roleGroup` varchar(25) NOT NULL COMMENT '角色分组',
  `roleName` varchar(25) NOT NULL COMMENT '角色名称',
  `description` varchar(500) DEFAULT NULL COMMENT '角色描述',
  `businessLine` varchar(25) NOT NULL COMMENT '角色所属业务线',
  `inputer` varchar(25) NOT NULL COMMENT '记录修改者',
  `createTime` datetime NOT NULL COMMENT '记录创建时间',
  `updateTime` datetime NOT NULL COMMENT '记录修改时间',
  `isDelete` varchar(25) DEFAULT NULL COMMENT '记录是否逻辑删除',
  PRIMARY KEY (`aclRoleId`)
) ENGINE=InnoDB AUTO_INCREMENT=64 DEFAULT CHARSET=utf8 COMMENT='acl 角色';

CREATE TABLE `SessionAccount` (
  `sessionAcountId` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `accountName` varchar(25) NOT NULL COMMENT '登陆账户名称',
  `accountNum` varchar(25) DEFAULT NULL COMMENT '登陆账户ID',
  `password` varchar(25) NOT NULL COMMENT '密码',
  `isDelete` varchar(25) NOT NULL COMMENT '记录是否逻辑删除',
  PRIMARY KEY (`sessionAcountId`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='默认账户表'


