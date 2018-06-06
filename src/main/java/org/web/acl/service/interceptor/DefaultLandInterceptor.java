package org.web.acl.service.interceptor;

import org.apache.log4j.Logger;
import org.web.acl.domain.SessionAccountDO;
import org.web.acl.helper.SessionAccountHelper;
import org.web.helper.HttpRequestHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luyl on 17-9-4.
 */
public class DefaultLandInterceptor extends LandInterceptor {

    private static Logger logger = Logger.getLogger(DefaultLandInterceptor.class);

    @Override
    public SessionAccountDO getSessionAccountFromRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        String value = HttpRequestHelper.getValueByParamOrCookie(httpServletRequest, "ACL_COOKIE_ACCOUNT");
        return SessionAccountHelper.getSessionAccountFromRequest(httpServletRequest);
    }

    @Override
    public String getRedirectUri(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getContextPath() + "/land/land.html";
    }


}
