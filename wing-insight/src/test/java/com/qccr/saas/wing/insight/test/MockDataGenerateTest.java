package com.qccr.saas.wing.insight.test;

import com.alibaba.fastjson.JSON;
import com.qccr.saas.wing.core.mock.MockData;
import com.qccr.saas.wing.insight.test.bean.Dog;
import org.junit.Test;

public class MockDataGenerateTest {



    @Test
    public void testMockDate(){
        MockData mockDate=new MockData();
        Dog dog= (Dog) mockDate.mock(Dog.class);
        System.out.println(JSON.toJSONString(dog));
    }






}