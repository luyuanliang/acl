package org.web.acl.controller.account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.acl.domain.AclAccountDO;
import org.web.acl.domain.AclAccountRoleMappingDO;
import org.web.acl.domain.AclRoleDO;
import org.web.acl.domain.vo.AclAccountRoleMappingVO;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.query.QueryAclAccount;
import org.web.acl.query.QueryAclAccountRoleMapping;
import org.web.acl.query.QueryAclRole;
import org.web.acl.service.AclAccountRoleMappingService;
import org.web.acl.service.AclAccountService;
import org.web.acl.service.AclRoleService;
import org.web.domain.ViewResult;
import org.web.helper.EnumHelper;
import org.web.helper.HttpRequestHelper;
import org.web.helper.ServiceExceptionHelper;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by luyl on 17-9-14.
 */
@Scope("prototype")
@Controller
@RequestMapping("account/aclAccountRoleMapping")
public class AclAccountRoleMappingController implements ACLConstant {

    @Resource
    private AclAccountRoleMappingService aclAccountRoleMappingService;

    @Resource
    private AclAccountService aclAccountService;

    @Resource
    private AclRoleService aclRoleService;

    @RequestMapping(value = "aclAccountRoleMapping", method = {RequestMethod.GET, RequestMethod.POST})
    public String aclAccountRoleMapping(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "account/aclAccountRoleMapping";
    }

    @RequestMapping(value = "selectAclAccountRoleMappingList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclAccountRoleMappingList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<List<AclAccountRoleMappingVO>> result = new ViewResult(true);
        List<AclAccountRoleMappingVO> viewList = new ArrayList();
        result.setData(viewList);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            QueryAclRole queryAclRole = AclHelper.buildBeanByRequest(request, QueryAclRole.class);
            Integer pageSize = queryAclRole.getPageSize();
            Integer pageNum = queryAclRole.getPageNum();
            queryAclRole.setAllResoced();
            Map<Long, AclRoleDO> aclRoleMap = aclRoleService.selectAclRoleMap(queryAclRole);
            if (aclRoleMap.keySet().size() == 0) {
                return gson.toJson(result);
            }

            QueryAclAccount queryAclAccount = AclHelper.buildBeanByRequest(request, QueryAclAccount.class);
            queryAclAccount.setAllResoced();
            Map<Long, AclAccountDO> aclAccountMap = aclAccountService.selectAclAccountMap(queryAclAccount);
            if (aclAccountMap.keySet().size() == 0) {
                return gson.toJson(result);
            }

            List<Long> aclRoleIdList = new ArrayList<>();
            List<Long> aclAccountIdList = new ArrayList<>();

            for (Long key : aclRoleMap.keySet()) {
                aclRoleIdList.add(aclRoleMap.get(key).getAclRoleId());
            }

            for (Long key : aclAccountMap.keySet()) {
                aclAccountIdList.add(aclAccountMap.get(key).getAclAccountId());
            }

            QueryAclAccountRoleMapping queryAclAccountRoleMapping = new QueryAclAccountRoleMapping();
            queryAclAccountRoleMapping.setAclRoleIdList(aclRoleIdList);
            queryAclAccountRoleMapping.setAclAccountIdList(aclAccountIdList);
            queryAclAccountRoleMapping.setPageNum(pageNum);
            queryAclAccountRoleMapping.setPageSize(pageSize);
            List<AclAccountRoleMappingDO> list = aclAccountRoleMappingService.selectAclAccountRoleMappingList(queryAclAccountRoleMapping);
            int count = aclAccountRoleMappingService.countAclAccountRoleMappingList(queryAclAccountRoleMapping);
            result.setTotal(count);
            for (AclAccountRoleMappingDO aclAccountRoleMappingDO : list) {
                AclAccountRoleMappingVO view = new AclAccountRoleMappingVO();
                AclRoleDO aclRoleDO = aclRoleMap.get(aclAccountRoleMappingDO.getAclRoleId());
                AclAccountDO aclAccountDO = aclAccountMap.get(aclAccountRoleMappingDO.getAclAccountId());
                BeanUtils.copyProperties(aclRoleDO, view);
                BeanUtils.copyProperties(aclAccountDO, view);
                BeanUtils.copyProperties(aclAccountRoleMappingDO, view);
                viewList.add(view);

            }
            result.setRows(viewList);
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }


    @RequestMapping(value = "deleteAclAccountRoleMapping", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String deleteAclAccountRoleMapping(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<Integer> result = new ViewResult(true);
        Gson gson = new Gson();
        try {
            List<AclAccountRoleMappingDO> list = (List<AclAccountRoleMappingDO>) AclHelper.getRequestAttribute(request, ACCOUNT_ROLE_LIST_PAGE, "");
            for (AclAccountRoleMappingDO aclAccountRoleMappingDO : list) {
                AclAccountRoleMappingDO update = new AclAccountRoleMappingDO();
                update.setAclAccountRoleMappingId(aclAccountRoleMappingDO.getAclAccountRoleMappingId());
                update.setIsDelete(EnumHelper.DELETE.Y.name());
                aclAccountRoleMappingService.updateAclAccountRoleMappingByAclAccountRoleMappingId(update);
            }
            result.setTotal(list.size());
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);
    }


}
