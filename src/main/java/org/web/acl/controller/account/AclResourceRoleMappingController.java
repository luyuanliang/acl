package org.web.acl.controller.account;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.acl.domain.AclResourceDO;
import org.web.acl.domain.AclResourceRoleMappingDO;
import org.web.acl.domain.AclRoleDO;
import org.web.acl.domain.vo.AclResourceRoleMappingVO;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.query.QueryAclResource;
import org.web.acl.query.QueryAclResourceRoleMapping;
import org.web.acl.query.QueryAclRole;
import org.web.acl.service.AclResourceRoleMappingService;
import org.web.acl.service.AclResourceService;
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
@RequestMapping("/account/aclResourceRoleMapping/")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AclResourceRoleMappingController implements ACLConstant {

    @Resource
    private AclResourceRoleMappingService aclResourceRoleMappingService;

    @Resource
    private AclResourceService aclResourceService;

    @Resource
    private AclRoleService aclRoleService;

    @RequestMapping(value = "aclResourceRoleMapping", method = {RequestMethod.GET, RequestMethod.POST})
    public String aclResourceRoleMapping(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return "account/aclResourceRoleMapping";
    }

    @RequestMapping(value = "selectAclResourceRoleMappingList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclResourceRoleMappingList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<List<AclResourceRoleMappingVO>> result = new ViewResult(true);
        List<AclResourceRoleMappingVO> viewList = new ArrayList();
        result.setData(viewList);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            QueryAclResource queryAclResource = AclHelper.buildBeanByRequest(request, QueryAclResource.class);
            Integer pageSize = queryAclResource.getPageSize();
            Integer pageNum = queryAclResource.getPageNum();
            queryAclResource.setAllResoced();
            Map<Long, AclResourceDO> aclResourceMap = aclResourceService.selectAclResourceMap(queryAclResource);
            if (aclResourceMap.keySet().size() == 0) {
                return gson.toJson(result);
            }

            QueryAclRole queryAclRole = AclHelper.buildBeanByRequest(request, QueryAclRole.class);
            queryAclRole.setAllResoced();
            Map<Long, AclRoleDO> aclRoleMap = aclRoleService.selectAclRoleMap(queryAclRole);
            if (aclRoleMap.keySet().size() == 0) {
                return gson.toJson(result);
            }

            List<Long> aclResourceIdList = new ArrayList<>();
            List<Long> aclRoleIdList = new ArrayList<>();

            for (Long key : aclResourceMap.keySet()) {
                aclResourceIdList.add(aclResourceMap.get(key).getAclResourceId());
            }

            for (Long key : aclRoleMap.keySet()) {
                aclRoleIdList.add(aclRoleMap.get(key).getAclRoleId());
            }

            QueryAclResourceRoleMapping queryAclResourceRoleMapping = new QueryAclResourceRoleMapping();
            queryAclResourceRoleMapping.setAclResourceIdList(aclResourceIdList);
            queryAclResourceRoleMapping.setAclRoleIdList(aclRoleIdList);
            queryAclResourceRoleMapping.setPageNum(pageNum);
            queryAclResourceRoleMapping.setPageSize(pageSize);

            List<AclResourceRoleMappingDO> list = aclResourceRoleMappingService.selectAclResourceRoleMappingList(queryAclResourceRoleMapping);
            int count = aclResourceRoleMappingService.countAclResourceRoleMappingList(queryAclResourceRoleMapping);
            result.setTotal(count);
            for (AclResourceRoleMappingDO aclResourceRoleMappingDO : list) {
                AclResourceRoleMappingVO view = new AclResourceRoleMappingVO();
                BeanUtils.copyProperties(aclResourceMap.get(aclResourceRoleMappingDO.getAclResourceId()), view);
                BeanUtils.copyProperties(aclRoleMap.get(aclResourceRoleMappingDO.getAclRoleId()), view);
                BeanUtils.copyProperties(aclResourceRoleMappingDO, view);
                viewList.add(view);

            }
            result.setRows(viewList);
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "deleteAclResourceRoleMapping", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String deleteAclResourceRoleMapping(HttpServletRequest request, HttpServletResponse response) throws IOException {

        ViewResult<Integer> result = new ViewResult(true);
        Gson gson = new Gson();
        try {
            List<AclResourceRoleMappingDO> list = (List<AclResourceRoleMappingDO>) AclHelper.getRequestAttribute(request, RESOURCE_ROLE_LIST_PAGE, "");
            for (AclResourceRoleMappingDO aclResourceRoleMappingDO : list) {
                AclResourceRoleMappingDO update = new AclResourceRoleMappingDO();
                update.setAclResourceRoleMappingId(aclResourceRoleMappingDO.getAclResourceRoleMappingId());
                update.setIsDelete(EnumHelper.DELETE.Y.name());
                aclResourceRoleMappingService.updateAclResourceRoleMappingByAclResourceRoleMappingId(update);
            }
            result.setTotal(list.size());
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);
    }
}
