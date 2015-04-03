/*
 * Copyright (c) 2015, struggle.2036@163.com All Rights Reserved. 
 *
 * @project asura 
 * @file BaseIbatisDaoContext 
 * @package com.asura.framework.dao.ibatis 
 *
 * @date 2015/4/3 11:28 
 */
package com.asura.framework.dao.ibatis;

import com.asura.framework.base.entity.BaseEntity;
import com.asura.framework.base.paging.PagingResult;
import com.asura.framework.base.paging.SearchCondition;
import com.asura.framework.base.paging.SearchModel;
import com.ibatis.sqlmap.client.SqlMapExecutor;
import org.springframework.orm.ibatis.SqlMapClientCallback;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> 提供SqlMapClient的方法调用 </P>
 *
 * <PRE>
 * <BR>	修改记录
 * <BR>-----------------------------------------------
 * <BR>	修改日期			修改人			修改内容
 * </PRE>
 *
 * @author SZL
 * @version 1.0
 * @since 1.0
 */
public class BaseIbatisDaoContext extends BaseIbatisDaoSupport implements IBaseDao {

    protected String SQLMAP_NAMESPCE = "NULL_NS";
    private final String SPLIT_NS = ".";

    /**
     * 查询指定SQL语句的所有记录
     *
     * @param sqlId
     *         SQL语句ID
     *
     * @return 查询到的实体集合
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> findAll(final String sqlId) {
        executeRouter(sqlId, null);
        return getReadSqlMapClientTemplate().queryForList(sqlId);
    }

    /**
     * 查询指定SQL语句的所有记录
     *
     * @param sqlId
     *         SQL语句ID
     * @param searchModel
     *         查询模型
     *
     * @return 查询到的实体集合
     */
    @Override
    public <T extends BaseEntity> List<T> findAll(final String sqlId, final SearchModel searchModel) {
        return findAll(sqlId, getConditionMap(searchModel));

    }

    /**
     * 查询指定SQL语句的所有记录
     *
     * @param sqlId
     *         SQL语句ID
     * @param params
     *         条件参数
     *
     * @return 查询到的结果集合
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> findAll(final String sqlId, final Map<String, Object> params) {

        return getReadSqlMapClientTemplate().queryForList(sqlId, executeRouter(sqlId, params));
    }

    /**
     * 查询指定SQL语句的所有记录
     *
     * @param sqlId
     *         SQL语句ID
     * @param clazz
     *         返回值类型
     * @param params
     *         条件参数
     *
     * @return 查询到的结果集合
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(final String sqlId, final Class<T> clazz, final Map<String, Object> params) {

        return getReadSqlMapClientTemplate().queryForList(sqlId, executeRouter(sqlId, params));
    }

    /**
     * 查询指定SQL语句的所有记录
     *
     * @param sqlId
     *         SQL语句ID
     * @param clazz
     *         返回值类型
     * @param param
     *         条件参数
     *
     * @return 查询到的结果集合
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> List<T> findAll(final String sqlId, final Class<T> clazz, final int param) {

        return getReadSqlMapClientTemplate().queryForList(sqlId, executeRouter(sqlId, param));
    }

    /**
     * 分页查询指定SQL语句的数据
     *
     * @param countSqlId
     *         总数查询SQL语句ID
     * @param sqlId
     *         SQL语句ID
     * @param searchModel
     *         查询模型
     * @param clazz
     *         查询结果类型
     *
     * @return 分页查询结果PagingResult
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> PagingResult<T> findForPage(final String countSqlId, final String sqlId, final SearchModel searchModel, final Class<T> clazz) {
        final Map<String, Object> params = getConditionMap(searchModel);
        final List<T> list = getReadSqlMapClientTemplate().queryForList(sqlId, executeRouter(sqlId, params), searchModel.getFirstRowIndex(), searchModel.getPageSize());

        return new PagingResult<T>(count(countSqlId, searchModel), list);
    }

    /**
     * 分页查询指定SQL语句的数据（ID）
     *
     * @param countSqlId
     *         总数查询SQL语句ID
     * @param sqlId
     *         SQL语句ID
     * @param searchModel
     *         查询模型
     *
     * @return 分页查询结果List<Integer>
     */
    @Override
    @SuppressWarnings("unchecked")
    public PagingResult<Integer> findForPage(final String countSqlId, final String sqlId, final SearchModel searchModel) {
        final Map<String, Object> params = getConditionMap(searchModel);

        final List<Integer> list = getReadSqlMapClientTemplate().queryForList(sqlId, executeRouter(sqlId, params), searchModel.getFirstRowIndex(), searchModel.getPageSize());
        return new PagingResult<Integer>(count(countSqlId, searchModel), list);
    }

    /**
     * 查询指定SQL语句的一条记录
     *
     * @param sqlId
     *         SQL语句ID
     *
     * @return 查询到的实体
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> T findOne(final String sqlId) {
        executeRouter(sqlId, null);
        return (T) getReadSqlMapClientTemplate().queryForObject(sqlId);
    }

    /**
     * 根据条件查询指定SQL语句的一条记录
     *
     * @param sqlId
     *         SQL语句ID
     * @param clazz
     *         返回值类型
     * @param params
     *         条件参数
     *
     * @return 查询到的结果
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findOne(final String sqlId, final Class<T> clazz, final Map<String, Object> params) {
        return (T) getReadSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, params));
    }

    /**
     * 根据条件查询指定SQL语句的一条记录，主要用于关联查询的情况
     *
     * @param sqlId
     *         SQL语句ID
     * @param clazz
     *         返回值类型
     * @param param
     *         条件参数
     *
     * @return 查询到的结果
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T findOne(final String sqlId, final Class<T> clazz, final int param) {

        return (T) getReadSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, param));
    }

    /**
     * 查询指定SQL语句的一条记录
     *
     * @param sqlId
     *         SQL语句ID
     * @param searchModel
     *         SQL语句中占位符对应的值
     *
     * @return 查询到的实体
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends BaseEntity> T findOne(final String sqlId, final SearchModel searchModel) {
        final Map<String, Object> params = getConditionMap(searchModel);
        return (T) getReadSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, params));
    }

    /**
     * 查询指定SQL语句所包含的记录数
     *
     * @param sqlId
     *         SQL语句ID
     *
     * @return 记录数
     */
    @Override
    public long count(final String sqlId) {

        executeRouter(sqlId, null);
        final Object obj = getReadSqlMapClientTemplate().queryForObject(sqlId);
        if (obj instanceof Long) {
            logger.debug(sqlId + " count result long");
            return (Long) obj;
        } else if (obj instanceof Integer) {
            logger.debug(sqlId + " count result integer");
            return Long.parseLong(((Integer) obj).toString());
        } else {
            logger.debug(sqlId + " count result neither long nor integer");
            return (Long) obj;
        }

    }

    /**
     * 查询指定SQL语句所包含的记录数
     *
     * @param sqlId
     *         SQL语句ID
     * @param params
     *         参数
     *
     * @return 符合条件的记录数
     */
    @Override
    public long count(final String sqlId, final Map<String, Object> params) {

        return (Long) getReadSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, params));
    }

    /**
     * 查询指定SQL语句所包含的记录数
     *
     * @param sqlId
     *         SQL语句ID
     * @param searchModel
     *         查询模型
     *
     * @return 符合条件的记录数
     */
    @Override
    public long count(final String sqlId, final SearchModel searchModel) {
        final Map<String, Object> params = getConditionMap(searchModel);
        return (Long) getReadSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, params));
    }

    /**
     * 插入指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     *
     * @return 插入对象的主键
     */
    @Override
    public Object save(final String sqlId) {
        executeRouter(sqlId, null);
        return getWriteSqlMapClientTemplate().insert(sqlId);
    }

    /**
     * 插入指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     * @param entity
     *         要插入的实体
     *
     * @return 插入对象的主键
     */
    @Override
    public Object save(final String sqlId, final BaseEntity entity) {
        return getWriteSqlMapClientTemplate().insert(sqlId, executeRouter(sqlId, entity));
    }

    /**
     * 更新指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     *
     * @return 成功更新的记录数
     */
    @Override
    public int update(final String sqlId) {
        executeRouter(sqlId, null);
        return getWriteSqlMapClientTemplate().update(sqlId);
    }

    /**
     * 更新指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     * @param entity
     *         要更新的对象
     *
     * @return 成功更新的记录数
     */
    @Override
    public int update(final String sqlId, final BaseEntity entity) {

        return getWriteSqlMapClientTemplate().update(sqlId, executeRouter(sqlId, entity));
    }

    /**
     * 更新指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     * @param params
     *         参数
     *
     * @return 成功更新的记录数
     */
    @Override
    public int update(final String sqlId, final Map<String, Object> params) {
        return getWriteSqlMapClientTemplate().update(sqlId, executeRouter(sqlId, params));
    }

    /**
     * 删除指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     *
     * @return 成功删除的记录数
     */
    @Override
    public int delete(final String sqlId) {
        executeRouter(sqlId, null);
        return getWriteSqlMapClientTemplate().delete(sqlId);
    }

    /**
     * 删除指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     * @param params
     *         查询参数
     *
     * @return 成功删除记录数
     */
    @Override
    public int delete(final String sqlId, final Map<String, Object> params) {
        return getWriteSqlMapClientTemplate().delete(sqlId, executeRouter(sqlId, params));
    }

    /**
     * 批量删除指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     * @param params
     *         删除数据的参数集合；NOT NULL
     *
     * @return 成功更新的记录数
     */
    @Override
    public int[] batchDelete(final String sqlId, final List<BaseEntity> params) {
        // 执行回调
        final SqlMapClientCallback<Object> callback = new SqlMapClientCallback<Object>() {
            // 实现回调接口
            @Override
            public Object doInSqlMapClient(final SqlMapExecutor executor) throws SQLException {
                // 开始批处理
                executor.startBatch();
                final int[] rtnDel = new int[params.size()];
                for (int i = 0; i < params.size(); i++) {
                    rtnDel[i] = executor.delete(sqlId, executeRouter(sqlId, params.get(i)));
                }
                // 执行批处理
                executor.executeBatch();
                return rtnDel;
            }
        };

        return (int[]) getWriteSqlMapClientTemplate().execute(callback);
    }

    /**
     * 批量更新指定SQL的数据
     *
     * @param sqlId
     *         SQL语句ID
     * @param params
     *         SQL语句中占位符对应的值
     *
     * @return 成功更新的记录数
     */
    @Override
    public int[] batchUpdate(final String sqlId, final List<? extends BaseEntity> params) {
        // 执行回调
        final SqlMapClientCallback<Object> callback = new SqlMapClientCallback<Object>() {
            // 实现回调接口
            @Override
            public Object doInSqlMapClient(final SqlMapExecutor executor) throws SQLException {
                // 开始批处理
                executor.startBatch();
                final int[] rtnUpd = new int[params.size()];
                for (int i = 0; i < params.size(); i++) {
                    rtnUpd[i] = executor.update(sqlId, executeRouter(sqlId, params.get(i)));
                }
                // 执行批处理
                executor.executeBatch();
                return rtnUpd;
            }
        };

        return (int[]) getWriteSqlMapClientTemplate().execute(callback);
    }

    /**
     * 以Map形式得到所有查询条件，
     * key为参数名称对应SearchCondition的name，value为参数值对应SearchCondition的value
     *
     * @param searchModel
     *         查询模型
     *
     * @return Map形式的参数信息
     */
    public Map<String, Object> getConditionMap(final SearchModel searchModel) {
        final Map<String, Object> conditionMap = new HashMap<String, Object>();
        if (searchModel.getSearchConditionList() != null) {
            for (final SearchCondition condition : searchModel.getSearchConditionList()) {
                conditionMap.put(condition.getName(), condition.getValue());
            }
        }
        return conditionMap;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> T findOneByM(final String sqlId) {
        executeRouter(sqlId, null);
        return (T) getWriteSqlMapClientTemplate().queryForObject(sqlId);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findOneByM(final String sqlId, final Class<T> clazz, final Map<String, Object> params) {
        return (T) getWriteSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, params));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> T findOneByM(final String sqlId, final SearchModel searchModel) {
        final Map<String, Object> params = getConditionMap(searchModel);
        return (T) getWriteSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, params));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T findOneByM(final String sqlId, final Class<T> clazz, final int param) {
        return (T) getWriteSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, param));
    }

    @Override
    public long countByM(final String sqlId) {
        executeRouter(sqlId, null);
        return (Long) getWriteSqlMapClientTemplate().queryForObject(sqlId);
    }

    @Override
    public long countByM(final String sqlId, final Map<String, Object> params) {
        return (Long) getWriteSqlMapClientTemplate().queryForObject(sqlId, executeRouter(sqlId, params));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> findAllByM(final String sqlId, final Class<T> clazz, final Map<String, Object> params) {
        return getWriteSqlMapClientTemplate().queryForList(sqlId, executeRouter(sqlId, params));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends BaseEntity> List<T> findAllByM(final String sqlId) {
        executeRouter(sqlId, null);
        return getWriteSqlMapClientTemplate().queryForList(sqlId);
    }

    /**
     * @return the sQLMAP_NAMESPCE
     */
    public String getSQLMAP_NAMESPCE() {
        return SQLMAP_NAMESPCE;
    }

    /**
     * @param sQLMAP_NAMESPCE
     *         the sQLMAP_NAMESPCE to set
     */
    public void setSQLMAP_NAMESPCE(final String sQLMAP_NAMESPCE) {
        SQLMAP_NAMESPCE = sQLMAP_NAMESPCE;
    }

    protected String convertSqlId(final String sqlid) {
        return SQLMAP_NAMESPCE + SPLIT_NS + sqlid.trim();
    }
}
