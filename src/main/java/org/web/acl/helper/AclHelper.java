package org.web.acl.helper;

import org.apache.log4j.Logger;
import org.web.exception.ResultMessageEnum;
import org.web.exception.ServiceException;
import org.web.helper.HttpRequestHelper;
import org.web.helper.HttpServletRequestHelper;
import org.web.helper.ServiceExceptionHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by luyl on 17-9-7.
 */
public class AclHelper {

    private static final Logger logger = Logger.getLogger(AclHelper.class);

    public static <T> T buildBeanByRequest(HttpServletRequest request, Class<T> clazz) throws ServiceException {
        logger.info("URI is " + request.getRequestURI());
        Map<String, Object> map = HttpServletRequestHelper.buildMapByRequest(request);
        map.put("inputer", SessionAccountHelper.getSessionAccountVaue(request));
        map.put("businessLine", HttpRequestHelper.getValueByCookie(request, ACLConstant.ACL_COOKIE_BUSINESSLINE));
        return HttpServletRequestHelper.buildBeanByMap(map, clazz);
    }

    public static <T> T transObject(Object obj, Class<T> clazz) throws ServiceException {
        return (T) obj;
    }

    public static Object getRequestAttribute(HttpServletRequest request, String key, String errorMessage) {
        Object obj = request.getAttribute(key);
        if (obj == null) {
            throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_NOT_EXIST, errorMessage != null ? errorMessage : "查询记录不存在");
        }
        return obj;
    }
}
