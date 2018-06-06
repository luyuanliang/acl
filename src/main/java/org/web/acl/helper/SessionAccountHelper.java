package org.web.acl.helper;

import com.google.gson.Gson;
import org.apache.commons.lang.StringUtils;
import org.web.acl.domain.SessionAccountDO;
import org.web.helper.HttpRequestHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by luyl on 17-9-6.
 */
public class SessionAccountHelper implements  ACLConstant{

    public static SessionAccountDO getSessionAccountFromRequest(HttpServletRequest httpServletRequest) {
        String value = HttpRequestHelper.getValueByParamOrCookie(httpServletRequest, ACL_COOKIE_ACCOUNT);
        if (StringUtils.isNotBlank(value)) {
            SessionAccountDO sessionAccountDO = new SessionAccountDO();
            String[] array = value.split("@");
            sessionAccountDO.setAccountNum(array[0]);
            sessionAccountDO.setAccountName(array[1]);
            return sessionAccountDO;
            //return new Gson().fromJson(value, SessionAccountDO.class);
        }
        return null;
    }

    public static String getSessionAccountVaue(HttpServletRequest httpServletRequest) {
        SessionAccountDO sessionAccountDO = getSessionAccountFromRequest(httpServletRequest);
        if (sessionAccountDO != null) {
            return sessionAccountDO.getAccountNum() + "/" + sessionAccountDO.getAccountName();
        }
        return null;
    }
}
