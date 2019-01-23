package com.d.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

@Slf4j
@SuppressWarnings({"unchecked", "unused"})
public abstract class BaseServiceImpl<D extends BaseMapper<T>, T extends BaseEntity<T>> implements BaseService<T> {
    @Autowired
    protected D mapper;
    private Class<T> entityClass;
    private Field entityId;

    @Override
    public T get(Long id) {
        T t = newEntity();
        if (t != null) {
            t.setId(id);
        }
        return mapper.get(t);
    }

    @Cacheable(value = "cache", key = "#root.targetClass.name+#id")
    @Override
    public T getCache(Long id) {
        log.info("查询数据库【{}】", id);
        T t = newEntity();
        if (t != null) {
            t.setId(id);
        }
        return mapper.get(t);
    }

    public T get(T t) {
        List<T> list = list(t);
        if (list == null || list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new RuntimeException("期望1条，但是返回了" + list.size() + "条记录。");
        }
    }

    @Override
    public List<T> list(T entity) {
        return mapper.list(entity);
    }

    @Override
    public Integer count(T entity) {
        return mapper.count(entity);
    }

    @Override
    public int insert(T entity) {
        return mapper.insertSelective(entity);
    }

    @Override
    public int update(T entity) {
        return mapper.updateSelective(entity);
    }

    @Override
    public int save(T entity) {
        if (entity.isNewRecord()) {
            return mapper.insertSelective(entity);
        } else {
            return mapper.updateSelective(entity);
        }
    }

    private T newEntity() {
        try {
            return this.getEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("实例化失败", e);
        }
        return null;
    }

    private Class<T> getEntityClass() {
        if (this.entityClass == null) {
            this.entityClass = (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[1];
        }
        return entityClass;
    }

    private Field getEntityId() {
        if (entityId == null) {
            entityId = SqlProvider.id(getEntityClass());
        }
        return entityId;
    }
}
