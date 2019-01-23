package com.d.base;

import com.d.base.BaseEntity;
import com.d.base.BaseMapper;
import com.d.base.BaseService;
import com.d.base.BaseServiceImpl;
import com.di.kit.JdbcMeta;
import com.di.kit.MvcGenerater;
import com.di.kit.SqlProvider;

public class GeneratorTest {
    String url = "jdbc:mysql://localhost:3306/boot?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false";
    String username = "root";
    String password = "root";

    public MvcGenerater getGenerator() {
        MvcGenerater g = new MvcGenerater(url, username, password);
        g.setEntityBaseClass(BaseEntity.class);
        g.setMapperBaseClass(BaseMapper.class);
        g.setServiceInterface(BaseService.class);
        g.setServiceImpl(BaseServiceImpl.class);
        g.setTableAnnotation(SqlProvider.Table.class);
        g.setPersistence(MvcGenerater.PersistenceEnum.MYBATIS);
        g.setPrimitive(false);
        g.sqlTypeAdaptor(JdbcMeta.Type.BIT, Integer.class).sqlTypeAdaptor(JdbcMeta.Type.TINYINT, Integer.class);

        g.setGenerateCrud(false);
        g.setBaseColumnList(true);
        g.setLombok(true);
        g.setSwaggerEntity(true);
        //g.setAuthor("kit");
        //g.createEntity("com.d.user");
        //g.createMapper("com.d.user");
        //g.createServiceInterface("com.d.user");
        //g.createServiceImpl("com.d.user");
        return g;
    }
}
