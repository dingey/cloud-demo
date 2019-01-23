package com.d;

import com.d.base.GeneratorTest;
import com.di.kit.MvcGenerater;
import org.junit.Test;

public class GenTest extends GeneratorTest {

    @Test
    public void test() {
        MvcGenerater g = getGenerator();
        g.setAuthor("d");
        g.setTables("goods");
        g.createEntity("com.d.goods.entity");
        g.createMapper("com.d.goods.mapper");
        g.createServiceInterface("com.d.goods.service");
        g.createServiceImpl("com.d.goods.impl");
        g.createXml("mapper/");
    }
}
