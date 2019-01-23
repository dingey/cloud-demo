package com.d.base;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.io.Serializable;
import java.util.List;

/**
 * @author di
 */
@SuppressWarnings("unused")
public interface BaseMapper<T> {
    /**
     * 插入，主键自动赋值
     *
     * @param t 参数对象
     * @return 影响行数
     */
    @InsertProvider(type = SqlProvider.class, method = "insert")
    @Options(useGeneratedKeys = true)
    int insert(T t);

    /**
     * 可选择插入，主键自动赋值
     *
     * @param t 参数对象
     * @return 影响行数
     */
    @InsertProvider(type = SqlProvider.class, method = "insertSelective")
    @Options(useGeneratedKeys = true)
    int insertSelective(T t);

    /**
     * 更新
     *
     * @param t 参数对象
     * @return 影响行数
     */
    @UpdateProvider(type = SqlProvider.class, method = "update")
    int update(T t);

    /**
     * 可选择更新
     *
     * @param t 参数对象
     * @return 影响行数
     */
    @UpdateProvider(type = SqlProvider.class, method = "updateSelective")
    int updateSelective(T t);

    /**
     * 根据主键查询
     *
     * @param t 参数对象
     * @return 单个记录
     */
    @SelectProvider(type = SqlProvider.class, method = "get")
    T get(T t);

    /**
     * 根据主键查询
     *
     * @param t  参数类型
     * @param id 主键
     * @return 单个记录
     */
    @SelectProvider(type = SqlProvider.class, method = "getById")
    T getById(Class<T> t, Serializable id);

    /**
     * 查询
     *
     * @param t 参数对象
     * @return 列表
     */
    @SelectProvider(type = SqlProvider.class, method = "list")
    List<T> list(T t);

    /**
     * 汇总
     *
     * @param t 参数对象
     * @return 记录数
     */
    @SelectProvider(type = SqlProvider.class, method = "count")
    Integer count(T t);
}
