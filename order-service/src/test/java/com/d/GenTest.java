package com.d;

import com.d.base.GeneratorTest;
import com.di.kit.MvcGenerater;
import org.junit.Test;

public class GenTest extends GeneratorTest {

    @Test
    public void test() {
        MvcGenerater g = getGenerator();
        g.setAuthor("d");
        g.setTables("order_info","order_item");
        g.createEntity("com.d.order.entity");
        g.createMapper("com.d.order.mapper");
        g.createServiceInterface("com.d.order.service");
        g.createServiceImpl("com.d.order.impl");
        g.createXml("mapper/");
    }
}
