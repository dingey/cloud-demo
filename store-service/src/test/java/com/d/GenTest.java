package com.d;

import com.d.base.GeneratorTest;
import com.di.kit.MvcGenerater;
import org.junit.Test;

public class GenTest extends GeneratorTest {

    @Test
    public void test() {
        MvcGenerater g = getGenerator();
        g.setAuthor("d");
        g.setTables("store");
        g.createEntity("com.d.store.entity");
        g.createMapper("com.d.store.mapper");
        g.createServiceInterface("com.d.store.service");
        g.createServiceImpl("com.d.store.impl");
    }
}
