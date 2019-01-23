package com.d;

import com.d.base.GeneratorTest;
import com.di.kit.MvcGenerater;
import org.junit.Test;

public class GenTest extends GeneratorTest {

    @Test
    public void test() {
        MvcGenerater g = getGenerator();
        //g.setAuthor("kit");
        g.createEntity("com.d.user");
        g.createMapper("com.d.user");
        g.createServiceInterface("com.d.user");
        g.createServiceImpl("com.d.user");
    }
}
