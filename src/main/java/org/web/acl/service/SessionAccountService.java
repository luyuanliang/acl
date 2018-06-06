package org.web.acl.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.web.acl.dao.SessionAccountDAO;
import org.web.acl.domain.SessionAccountDO;
import org.web.acl.query.QuerySessionAccount;
import org.web.exception.ResultMessageEnum;
import org.web.exception.ServiceException;
import org.web.helper.EnumHelper;
import org.web.helper.ServiceExceptionHelper;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by luyl on 17-9-6.
 */
@Service("sessionAccountService")
public class SessionAccountService {
    @Resource
    private SessionAccountDAO sessionAccountDAO;

    /**
     * @param sessionAcountId
     * @return 返回唯一记录SessionAccount.
     * @Decription 根据主键查询记录
     * @author luyl
     * @date 2017-09-04 18:38:51
     */
    public SessionAccountDO selectSessionAccountBySessionAcountId(Long sessionAcountId) throws ServiceException {
        if (sessionAcountId == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        }
        return sessionAccountDAO.selectSessionAccountBySessionAcountId(sessionAcountId);
    }

    /**
     * @param querySessionAccount 封装了查询条件对象.
     * @return 返回一组记录.
     * @Decription 根据查询条件, 返回List.
     * @author luyl
     * @date 2017-09-04 18:38:51
     */
    public List<SessionAccountDO> selectSessionAccountList(QuerySessionAccount querySessionAccount) throws ServiceException {

        if (querySessionAccount == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        }
        //TODO
        setDefaultQuery(querySessionAccount);
        return sessionAccountDAO.selectSessionAccountList(querySessionAccount);
    }

    /**
     * @param querySessionAccount 封装了查询条件对象.
     * @return 返回查询条件返回的记录总数.
     * @Decription 根据查询条件, 查询满足条件的记录数.
     * @author luyl
     * @date 2017-09-04 18:38:51
     */
    public Integer countSessionAccountList(QuerySessionAccount querySessionAccount) throws ServiceException {
        if (querySessionAccount == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        }
        setDefaultQuery(querySessionAccount);
        return sessionAccountDAO.countSessionAccountList(querySessionAccount);
    }

    /**
     * 默认不查询逻辑删除的数据
     */
    private void setDefaultQuery(QuerySessionAccount querySessionAccount) {
        if (StringUtils.isEmpty(querySessionAccount.getIsDelete())) {
            querySessionAccount.setIsDelete("NO");
        }
        if (StringUtils.isEmpty(querySessionAccount.getOrderByClause())) {
            //querySessionAccount.setOrderByClause("  updateDate DESC ");
        }
    }

    /**
     * @param querySessionAccount 封装了查询条件对象.
     * @return 返回第一条记录.
     * @Decription 根据查询条件, 返回第一条记录.
     * @author luyl
     * @date 2017-09-04 18:38:51
     */
    public SessionAccountDO selectOneSessionAccount(QuerySessionAccount querySessionAccount) throws ServiceException {
        //querySessionAccount.set
        List<SessionAccountDO> list = sessionAccountDAO.selectSessionAccountList(querySessionAccount);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * @param querySessionAccount 封装了查询条件对象.
     * @return 返回不重复信息.
     * @Decription 根据查询条件, 查询不重复信息.
     * @author luyl
     * @date 2017-09-04 18:38:51
     */
    public List<String> selectDistinctList(QuerySessionAccount querySessionAccount) throws ServiceException {
        if (StringUtils.isEmpty(querySessionAccount.getIsDelete())) {
            querySessionAccount.setIsDelete("NO");
        }
        return sessionAccountDAO.selectDistinctList(querySessionAccount);
    }


    /**
     * @param sessionAccountDO 封装新增的对象.
     * @return 返回原始对象，如果用到数据库自增主键，并会自动设置新增主键.
     * @Decription 插入一条新记录.
     * @author luyl
     * @date 2017-09-04 18:38:51
     */
    public SessionAccountDO insertSessionAccount(SessionAccountDO sessionAccountDO) throws ServiceException {
        // check params first.

        sessionAccountDO.setIsDelete(EnumHelper.DELETE.N.name());
        QuerySessionAccount querySessionAccount = new QuerySessionAccount();
        querySessionAccount.setAccountNum(sessionAccountDO.getAccountNum());
        int count = countSessionAccountList(querySessionAccount);
        if (count > 0) {
            throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_EXIST, "账户已经存在");
        }

        checkInsert(sessionAccountDO);

        sessionAccountDAO.insertSessionAccount(sessionAccountDO);
        return sessionAccountDO;
    }

    /**
     * @param sessionAccountDO 封装修改的对象.
     * @return 返回修改记录数.
     * @Decription 根据主键修改记录.
     * @author luyl
     * @date 2017-09-04 18:38:51
     */
    public int updateSessionAccountBySessionAcountId(SessionAccountDO sessionAccountDO) throws ServiceException {
        // check params first.
        checkUpdate(sessionAccountDO);

        return sessionAccountDAO.updateSessionAccountBySessionAcountId(sessionAccountDO);

    }

    /**
     * According to DB info. check attribute allow empty or not, and check attribute's length is over upper limit of length or not.
     * and this method is generate by the auto build tool.
     */
    @SuppressWarnings({"deprecation"})
    private void checkInsert(SessionAccountDO sessionAccountDO) throws ServiceException {
        if (sessionAccountDO == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        } else if (StringUtils.isEmpty(sessionAccountDO.getAccountName())
                || (StringUtils.isNotEmpty(sessionAccountDO.getAccountName()) && sessionAccountDO.getAccountName().length() > 25)) {
            throw new ServiceException("PARAM_IS_INVALID", "AccountName is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(sessionAccountDO.getAccountNum()) && sessionAccountDO.getAccountNum().length() > 25) {
            throw new ServiceException("PARAM_IS_INVALID", "AccountNum is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isEmpty(sessionAccountDO.getPassword())
                || (StringUtils.isNotEmpty(sessionAccountDO.getPassword()) && sessionAccountDO.getPassword().length() > 25)) {
            throw new ServiceException("PARAM_IS_INVALID", "Password is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isEmpty(sessionAccountDO.getIsDelete())
                || (StringUtils.isNotEmpty(sessionAccountDO.getIsDelete()) && sessionAccountDO.getIsDelete().length() > 25)) {
            throw new ServiceException("PARAM_IS_INVALID", "IsDelete is null or out of range, Upper limit of length is 25");
        }

    }

    @SuppressWarnings({"deprecation"})
    private void checkUpdate(SessionAccountDO sessionAccountDO) throws ServiceException {
        if (sessionAccountDO == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        } else if (sessionAccountDO.getSessionAcountId() != null && String.valueOf(sessionAccountDO.getSessionAcountId()).length() > 11) {
            throw new ServiceException("UPDATE_ERROR", "SessionAcountId is out of range, Upper limit of length is 11");
        } else if (StringUtils.isNotEmpty(sessionAccountDO.getAccountName()) && sessionAccountDO.getAccountName().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "AccountName is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(sessionAccountDO.getAccountNum()) && sessionAccountDO.getAccountNum().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "AccountNum is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(sessionAccountDO.getPassword()) && sessionAccountDO.getPassword().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "Password is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(sessionAccountDO.getIsDelete()) && sessionAccountDO.getIsDelete().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "IsDelete is out of range, Upper limit of length is 25");
        }
    }
}
