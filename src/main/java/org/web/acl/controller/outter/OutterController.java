package org.web.acl.controller.outter;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.web.acl.domain.AclAccountDO;
import org.web.acl.domain.AclResourceAccountMappingDO;
import org.web.acl.domain.AclResourceDO;
import org.web.acl.domain.SessionAccountDO;
import org.web.acl.helper.ACLOutterConstant;
import org.web.acl.helper.AclHelper;
import org.web.acl.helper.SessionAccountHelper;
import org.web.acl.query.QueryAclAccount;
import org.web.acl.query.QueryAclResource;
import org.web.acl.query.QueryAclResourceAccountMapping;
import org.web.acl.service.AclResourceAccountMappingService;
import org.web.acl.service.AclResourceService;
import org.web.domain.ViewResult;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("outter")
public class OutterController implements ACLOutterConstant {

    @Resource
    private AclResourceService aclResourceService;

    @Resource
    private AclResourceAccountMappingService aclResourceAccountMappingService;

    @RequestMapping(value = "index", method = {RequestMethod.GET, RequestMethod.POST})
    public String land(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String businessLine = request.getParameter(BUSINESS_LINE);
        if (StringUtils.isEmpty(businessLine)) {
            response.sendRedirect("/acl/error/error.html?msg=" + "业务线不能为空！！！");
            return null;
        }
        request.setAttribute("resourceKey",request.getParameter(OUTTER_BUSINESS_LINE));
        request.setAttribute(BUSINESS_LINE,businessLine);
        return "outter/index";
    }

    @RequestMapping(value = "selectRoleList", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String selectRoleList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ViewResult<List<AclResourceDO>> result = new ViewResult();
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

        String businessLine = request.getParameter(BUSINESS_LINE);
        SessionAccountDO sessionAccountDO = SessionAccountHelper.getSessionAccountFromRequest(request);
        QueryAclResource queryAclResource  = AclHelper.buildBeanByRequest(request,QueryAclResource.class);
        queryAclResource.setBusinessLine(businessLine);
        queryAclResource.setResourceName(FIXED_ROLE);
        Map<Long, AclResourceDO> aclResourceDOMap = aclResourceService.selectAclResourceMap(queryAclResource);
        List<Long> aclResourceIdList = new ArrayList<>();
        if(aclResourceDOMap.size()==0){
            return gson.toJson(buildZeroResult(result));
        }

        Iterator<Long> it = aclResourceDOMap.keySet().iterator();
        while (it.hasNext()) {
            aclResourceIdList.add(it.next());
        }

        QueryAclResourceAccountMapping queryAclResourceAccountMapping = new QueryAclResourceAccountMapping();
        queryAclResourceAccountMapping.setAclResourceIdList(aclResourceIdList);
        queryAclResourceAccountMapping.setAccountNum(sessionAccountDO.getAccountNum());
        int count = aclResourceAccountMappingService.countAclResourceAccountMappingList(queryAclResourceAccountMapping);
        if(count == 0){
            return gson.toJson(buildZeroResult(result));
        }
        List<AclResourceAccountMappingDO> aclResourceAccountMappingDOList = aclResourceAccountMappingService.selectAclResourceAccountMappingList(queryAclResourceAccountMapping);
        List<AclResourceDO> aclResourceDOList = new ArrayList<>();
        for(AclResourceAccountMappingDO aclResourceAccountMappingDO:aclResourceAccountMappingDOList){
            aclResourceDOList.add(aclResourceDOMap.get(aclResourceAccountMappingDO.getAclResourceId()));
        }

        result.setData(aclResourceDOList,true);
        result.setTotal(count);
        result.setRows(aclResourceDOList);
        result.setMsg("查询成功");
        result.setResult(true);
        result.setType("info");
        return gson.toJson(result);
    }

    private ViewResult buildZeroResult(ViewResult<List<AclResourceDO>> result ){
        result.setTotal(0);
        result.setData(new ArrayList());
        result.setMsg("查询成功");
        result.setType("info");
        return result;
    }

}

