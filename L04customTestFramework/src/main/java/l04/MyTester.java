package l04;

import l04.annotations.After;
import l04.annotations.Before;
import l04.annotations.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyTester {

    public void run(Class cl){
        System.out.println("Run test...");
        Method[] methodList = ReflectionHelper.getMethodList(cl);
        Method methodBefore = null;
        Method methodAfter = null;
        List<Method> testMethodList = new ArrayList<>();

        try{
            for (Method m: methodList) {
                Annotation[] annotationList = ReflectionHelper.getAnnotationList(m);
                for(Annotation a: annotationList){
                    if(a.annotationType().equals(Before.class) && methodBefore == null){
                        methodBefore = m;
                    }
                    if(a.annotationType().equals(After.class) && methodAfter == null){
                        methodAfter = m;
                    }
                    if(a.annotationType().equals(Test.class)){
                        testMethodList.add(m);
                    }
                }

            }

            for(Method m : testMethodList){
                Object o = ReflectionHelper.instantiate(cl);
                if(o == null){
                    throw new Exception("Cannot create target object!");
                }

                if(methodBefore != null){
                    ReflectionHelper.callMethod(o,methodBefore.getName());
                }

                ReflectionHelper.callMethod(o,m.getName());

                if(methodAfter != null){
                    ReflectionHelper.callMethod(o,methodAfter.getName());
                }
            }
        }
        catch (Throwable e){
            System.out.println(e);
        }
    }
}
