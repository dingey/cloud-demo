package com.d;

import com.d.base.GeneratorTest;
import com.di.kit.MvcGenerater;
import org.junit.Test;

public class GenTest extends GeneratorTest {

    @Test
    public void test() {
        MvcGenerater g = getGenerator();
        g.setAuthor("d");
        g.setTables("pay_info");
        g.createEntity("com.d.pay.entity");
        g.createMapper("com.d.pay.mapper");
        g.createServiceInterface("com.d.pay.service");
        g.createServiceImpl("com.d.pay.impl");
        g.createXml("mapper/");
    }
}
