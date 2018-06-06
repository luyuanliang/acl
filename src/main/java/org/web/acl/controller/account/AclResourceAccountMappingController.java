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
import org.web.acl.domain.AclResourceAccountMappingDO;
import org.web.acl.domain.AclResourceDO;
import org.web.acl.domain.AclResourceRoleMappingDO;
import org.web.acl.domain.vo.AclResourceAccountMappingVO;
import org.web.acl.helper.ACLConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.query.QueryAclAccount;
import org.web.acl.query.QueryAclResource;
import org.web.acl.query.QueryAclResourceAccountMapping;
import org.web.acl.service.AclAccountService;
import org.web.acl.service.AclResourceAccountMappingService;
import org.web.acl.service.AclResourceService;
import org.web.domain.ViewResult;
import org.web.helper.EnumHelper;
import org.web.helper.HttpRequestHelper;
import org.web.helper.HttpServletRequestHelper;
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
@RequestMapping("/account/aclResourceAccountMapping/")
@SuppressWarnings({"rawtypes", "unchecked"})
public class AclResourceAccountMappingController implements ACLConstant{

    @Resource
    private AclResourceAccountMappingService aclResourceAccountMappingService;

    @Resource
    private AclResourceService aclResourceService;

    @Resource
    private AclAccountService aclAccountService;

    @RequestMapping(value = "aclResourceAccountMapping", method = {RequestMethod.GET, RequestMethod.POST})
    public String AclResourceAccountMapping(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return "account/aclResourceAccountMapping";
    }


    @RequestMapping(value = "selectAclResourceAccountMappingList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectAclResourceAccountMappingList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<List<AclResourceAccountMappingVO>> result = new ViewResult(true);
        List<AclResourceAccountMappingVO> viewList = new ArrayList();
        result.setData(viewList);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        try {
            QueryAclResource queryAclResource = AclHelper.buildBeanByRequest(request, QueryAclResource.class);
            Integer pageSize = queryAclResource.getPageSize();
            Integer pageNum = queryAclResource.getPageNum();
            queryAclResource.setAllRecords();
            Map<Long, AclResourceDO> aclResourceMap = aclResourceService.selectAclResourceMap(queryAclResource);
            if (aclResourceMap.keySet().size() == 0) {
                return gson.toJson(result);
            }

            QueryAclAccount queryAclAccount = AclHelper.buildBeanByRequest(request, QueryAclAccount.class);
            queryAclAccount.setAllRecords();
            Map<Long, AclAccountDO> aclAccountMap = aclAccountService.selectAclAccountMap(queryAclAccount);
            if (aclAccountMap.keySet().size() == 0) {
                return gson.toJson(result);
            }

            List<Long> aclResourceIdList = new ArrayList<>();
            List<Long> aclAccountIdList = new ArrayList<>();

            for (Long key : aclResourceMap.keySet()) {
                aclResourceIdList.add(aclResourceMap.get(key).getAclResourceId());
            }

            for (Long key : aclAccountMap.keySet()) {
                aclAccountIdList.add(aclAccountMap.get(key).getAclAccountId());
            }

            QueryAclResourceAccountMapping queryAclResourceAccountMapping = new QueryAclResourceAccountMapping();
            queryAclResourceAccountMapping.setAclResourceIdList(aclResourceIdList);
            queryAclResourceAccountMapping.setAclAccountIdList(aclAccountIdList);
            queryAclResourceAccountMapping.setPageNum(pageNum);
            queryAclResourceAccountMapping.setPageSize(pageSize);

            List<AclResourceAccountMappingDO> list = aclResourceAccountMappingService.selectAclResourceAccountMappingList(queryAclResourceAccountMapping);
            int count = aclResourceAccountMappingService.countAclResourceAccountMappingList(queryAclResourceAccountMapping);
            result.setTotal(count);
            for (AclResourceAccountMappingDO aclResourceAccountMappingDO : list) {
                AclResourceAccountMappingVO view = new AclResourceAccountMappingVO();
                AclResourceDO aclResourceDO = aclResourceMap.get(aclResourceAccountMappingDO.getAclResourceId());
                AclAccountDO aclAccountDO = aclAccountMap.get(aclResourceAccountMappingDO.getAclAccountId());
                BeanUtils.copyProperties(aclResourceDO, view);
                BeanUtils.copyProperties(aclAccountDO, view);
                BeanUtils.copyProperties(aclResourceAccountMappingDO, view);
                viewList.add(view);
            }
            result.setRows(viewList);
        } catch (Exception e) {
            e.printStackTrace();
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }
        return gson.toJson(result);
    }

    @RequestMapping(value = "deleteAclResourceAccountMapping", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String deleteAclResourceAccountMapping(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<Integer> result = new ViewResult(true);
        Gson gson = new Gson();
        try {
            List<AclResourceAccountMappingDO> list = (List<AclResourceAccountMappingDO>) AclHelper.getRequestAttribute(request, RESOURCE_ACCOUNT_LIST_PAGE, "");
            for (AclResourceAccountMappingDO aclResourceAccountMappingDO : list) {
                AclResourceAccountMappingDO update = new AclResourceAccountMappingDO();
                update.setAclResourceAccountMappingId(aclResourceAccountMappingDO.getAclResourceAccountMappingId());
                update.setIsDelete(EnumHelper.DELETE.Y.name());
                aclResourceAccountMappingService.updateAclResourceAccountMappingByAclResourceAccountMappingId(update);
            }
            result.setTotal(list.size());
        } catch (Exception e) {
            return gson.toJson(ServiceExceptionHelper.buildViewResultByServiceException(e));
        }

        return gson.toJson(result);

    }
}
