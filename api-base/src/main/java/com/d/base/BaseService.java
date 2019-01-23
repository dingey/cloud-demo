package com.d.base;

import java.util.List;

@SuppressWarnings("unused")
public interface BaseService<T extends BaseEntity<T>> {

    /**
     * 根据主键查询
     *
     * @param id 主键
     * @return 一条记录
     */
    T get(Long id);

    /**
     * 根据主键查询并缓存结果
     *
     * @param id 主键
     * @return 一条记录
     */
    T getCache(Long id);

    /**
     * 查询一条记录,和entity字段值不为空相等的一条记录，大于1条报错
     *
     * @param entity 查询对象
     * @return 一条记录
     */
    T get(T entity);

    /**
     * 查询和entity字段值不为空相等的多条记录
     *
     * @param entity 查询对象
     * @return 多条记录
     */
    List<T> list(T entity);

    /**
     * 汇总和entity字段值不为空相等的记录数
     *
     * @param entity 查询对象
     * @return 记录数
     */
    Integer count(T entity);

    /**
     * 插入记录
     *
     * @param entity 实体
     * @return 影响行数
     */
    int insert(T entity);

    /**
     * 修改记录
     *
     * @param entity 实体
     * @return 影响行数
     */
    int update(T entity);

    /**
     * 保存/修改记录
     *
     * @param entity 实体
     * @return 影响行数
     */
    int save(T entity);
}
