/**
 * @Description AclAccountService is generated by the auto build Tools.
 * @author luyl
 * @time 2017-09-07 10:51:48
 */

package org.web.acl.service;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.web.acl.dao.AclAccountDAO;
import org.web.acl.domain.AclAccountDO;
import org.web.acl.query.QueryAclAccount;
import org.web.exception.ResultMessageEnum;
import org.web.exception.ServiceException;
import org.web.helper.EnumHelper;
import org.web.helper.ServiceExceptionHelper;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("aclAccountService")
public class AclAccountService {

    private static Logger logger = Logger.getLogger(AclAccountService.class);

    @Resource
    private AclAccountDAO aclAccountDAO;

    /**
     * @param aclAccountId
     * @return 返回唯一记录AclAccount.
     * @Decription 根据主键查询记录
     * @author luyl
     * @date 2017-09-07 10:51:48
     */
    public AclAccountDO selectAclAccountByAclAccountId(Long aclAccountId) throws ServiceException {
        if (aclAccountId == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        }
        return aclAccountDAO.selectAclAccountByAclAccountId(aclAccountId);
    }

    /**
     * @param queryAclAccount 封装了查询条件对象.
     * @return 返回一组记录.
     * @Decription 根据查询条件, 返回List.
     * @author luyl
     * @date 2017-09-07 10:51:48
     */
    public List<AclAccountDO> selectAclAccountList(QueryAclAccount queryAclAccount) throws ServiceException {

        if (queryAclAccount == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        }
        //TODO
        setDefaultQuery(queryAclAccount);
        return aclAccountDAO.selectAclAccountList(queryAclAccount);
    }

    public Map<Long, AclAccountDO> selectAclAccountMap(QueryAclAccount queryAclAccount) throws ServiceException {
        Map<Long, AclAccountDO> map = new HashMap<>();
        List<AclAccountDO> list = selectAclAccountList(queryAclAccount);
        for (AclAccountDO aclAccountDO : list) {
            map.put(aclAccountDO.getAclAccountId(), aclAccountDO);
        }
        return map;
    }

    /**
     * @param queryAclAccount 封装了查询条件对象.
     * @return 返回查询条件返回的记录总数.
     * @Decription 根据查询条件, 查询满足条件的记录数.
     * @author luyl
     * @date 2017-09-07 10:51:48
     */
    public Integer countAclAccountList(QueryAclAccount queryAclAccount) throws ServiceException {
        if (queryAclAccount == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        }
        setDefaultQuery(queryAclAccount);
        return aclAccountDAO.countAclAccountList(queryAclAccount);
    }


    /**
     * 默认不查询逻辑删除的数据
     */
    private void setDefaultQuery(QueryAclAccount queryAclAccount) {
        if (StringUtils.isEmpty(queryAclAccount.getIsDelete())) {
            queryAclAccount.setIsDelete(EnumHelper.DELETE.N.name());
        }
        if (StringUtils.isEmpty(queryAclAccount.getOrderByClause())) {
            //queryAclAccount.setOrderByClause("  updateDate DESC ");
        }
    }

    /**
     * @param queryAclAccount 封装了查询条件对象.
     * @return 返回第一条记录.
     * @Decription 根据查询条件, 返回第一条记录.
     * @author luyl
     * @date 2017-09-07 10:51:48
     */
    public AclAccountDO selectOneAclAccount(QueryAclAccount queryAclAccount) throws ServiceException {
        queryAclAccount.setFirstRecord();
        List<AclAccountDO> list = aclAccountDAO.selectAclAccountList(queryAclAccount);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * @param queryAclAccount 封装了查询条件对象.
     * @return 返回不重复信息.
     * @Decription 根据查询条件, 查询不重复信息.
     * @author luyl
     * @date 2017-09-07 10:51:48
     */
    public List<String> selectDistinctList(QueryAclAccount queryAclAccount) throws ServiceException {
        if (StringUtils.isEmpty(queryAclAccount.getIsDelete())) {
            queryAclAccount.setIsDelete(EnumHelper.DELETE.N.name());
        }
        return aclAccountDAO.selectDistinctList(queryAclAccount);
    }


    /**
     * @param aclAccountDO 封装新增的对象.
     * @return 返回原始对象，如果用到数据库自增主键，并会自动设置新增主键.
     * @Decription 插入一条新记录.
     * @author luyl
     * @date 2017-09-07 10:51:48
     */
    public AclAccountDO insertAclAccount(AclAccountDO aclAccountDO) throws ServiceException {
        // check params first.

        setDefaultValue(aclAccountDO);
        checkInsert(aclAccountDO);

        checkExist(aclAccountDO);

        aclAccountDAO.insertAclAccount(aclAccountDO);
        return aclAccountDO;
    }


    /**
     * @param aclAccountDO 封装新增的对象.
     * @return 返回原始对象，如果用到数据库自增主键，并会自动设置新增主键.
     * @Decription 插入一条新记录.
     * @author luyl
     * @date 2017-09-07 10:51:48
     */
    public AclAccountDO saveAndUpdateAclAccount(AclAccountDO aclAccountDO) throws ServiceException {
        // check params first.

        setDefaultValue(aclAccountDO);
        checkInsert(aclAccountDO);

        QueryAclAccount queryAclAccount = new QueryAclAccount();
        queryAclAccount.setAccountNum(aclAccountDO.getAccountNum());
        Integer count = countAclAccountList(queryAclAccount);
        if (count > 0) {
            AclAccountDO fromDb = selectOneAclAccount(queryAclAccount);
            aclAccountDO.setAclAccountId(fromDb.getAclAccountId());
            BeanUtils.copyProperties(aclAccountDO, fromDb);
            updateAclAccountByAclAccountId(fromDb);
            return fromDb;
        } else {
            aclAccountDAO.insertAclAccount(aclAccountDO);
            return aclAccountDO;
        }
    }

    private void setDefaultValue(AclAccountDO aclAccountDO) {
        Date current = new Date();
        aclAccountDO.setCreateTime(current);
        aclAccountDO.setUpdateTime(current);
        aclAccountDO.setAccountStatus("ON");
        aclAccountDO.setIsDelete(EnumHelper.DELETE.N.name());
    }

    private void checkExist(AclAccountDO aclAccountDO) throws ServiceException {
        QueryAclAccount queryAclAccount = new QueryAclAccount();
        if (aclAccountDO.getAccountNum() != null) {
            queryAclAccount.setBusinessLine(aclAccountDO.getBusinessLine());
            queryAclAccount.setAccountNum(aclAccountDO.getAccountNum());
            Integer count = countAclAccountList(queryAclAccount);
            if (count > 0) {
                throw ServiceExceptionHelper.buildServiceException(ResultMessageEnum.RECORD_EXIST, aclAccountDO.getAccountNum().toString(), "员工号已存在");
            }
        }
    }

    /**
     * @param aclAccountDO 封装修改的对象.
     * @return 返回修改记录数.
     * @Decription 根据主键修改记录.
     * @author luyl
     * @date 2017-09-07 10:51:48
     */
    public int updateAclAccountByAclAccountId(AclAccountDO aclAccountDO) throws ServiceException {
        // check params first.
        checkUpdate(aclAccountDO);
        int update = aclAccountDAO.updateAclAccountByAclAccountId(aclAccountDO);
        return update;

    }

    /**
     * According to DB info. check attribute allow empty or not, and check attribute's length is over upper limit of length or not.
     * and this method is generate by the auto build tool.
     */
    @SuppressWarnings({"deprecation"})
    private void checkInsert(AclAccountDO aclAccountDO) throws ServiceException {
        if (aclAccountDO == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        } else if (StringUtils.isEmpty(aclAccountDO.getAccountName())
                || (StringUtils.isNotEmpty(aclAccountDO.getAccountName()) && aclAccountDO.getAccountName().length() > 25)) {
            throw new ServiceException("PARAM_IS_INVALID", "AccountName is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isEmpty(aclAccountDO.getAccountNum())
                || (StringUtils.isNotEmpty(aclAccountDO.getAccountNum()) && aclAccountDO.getAccountNum().length() > 25)) {
            throw new ServiceException("PARAM_IS_INVALID", "AccountNum is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isEmpty(aclAccountDO.getAccountStatus())
                || (StringUtils.isNotEmpty(aclAccountDO.getAccountStatus()) && aclAccountDO.getAccountStatus().length() > 25)) {
            throw new ServiceException("PARAM_IS_INVALID", "AccountStatus is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isEmpty(aclAccountDO.getBusinessLine())
                || (StringUtils.isNotEmpty(aclAccountDO.getBusinessLine()) && aclAccountDO.getBusinessLine().length() > 25)) {
            throw new ServiceException("PARAM_IS_INVALID", "BusinessLine is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getPhone()) && aclAccountDO.getPhone().length() > 25) {
            throw new ServiceException("PARAM_IS_INVALID", "Phone is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getFirstDepartment()) && aclAccountDO.getFirstDepartment().length() > 25) {
            throw new ServiceException("PARAM_IS_INVALID", "FirstDepartment is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getSecondDepartment()) && aclAccountDO.getSecondDepartment().length() > 25) {
            throw new ServiceException("PARAM_IS_INVALID", "SecondDepartment is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getPosition()) && aclAccountDO.getPosition().length() > 25) {
            throw new ServiceException("PARAM_IS_INVALID", "Position is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getInputer()) && aclAccountDO.getInputer().length() > 25) {
            throw new ServiceException("PARAM_IS_INVALID", "Inputer is null or out of range, Upper limit of length is 25");
        } else if (StringUtils.isEmpty(aclAccountDO.getIsDelete())
                || (StringUtils.isNotEmpty(aclAccountDO.getIsDelete()) && aclAccountDO.getIsDelete().length() > 25)) {
            throw new ServiceException("PARAM_IS_INVALID", "IsDelete is null or out of range, Upper limit of length is 25");
        }

    }

    @SuppressWarnings({"deprecation"})
    private void checkUpdate(AclAccountDO aclAccountDO) throws ServiceException {
        if (aclAccountDO == null) {
            throw new ServiceException("PARAM_IS_EMPTY", "Query is null.");
        } else if (aclAccountDO.getAclAccountId() != null && String.valueOf(aclAccountDO.getAclAccountId()).length() > 11) {
            throw new ServiceException("UPDATE_ERROR", "AclAccountId is out of range, Upper limit of length is 11");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getAccountName()) && aclAccountDO.getAccountName().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "AccountName is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getAccountNum()) && aclAccountDO.getAccountNum().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "AccountNum is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getAccountStatus()) && aclAccountDO.getAccountStatus().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "AccountStatus is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getBusinessLine()) && aclAccountDO.getBusinessLine().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "BusinessLine is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getPhone()) && aclAccountDO.getPhone().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "Phone is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getFirstDepartment()) && aclAccountDO.getFirstDepartment().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "FirstDepartment is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getSecondDepartment()) && aclAccountDO.getSecondDepartment().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "SecondDepartment is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getPosition()) && aclAccountDO.getPosition().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "Position is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getInputer()) && aclAccountDO.getInputer().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "Inputer is out of range, Upper limit of length is 25");
        } else if (StringUtils.isNotEmpty(aclAccountDO.getIsDelete()) && aclAccountDO.getIsDelete().length() > 25) {
            throw new ServiceException("UPDATE_ERROR", "IsDelete is out of range, Upper limit of length is 25");
        }
    }
}


