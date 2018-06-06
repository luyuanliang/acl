package org.web.acl.service.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.web.acl.domain.SessionAccountDO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by luyl on 17-8-23.
 */
public abstract class LandInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String uri = httpServletRequest.getRequestURI();
        if (uri.startsWith("/acl/land")||uri.startsWith("/error")) {
            return true;
        }
        SessionAccountDO accountSessionDO = getSessionAccountFromRequest(httpServletRequest, httpServletResponse);
        if (accountSessionDO == null) {
            String redirect = getRedirectUri(httpServletRequest);
            httpServletResponse.sendRedirect(redirect);
            return false;
        }
        return true;
    }

    /**
     * 获取登陆账户信息
     */
    public abstract SessionAccountDO getSessionAccountFromRequest(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    /**
     * 返回重定向URI
     */
    public abstract String getRedirectUri(HttpServletRequest httpServletRequest);

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
