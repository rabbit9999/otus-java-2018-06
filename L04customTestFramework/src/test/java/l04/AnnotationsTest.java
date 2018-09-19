package l04;

import l04.annotations.After;
import l04.annotations.Before;
import l04.annotations.Test;

public class AnnotationsTest {
    public AnnotationsTest(){
        System.out.println("Call of the constructor");
    }

    @Before
    public void before(){
        System.out.println("Before");
    }

    @Test
    public void testOne(){
        System.out.println("TestOne");
    }

    @Test
    public void testTwo(){
        System.out.println("TestTwo");
    }

    @After
    public void after(){
        System.out.println("After");
    }

}
