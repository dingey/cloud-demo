package com.d.base;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author d
 */
@SuppressWarnings("all")
public class SqlProvider {
    private static final HashMap<String, String> sqls = new HashMap<>();
    private static final HashMap<String, List<Field>> modelFieldsMap = new HashMap<>();
    private static final HashMap<Class<?>, Field> idFieldsMap = new HashMap<>();

    public String insert(Object bean) {
        return getCachedSql(bean, "insert", t -> getInsertSql(bean, false));
    }

    public String insertSelective(Object bean) {
        return getInsertSql(bean, true);
    }

    public String getInsertSql(Object bean, boolean selective) {
        StringBuilder sql = new StringBuilder();
        List<String> props = new ArrayList<>();
        List<String> columns = new ArrayList<>();
        sql.append("insert into ").append(table(bean)).append("(");
        try {
            for (Field field : getCachedModelFields(bean.getClass())) {
                if (selective) {
                    Object value = field.get(bean);
                    if (value == null) {
                        continue;
                    }
                }
                if (field.isAnnotationPresent(IgnoreInsert.class)) {
                    continue;
                } else if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(OrderBy.class)) {
                    continue;
                }
                columns.add(snakeCase(field.getName()));
                props.add("#{" + field.getName() + "}");
            }
        } catch (Exception e) {
            throw new RuntimeException(sql.toString(), e);
        }
        for (int i = 0; i < columns.size(); i++) {
            sql.append("`").append(columns.get(i)).append("`");
            if (i != columns.size() - 1)
                sql.append(",");
        }
        sql.append(")").append(" values(");
        for (int i = 0; i < props.size(); i++) {
            sql.append(props.get(i));
            if (i != props.size() - 1)
                sql.append(",");
        }
        sql.append(")");
        return sql.toString();
    }

    public String update(Object bean) {
        return getCachedSql(bean, "update", t -> getUpdateSql(bean, false));
    }

    public String updateSelective(Object bean) {
        return getUpdateSql(bean, true);
    }

    public String getUpdateSql(Object bean, boolean selective) {
        StringBuilder sql = new StringBuilder();
        sql.append("update ").append(table(bean)).append(" set ");
        List<String> ids = new ArrayList<>();
        String version = null;
        try {
            for (Field field : getCachedModelFields(bean.getClass())) {
                if (selective) {
                    Object value = field.get(bean);
                    if (value == null) {
                        continue;
                    }
                }
                if (field.isAnnotationPresent(Id.class)) {
                    ids.add(field.getName());
                    continue;
                } else if (field.isAnnotationPresent(Version.class)) {
                    version = field.getName();
                    continue;
                } else if (field.isAnnotationPresent(Transient.class) || field.isAnnotationPresent(IgnoreUpdate.class) || field.isAnnotationPresent(OrderBy.class)) {
                    continue;
                }
                sql.append("`").append(snakeCase(field.getName())).append("`=#{").append(field.getName()).append("},");
            }
        } catch (Exception e) {
            throw new RuntimeException(sql.toString(), e);
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(" where ");
        for (String id : ids) {
            sql.append(snakeCase(id)).append(" =#{")
                    .append(id).append("} and ");
        }
        if (version != null) {
            sql.append(" and ").append(snakeCase(version)).append("=#{").append(version).append("} and");
        }
        return sql.delete(sql.length() - 4, sql.length()).toString();
    }

    public String delete(Object bean) {
        return getCachedSql(bean, "delete", t -> {
            StringBuilder sql = new StringBuilder();
            sql.append("delete from ").append(table(bean)).append(" where ");
            List<String> ids = new ArrayList<>();
            try {
                for (Field field : getCachedModelFields(bean.getClass())) {
                    if (field.isAnnotationPresent(Id.class)) {
                        ids.add(field.getName());
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(sql.toString(), e);
            }
            if (ids.isEmpty()) {
                throw new RuntimeException();
            } else {
                for (String id : ids) {
                    sql.append(snakeCase(id)).append("=#{").append(id).append("} and ");
                }
            }
            return sql.delete(sql.length() - 5, sql.length()).toString();
        });
    }

    public String deleteMark(Object bean) {
        return getCachedSql(bean, "deleteMark", t -> {
            StringBuilder sql = new StringBuilder();
            sql.append("update ").append(table(bean)).append(" set ");
            String delete = null, version = null;
            List<String> ids = new ArrayList<>();
            try {
                for (Field field : getCachedModelFields(bean.getClass())) {
                    if (field.isAnnotationPresent(DeleteMark.class)) {
                        delete = snakeCase(field.getName());
                    } else if (field.isAnnotationPresent(Id.class)) {
                        ids.add(field.getName());
                    } else if (field.isAnnotationPresent(Version.class)) {
                        version = field.getName();
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(sql.toString(), e);
            }
            sql.append(delete).append("=1 where ");
            if (ids.isEmpty()) {
                throw new RuntimeException("主键必须声明");
            } else {
                for (String id : ids) {
                    sql.append(snakeCase(id)).append("=#{").append(id).append("} and ");
                }
                if (version != null) {
                    sql.append(snakeCase(version)).append("=#{").append(version).append("} and ");
                }
            }
            return sql.delete(sql.length() - 5, sql.length()).toString();
        });
    }

    public String get(Object bean) {
        return getCachedSql(bean, "get", t -> {
            StringBuilder sql = new StringBuilder();
            sql.append("select * from ").append(table(bean)).append(" where ");
            try {
                for (Field f : getCachedModelFields(bean.getClass())) {
                    if (f.isAnnotationPresent(Id.class)) {
                        sql.append(snakeCase(f.getName())).append("=#{").append(f.getName()).append("} and");
                    }
                }
                sql.delete(sql.toString().length() - 3, sql.toString().length());
            } catch (Exception e) {
                throw new RuntimeException(sql.toString(), e);
            }
            return sql.toString();
        });
    }

    public String getById(Class<?> bean, Serializable id) {
        return getCachedSql(bean, "getById", (Func<Class<?>>) t -> {
            StringBuilder sql = new StringBuilder();
            sql.append("select * from ").append(table(bean)).append(" where ");
            try {
                for (Field field : getCachedModelFields(bean)) {
                    if (field.isAnnotationPresent(Id.class)) {
                        sql.append(snakeCase(field.getName())).append("=#{param1}");
                        break;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(sql.toString(), e);
            }
            return sql.toString();
        });
    }

    public String list(Object bean) {
        StringBuilder sql = new StringBuilder();
        sql.append("select * from ").append(table(bean)).append(" where 1=1 ");
        String orderby = null;
        try {
            for (Field f : getCachedModelFields(bean.getClass())) {
                if (f.get(bean) != null) {
                    if (f.isAnnotationPresent(OrderBy.class)) {
                        orderby = f.get(bean) + "";
                        if (!orderby.contains("order by")) {
                            orderby = " order by " + orderby;
                        }
                    } else {
                        sql.append(" and ").append(snakeCase(f.getName())).append("=#{").append(f.getName()).append("}");
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(sql.toString(), e);
        }
        if (orderby != null) {
            sql.append(orderby);
        }
        return sql.toString();
    }

    public String count(Object bean) {
        StringBuilder sql = new StringBuilder();
        sql.append("select count(0) from ").append(table(bean)).append(" where 1=1 ");
        try {
            for (Field f : getCachedModelFields(bean.getClass())) {
                if (f.get(bean) != null && !f.isAnnotationPresent(OrderBy.class))
                    sql.append(" and ").append(snakeCase(f.getName())).append("=#{").append(f.getName()).append("}");
            }
        } catch (Exception e) {
            throw new RuntimeException(sql.toString(), e);
        }
        return sql.toString();
    }

    public String listByIds(Class<?> entity, Iterable<Serializable> ids) {
        StringBuilder s = new StringBuilder();
        s.append("select * from ").append(table(entity)).append(" where ");
        s.append(snakeCase(id(entity).getName())).append(" in ( ");
        for (Serializable id : ids) {
            s.append("'").append(id).append("',");
        }
        s.deleteCharAt(s.length() - 1).append(" )");
        return s.toString();
    }

    public static Field id(Class<?> entity) {
        Field id = null;
        if (idFieldsMap.containsKey(entity)) {
            id = idFieldsMap.get(entity);
        } else {
            for (Field f : getCachedModelFields(entity)) {
                if (f.isAnnotationPresent(Id.class)) {
                    if (!f.isAccessible())
                        f.setAccessible(true);
                    id = f;
                    idFieldsMap.put(entity, f);
                    break;
                }
            }
        }
        if (id == null)
            throw new RuntimeException(entity.getName() + "没有主键!");
        return id;
    }

    private static String getCachedSql(Object bean, String method, Func<Object> func) {
        String key = bean.getClass().getName() + "_" + method;
        if (sqls.get(key) != null) {
            return sqls.get(key);
        } else {
            String res = func.apply(bean);
            sqls.put(key, res);
            return res;
        }
    }

    private static String getCachedSql(Class<?> bean, String method, Func<Class<?>> func) {
        String k = bean.getName() + "_" + method;
        if (sqls.containsKey(k)) {
            return sqls.get(k);
        } else {
            String apply = func.apply(bean);
            sqls.put(k, apply);
            return apply;
        }
    }

    public static List<Field> getCachedModelFields(Class<?> beanClass) {
        if (modelFieldsMap.containsKey(beanClass.getName())) {
            return modelFieldsMap.get(beanClass.getName());
        } else {
            List<Field> fields = getDeclaredFields(beanClass);
            fields.forEach(f -> f.setAccessible(true));
            modelFieldsMap.put(beanClass.getName(), fields);
            return fields;
        }
    }

    private static List<Field> getDeclaredFields(Class<?> t) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz = t; clazz != Object.class && clazz != Class.class && clazz != Field.class; clazz = clazz.getSuperclass()) {
            try {
                for (Field f : clazz.getDeclaredFields()) {
                    int modifiers = f.getModifiers();
                    if (!Modifier.isFinal(modifiers) && !Modifier.isStatic(modifiers) && !Modifier.isNative(modifiers) && !Modifier.isTransient(modifiers)) {
                        fields.add(f);
                    }
                }
            } catch (Exception e) {
            }
        }
        return fields;
    }

    private static String table(Object bean) {
        return table(bean.getClass());
    }

    private static String table(Class<?> bean) {
        if (bean.isAnnotationPresent(Table.class) && !bean.getAnnotation(Table.class).value().isEmpty()) {
            return bean.getAnnotation(Table.class).value();
        } else {
            return snakeCase(bean.getSimpleName());
        }
    }

    private static String snakeCase(String camelCase) {
        if (camelCase != null && !camelCase.trim().isEmpty()) {
            char[] cs = camelCase.toCharArray();
            StringBuilder sb = new StringBuilder();
            sb.append(Character.toLowerCase(cs[0]));
            for (int i = 1; i < cs.length; i++) {
                if (Character.isUpperCase(cs[i])) {
                    sb.append("_").append(Character.toLowerCase(cs[i]));
                } else {
                    sb.append(camelCase.toCharArray()[i]);
                }
            }
            return sb.toString();
        } else {
            return camelCase;
        }
    }

    @FunctionalInterface
    public interface Func<T> {
        String apply(T t);
    }

    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Table {
        String value() default "";
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Id {
        boolean autoGenerated() default false;
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Transient {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface DeleteMark {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IgnoreInsert {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface IgnoreUpdate {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Version {
    }

    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface OrderBy {
    }
}