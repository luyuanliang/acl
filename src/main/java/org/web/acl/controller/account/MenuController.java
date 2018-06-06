package org.web.acl.controller.account;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.acl.domain.*;
import org.web.acl.domain.menu.MenuNode;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.helper.SessionAccountHelper;
import org.web.acl.query.*;
import org.web.acl.service.*;
import org.web.domain.ViewResult;
import org.web.exception.ResultMessageEnum;
import org.web.helper.EnumHelper;
import org.web.helper.HttpRequestHelper;
import org.web.helper.ServiceExceptionHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by luyl on 17-11-15.
 */
@Scope("prototype")
@Controller
@RequestMapping("account/menu/")
public class MenuController implements ACLConstant {

    private static final Logger logger = Logger.getLogger(MenuController.class);

    @Resource
    private AclResourceService aclResourceService;

    @Resource
    private AclRoleService aclRoleService;

    @Resource
    private AclAccountService aclAccountService;

    @Resource
    private AclAccountRoleMappingService aclAccountRoleMappingService;

    @Resource
    private AclResourceRoleMappingService aclResourceRoleMappingService;

    @RequestMapping(value = "menuMain", method = {RequestMethod.GET, RequestMethod.POST})
    public String menuMain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/account/menu/menuMain";
    }

    @RequestMapping(value = "menuSetting", method = {RequestMethod.GET, RequestMethod.POST})
    public String menuSetting(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/account/menu/menuSetting";
    }


    @RequestMapping(value = "testTree", method = {RequestMethod.GET, RequestMethod.POST})
    public String testTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/account/menu/tree";
    }

    @RequestMapping(value = "tree", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String tree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String businessLine = HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE);
        QueryAclResource queryAclResource = new QueryAclResource();
        queryAclResource.setBusinessLine(businessLine);
        queryAclResource.setResourceName(ACL_RESOURCE_MENU);
        queryAclResource.setOrderByClause("  extendsValue asc  ");
        List<AclResourceDO> list = aclResourceService.selectAclResourceList(queryAclResource);

        List<MenuNode> menuNodes = Lists.newArrayList();

        List<Long> seleckedList = getSeleckedMenu(request);

        for (AclResourceDO aclResourceDO : list) {
            MenuNode m = new MenuNode(aclResourceDO.getAclResourceId());
            m.setText(aclResourceDO.getResourceKey());
            m.getAttributes().put("pathAddress", aclResourceDO.getResourceValue());
            m.getAttributes().put("description", aclResourceDO.getDescription());
            String token = aclResourceDO.getExtendsValue();
            if (token.endsWith(ACL_MENU_SEPERATE)) token = token.substring(0, token.length() - 1);
            m.setPath(token.split(ACL_MENU_SEPERATE));
            menuNodes.add(m);
        }
        MenuNode root = new MenuNode(-1);
        genTree(root, menuNodes);

        for (MenuNode menuNode:menuNodes){
            if (menuNode.getChildren().isEmpty()&&seleckedList.contains(Long.valueOf(menuNode.getId()))) {
                menuNode.setChecked(true);
            }
        }

        return new Gson().toJson(root.getChildren());
    }

    /**
     * 瞎写的。
     */
    private List<Long> getSeleckedMenu(HttpServletRequest request) {
        String businessLine = HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE);
        String accountName = request.getParameter("accountName");
        String accountNum = request.getParameter("accountNum");
        String roleName = request.getParameter("roleName");
        List<Long> list = new ArrayList<>();//菜单资源列表
        if (StringUtils.isEmpty(accountName) && StringUtils.isEmpty(accountNum) && StringUtils.isEmpty(roleName)) {
            // 不需要查詢任何信息。
            return list;
        }

        AclAccountDO aclAccountDO = null;
        AclRoleDO aclRoleDO = null;
        if (StringUtils.isNotBlank(accountName) || StringUtils.isNotBlank(accountNum)) {
            QueryAclAccount queryAclAccount = new QueryAclAccount();
            queryAclAccount.setBusinessLine(businessLine);
            if(StringUtils.isNotBlank(accountName) ){
                queryAclAccount.setAccountName(accountName);
            }
            if(StringUtils.isNotBlank(accountNum) ){
                queryAclAccount.setAccountNum(accountNum);
            }
            aclAccountDO = aclAccountService.selectOneAclAccount(queryAclAccount);
            if (aclAccountDO == null) {
                // 用户没有取到的场合返回空列表。
                return list;
            }
        }

        if (StringUtils.isNotBlank(roleName)) {
            QueryAclRole queryAclRole = new QueryAclRole();
            queryAclRole.setBusinessLine(businessLine);
            queryAclRole.setRoleName(roleName);
            queryAclRole.setRoleGroup(ACL_RESOURCE_MENU);
            aclRoleDO = aclRoleService.selectOneAclRole(queryAclRole);
            if (aclRoleDO == null) {
                // 角色没有取到，则反馈空列表
                return list;
            }
        }

        // 只有三种场合：都不为空，有一个为空。
        if (aclRoleDO != null && aclAccountDO != null) {
            // 需要查询账户权限。
            QueryAclAccountRoleMapping queryAclAccountRoleMapping = new QueryAclAccountRoleMapping();
            queryAclAccountRoleMapping.setBusinessLine(businessLine);
            queryAclAccountRoleMapping.setAclAccountId(aclAccountDO.getAclAccountId());
            queryAclAccountRoleMapping.setAccountNum(aclAccountDO.getAccountNum());
            queryAclAccountRoleMapping.setAclRoleId(aclRoleDO.getAclRoleId());
            int count = aclAccountRoleMappingService.countAclAccountRoleMappingList(queryAclAccountRoleMapping);
            if (count > 0) {
                return list;
            } else {
                return getList(aclRoleDO.getAclRoleId(), businessLine);
            }
        } else if (aclRoleDO != null) {
            return getList(aclRoleDO.getAclRoleId(), businessLine);
        } else if (aclAccountDO != null) {
            // 查询用户菜单角色列表。
            QueryAclRole queryAclRole = new QueryAclRole();
            queryAclRole.setBusinessLine(businessLine);
            queryAclRole.setRoleGroup(ACL_RESOURCE_MENU);
            List<AclRoleDO> queryRoleList = aclRoleService.selectAclRoleList(queryAclRole);
            if (queryRoleList.size() == 0) {
                return list;
            }

            List<Long> aclRoleIdList = new ArrayList<>();
            for (AclRoleDO aclRoleDOFromList : queryRoleList) {
                aclRoleIdList.add(aclRoleDOFromList.getAclRoleId());
            }

            QueryAclAccountRoleMapping queryAclAccountRoleMapping = new QueryAclAccountRoleMapping();
            queryAclAccountRoleMapping.setAclAccountId(aclAccountDO.getAclAccountId());
            queryAclAccountRoleMapping.setAccountNum(aclAccountDO.getAccountNum());
            queryAclAccountRoleMapping.setBusinessLine(businessLine);
            queryAclAccountRoleMapping.setAclRoleIdList(aclRoleIdList);
            List<AclAccountRoleMappingDO> queryAclAccountRoleMappingDOList = aclAccountRoleMappingService.selectAclAccountRoleMappingList(queryAclAccountRoleMapping);

            aclRoleIdList.clear();
            for (AclAccountRoleMappingDO aclAccountRoleMappingDO : queryAclAccountRoleMappingDOList) {
                aclRoleIdList.add(aclAccountRoleMappingDO.getAclRoleId());
            }
            QueryAclResourceRoleMapping queryAclResourceRoleMapping = new QueryAclResourceRoleMapping();
            queryAclResourceRoleMapping.setAclRoleIdList(aclRoleIdList);
            List<AclResourceRoleMappingDO> queryList = aclResourceRoleMappingService.selectAclResourceRoleMappingList(queryAclResourceRoleMapping);
            for (AclResourceRoleMappingDO aclResourceRoleMappingDO : queryList) {
                list.add(aclResourceRoleMappingDO.getAclResourceId());
            }
            return list;
        }

        return list;
    }

    private List<Long> getList(Long aclRoleId, String businessLine) {
        List<Long> list = new ArrayList<>();
        QueryAclResourceRoleMapping queryAclResourceRoleMapping = new QueryAclResourceRoleMapping();
        queryAclResourceRoleMapping.setAclRoleId(aclRoleId);
        queryAclResourceRoleMapping.setBusinessLine(businessLine);
        //角色对应菜单资源列表
        List<AclResourceRoleMappingDO> queryList = aclResourceRoleMappingService.selectAclResourceRoleMappingList(queryAclResourceRoleMapping);
        for (AclResourceRoleMappingDO aclResourceRoleMappingDO : queryList) {
            list.add(aclResourceRoleMappingDO.getAclResourceId());
        }
        return list;
    }

    private void genTree(MenuNode root, List<MenuNode> menuNodes) {
//        Collections.sort(menuNodes, new Comparator<MenuNode>() {
//            @Override
//            public int compare(MenuNode o1, MenuNode o2) {
//                return (int) Math.signum(o1.getPath().length - o2.getPath().length);
//            }
//        });
        for (MenuNode menuNode : menuNodes) {
            MenuNode parent = root;
            if (menuNode.getPath().length > 1) {
                String[] path = menuNode.getPath();
                for (int i = 0; i < path.length - 1; i++)
                    parent = parent.getIdxChild(path[i]);
            }
            parent.getChildren().add(menuNode);
        }
    }


    @RequestMapping(value = "test", method = {RequestMethod.GET, RequestMethod.POST})
    public String test(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "/account/menu/test";
    }

    @RequestMapping(value = "menuTree", method = {RequestMethod.GET, RequestMethod.POST})
    public String menuTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String businessLine = HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE);
        QueryAclResource queryAclResource = new QueryAclResource();
        queryAclResource.setBusinessLine(businessLine);
        queryAclResource.setResourceName(ACL_RESOURCE_MENU);
        queryAclResource.setOrderByClause("  extendsValue asc ");
        List<AclResourceDO> list = aclResourceService.selectAclResourceList(queryAclResource);
        return "/account/menu/test";
    }


    @RequestMapping(value = "bindMenuRole", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String bindMenuRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ViewResult<AclResourceDO> result = new ViewResult(true);
        try {
            String roleName = request.getParameter("roleName");
            String businessLine = HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE);

            //查询角色信息，并校验产品线。
            QueryAclRole queryAclRole = new QueryAclRole();
            queryAclRole.setBusinessLine(businessLine);
            queryAclRole.setRoleName(roleName);
            queryAclRole.setRoleGroup(ACL_RESOURCE_MENU);
            AclRoleDO aclRoleDO = aclRoleService.selectOneAclRole(queryAclRole);
            if (aclRoleDO == null) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_NOT_EXIST, "角色信息没有取到！");
            } else if (!businessLine.equalsIgnoreCase(aclRoleDO.getBusinessLine())) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_NOT_ALLOW_UPDATE, "角色与当前产品线不匹配！");
            }


            /**
             * 老的绑定关系全部删除。
             * */
            QueryAclResourceRoleMapping queryAclResourceRoleMapping = new QueryAclResourceRoleMapping();
            queryAclResourceRoleMapping.setAclRoleId(aclRoleDO.getAclRoleId());
            queryAclResourceRoleMapping.setBusinessLine(businessLine);
            List<AclResourceRoleMappingDO> list = aclResourceRoleMappingService.selectAclResourceRoleMappingList(queryAclResourceRoleMapping);
            for (AclResourceRoleMappingDO aclResourceRoleMappingDO : list) {
                AclResourceRoleMappingDO deletAclResourceRoleMappingDO = new AclResourceRoleMappingDO();
                deletAclResourceRoleMappingDO.setAclResourceRoleMappingId(aclResourceRoleMappingDO.getAclResourceRoleMappingId());
                deletAclResourceRoleMappingDO.setIsDelete(EnumHelper.DELETE.Y.name());
                aclResourceRoleMappingService.updateAclResourceRoleMappingByAclResourceRoleMappingId(deletAclResourceRoleMappingDO);
            }

            /**
             * 新增新的绑定关系全部删除。
             * */
            String inputer = SessionAccountHelper.getSessionAccountVaue(request);
            QueryAclResource queryAclResource = new QueryAclResource();
            queryAclResource.setOrderByClause(" extendsValue asc ");
            List<AclResourceDO> aclResourceDOList = (List<AclResourceDO>) AclHelper.getRequestAttribute(request, RESOURCE_LIST_PAGE, "参数aclResourceList为空!!");
            for (AclResourceDO aclResourceDO : aclResourceDOList) {
                AclResourceRoleMappingDO aclResourceRoleMappingDO = new AclResourceRoleMappingDO();
                aclResourceRoleMappingDO.setAclResourceId(aclResourceDO.getAclResourceId());
                aclResourceRoleMappingDO.setAclRoleId(aclRoleDO.getAclRoleId());
                aclResourceRoleMappingDO.setBusinessLine(businessLine);
                aclResourceRoleMappingDO.setInputer(inputer);
                aclResourceRoleMappingService.insertAclResourceRoleMapping(aclResourceRoleMappingDO);
            }

        } catch (Exception e) {
            logger.error(ServiceExceptionHelper.getExceptionInfo(e));
            return new Gson().toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return new Gson().toJson(result);
    }


    @RequestMapping(value = "operateMenu", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String operateMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ViewResult<AclResourceDO> result = new ViewResult(true);
        try {
            //首先校验参数. 参数要预先校验，保障一定能更新到数据库。
            String operate = request.getParameter("operate");// beforeInsert,afterInsert,update,delete,append
            String aclResourceIdStr = request.getParameter(RESOURCE_ID_PAGE);
            String description = request.getParameter("description");
            String pathName = request.getParameter("pathName");
            String pathAddress = request.getParameter("pathAddress");
            String inputer = SessionAccountHelper.getSessionAccountVaue(request);
            checkParam(aclResourceIdStr, operate, pathName, pathAddress);


            // 然后查找节点信息。
            String businessLine = HttpRequestHelper.getValueByCookie(request, ACL_COOKIE_BUSINESSLINE);
            Long aclResourceId = Long.valueOf(aclResourceIdStr);
            QueryAclResource queryAclResource = getQueryAclResource(businessLine, aclResourceId);
            AclResourceDO selectAclResourceDO = (AclResourceDO) AclHelper.getRequestAttribute(request, RESOURCE_ID_PAGE, "菜单信息没有找到！");

            AclResourceDO executeAclResourceDO = new AclResourceDO();
            executeAclResourceDO.setInputer(inputer);
            executeAclResourceDO.setBusinessLine(businessLine);
            executeAclResourceDO.setDescription(description);
            executeAclResourceDO.setResourceName(ACL_RESOURCE_MENU);
            executeAclResourceDO.setResourceKey(pathName);
            executeAclResourceDO.setResourceValue(pathAddress);
            executeAclResourceDO.setResourceStatus("ON");
            if (!OPERATE.update.name().equalsIgnoreCase(operate)) {
                aclResourceService.checkExist(executeAclResourceDO);
            }

            boolean needExecuteOther = true;

            queryAclResource.setAclResourceId(null);
            queryAclResource.setExtendsValue(selectAclResourceDO.getExtendsValue() + "01" + ACL_MENU_SEPERATE);
            AclResourceDO subAclResourceDO = aclResourceService.selectOneAclResource(queryAclResource);
            if (OPERATE.append.name().equalsIgnoreCase(operate)) {
                if (subAclResourceDO == null) {
                    //说明没有子节点，直接插入,然后返回。
                    needExecuteOther = false;
                } else {
                    selectAclResourceDO = subAclResourceDO;
                    operate = OPERATE.beforeInsert.name();
                }
            } else if (OPERATE.delete.name().equalsIgnoreCase(operate) && subAclResourceDO != null) {
                /**
                 * 存在子节点的场合，不允许删除。
                 * */
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_NOT_ALLOW_UPDATE, "存在子节点，不允许删除！");
            }

            // 处理其他节点信息
            if (needExecuteOther && !OPERATE.update.name().equalsIgnoreCase(operate)) {
                executeOtherMenu(selectAclResourceDO, operate, inputer);
            }

            // 处理完其他节点后，操作要处理的节点。
            if (OPERATE.beforeInsert.name().equalsIgnoreCase(operate) ||
                    OPERATE.afterInsert.name().equalsIgnoreCase(operate) ||
                    OPERATE.append.name().equalsIgnoreCase(operate)) {

                if (OPERATE.beforeInsert.name().equalsIgnoreCase(operate)) {
                    executeAclResourceDO.setExtendsValue(selectAclResourceDO.getExtendsValue());
                } else if (OPERATE.afterInsert.name().equalsIgnoreCase(operate)) {
                    String[] array = selectAclResourceDO.getExtendsValue().split(",");
                    executeAclResourceDO.setExtendsValue(getExtendsValue(selectAclResourceDO.getExtendsValue(),
                            selectAclResourceDO.getExtendsValue().split(ACL_MENU_SEPERATE).length - 1, 1));
                } else if (OPERATE.append.name().equalsIgnoreCase(operate)) {
                    executeAclResourceDO.setExtendsValue(selectAclResourceDO.getExtendsValue() + "01" + ACL_MENU_SEPERATE);
                }
                //TODO
                executeAclResourceDO.setResourceLevel(String.valueOf(executeAclResourceDO.getExtendsValue().split(ACL_MENU_SEPERATE).length));
                proxyExecuteMenuForDB(executeAclResourceDO);

            } else if (OPERATE.delete.name().equalsIgnoreCase(operate)) {
                AclResourceDO updateAclResourceDO = new AclResourceDO();
                updateAclResourceDO.setAclResourceId(aclResourceId);
                updateAclResourceDO.setIsDelete(EnumHelper.DELETE.Y.name());
                proxyExecuteMenuForDB(updateAclResourceDO);
            } else if (OPERATE.update.name().equalsIgnoreCase(operate)) {
                executeAclResourceDO.setAclResourceId(selectAclResourceDO.getAclResourceId());
                proxyExecuteMenuForDB(executeAclResourceDO);
            }
            result.setData(executeAclResourceDO);
        } catch (Exception e) {
            logger.error(ServiceExceptionHelper.getExceptionInfo(e));
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }


    private void checkParam(String aclResourceIdStr, String operate, String pathName, String pathAddress) {
        if (StringUtils.isEmpty(aclResourceIdStr)) {
            throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.PARAM_EMPTY, "参数为空！");
        } else if (!EnumHelper.checkExist(OPERATE.class, operate)) {
            throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.PARAM_INVALID, "operate格式不合法！");
        }

        if (OPERATE.beforeInsert.name().equalsIgnoreCase(operate) || OPERATE.afterInsert.name().equalsIgnoreCase(operate) || OPERATE.append.name().equalsIgnoreCase(operate)) {
            if (StringUtils.isEmpty(pathName)) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.PARAM_EMPTY, "新增节点,pathName不能为空！");
            }
            if (StringUtils.isEmpty(pathAddress)) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.PARAM_EMPTY, "新增节点,pathAddress！");
            }
        }
    }


    private QueryAclResource getQueryAclResource(String businessLine, Long aclResourceId) {
        QueryAclResource queryAclResource = new QueryAclResource();
        queryAclResource.setBusinessLine(businessLine);
        queryAclResource.setResourceName(ACL_RESOURCE_MENU);
        queryAclResource.setAclResourceId(aclResourceId);
        return queryAclResource;
    }

    private void checkInputValue(HttpServletRequest request) {
        String operate = request.getParameter("operate");
        String aclResourceIdStr = request.getParameter("id");
        if (StringUtils.isEmpty(aclResourceIdStr)) {
            throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.PARAM_EMPTY, "参数为空！");
        } else if (!EnumHelper.checkExist(OPERATE.class, operate)) {
            throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.PARAM_INVALID, "operate格式不合法！");
        }
    }

    private void executeOtherMenu(AclResourceDO aclResourceDO, String operate, String inputer) {

        if (OPERATE.beforeInsert.name().equalsIgnoreCase(operate) ||
                OPERATE.afterInsert.name().equalsIgnoreCase(operate) ||
                OPERATE.delete.name().equalsIgnoreCase(operate)) {

            String extendsValue = aclResourceDO.getExtendsValue();
            Integer updateIndex = extendsValue.split(ACL_MENU_SEPERATE).length - 1;

            QueryAclResource queryAclResourceList = buildQueryAclResource(aclResourceDO, operate);
            List<AclResourceDO> list = aclResourceService.selectAclResourceList(queryAclResourceList);

            for (AclResourceDO aclResourceDOFromList : list) {
                AclResourceDO updateAclResourceDO = new AclResourceDO();
                updateAclResourceDO.setInputer(inputer);
                updateAclResourceDO.setAclResourceId(aclResourceDOFromList.getAclResourceId());
                //updateAclResourceDO.setDescription("auto update by system.");

                if (OPERATE.beforeInsert.name().equalsIgnoreCase(operate) || OPERATE.afterInsert.name().equalsIgnoreCase(operate)) {
                    updateAclResourceDO.setExtendsValue(getExtendsValue(aclResourceDOFromList.getExtendsValue(), updateIndex, 1));
                } else if (OPERATE.delete.name().equalsIgnoreCase(operate)) {
                    updateAclResourceDO.setExtendsValue(getExtendsValue(aclResourceDOFromList.getExtendsValue(), updateIndex, -1));
                } else {
                    throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.PARAM_INVALID, "operate格式不合法！");
                }
                proxyExecuteMenuForDB(updateAclResourceDO);
            }
        }
    }

    /**
     * 持久化数据库前。预处理，菜单level信息。
     */
    private void proxyExecuteMenuForDB(AclResourceDO aclResourceDO) {
        if (StringUtils.isNotBlank(aclResourceDO.getExtendsValue())) {
            String[] array = aclResourceDO.getExtendsValue().split(ACL_MENU_SEPERATE);
            for (int i = 0; i < array.length; i++) {
                if (i == 0) {
                    aclResourceDO.setFirstLevel(array[i]);
                } else if (i == 1) {
                    aclResourceDO.setSecondLevel(array[i]);
                } else if (i == 2) {
                    aclResourceDO.setThirdLevel(array[i]);
                } else if (i == 3) {
                    aclResourceDO.setFourthLevel(array[i]);
                } else if (i == 4) {
                    aclResourceDO.setFifthLevel(array[i]);
                }
            }
        }
        if (aclResourceDO.getAclResourceId() != null) {
            if (EnumHelper.DELETE.Y.name().equalsIgnoreCase(aclResourceDO.getIsDelete())) {
                //TODO
            }
            aclResourceService.updateAclResourceByAclResourceId(aclResourceDO);
        } else {
            aclResourceService.insertAclResource(aclResourceDO);
        }

    }


    /**
     * @param input:[01,01,] \ [01,]
     * @param updateIndex:   修改第几位
     * @param tag:+1,-1,0    三种可能
     *                       入参：{"01,10,", 1, -1} -》 01,09
     */
    public String getExtendsValue(String input, int updateIndex, int tag) {
        String[] array = input.split(ACL_MENU_SEPERATE);
        String value = "";
        for (int i = 0; i < array.length; i++) {
            String temp = "";
            if (updateIndex == i) {
                String indexValue = array[i];
                int afterAddedResult = 0;
                if (indexValue.startsWith("0")) {
                    afterAddedResult = Integer.valueOf(indexValue.substring(1));
                } else {
                    afterAddedResult = Integer.valueOf(indexValue.substring(0));
                }
                afterAddedResult = afterAddedResult + tag;
                if (afterAddedResult >= 10) {
                    // 大于等于10，不需要补零。
                    temp = String.valueOf(afterAddedResult);
                } else {
                    // 个位，第一位补零。
                    temp = String.valueOf("0" + afterAddedResult);
                }

            } else {
                temp = array[i];
            }

            if (StringUtils.isEmpty(value)) {
                value = temp + ACL_MENU_SEPERATE;
            } else {
                value = value + temp + ACL_MENU_SEPERATE;
            }
        }
        return value;
    }


    public static void main(String args[]) {
        MenuController test = new MenuController();
        System.out.println(test.getExtendsValue("01,10,", 1, -1));

    }

    private Integer getLastValue(String value) {
        String[] array = value.split(",");
        return Integer.valueOf(array[array.length - 1]);
    }

    private QueryAclResource buildQueryAclResource(AclResourceDO aclResourceDO, String operate) {
        QueryAclResource queryAclResource = new QueryAclResource();
        queryAclResource.setBusinessLine(aclResourceDO.getBusinessLine());
        queryAclResource.setResourceName(ACL_RESOURCE_MENU);

        String extendsValue = aclResourceDO.getExtendsValue();
        String[] array = extendsValue.split(",");
        int length = array.length;
        String beginValue = getExtendsValue(extendsValue, length - 1, +1);

        String parentValue = "";
        for (int i = 0; i < length - 1; i++) {
            String temp = array[i];
            if (StringUtils.isEmpty(parentValue)) {
                parentValue = temp + ACL_MENU_SEPERATE;
            } else {
                parentValue = parentValue + temp + ACL_MENU_SEPERATE;
            }
        }

        String endValue = null;
        if (StringUtils.isNotBlank(parentValue)) {
            endValue = getExtendsValue(parentValue, length - 2, +1);
        }

        if (OPERATE.beforeInsert.name().equalsIgnoreCase(operate)) {
            queryAclResource.setEqualAndMoreThanExtendsValue(extendsValue);
            queryAclResource.setLessThanExtendsValue(endValue);
        } else if (OPERATE.afterInsert.name().equalsIgnoreCase(operate)) {
            queryAclResource.setEqualAndMoreThanExtendsValue(beginValue);
            queryAclResource.setLessThanExtendsValue(endValue);
        } else if (OPERATE.delete.name().equalsIgnoreCase(operate)) {
            queryAclResource.setEqualAndMoreThanExtendsValue(beginValue);
            queryAclResource.setLessThanExtendsValue(endValue);
        }
        return queryAclResource;
    }


    public static enum OPERATE {
        beforeInsert, afterInsert, update, delete, append
    }
}

